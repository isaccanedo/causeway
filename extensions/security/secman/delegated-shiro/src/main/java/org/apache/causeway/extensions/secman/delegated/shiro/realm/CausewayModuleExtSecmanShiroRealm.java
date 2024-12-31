/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.causeway.extensions.secman.delegated.shiro.realm;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.inject.ServiceInjector;
import org.apache.causeway.commons.internal.assertions._Assert;
import org.apache.causeway.commons.internal.base._NullSafe;
import org.apache.causeway.commons.internal.collections._Arrays;
import org.apache.causeway.core.config.CausewayConfiguration;
import org.apache.causeway.core.config.CausewayConfiguration.Extensions.Secman.DelegatedUsers.AutoCreatePolicy;
import org.apache.causeway.core.security.authorization.Authorizor;
import org.apache.causeway.extensions.secman.applib.user.dom.AccountType;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.causeway.extensions.secman.delegated.shiro.util.ShiroUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @since 2.0 {@index}
 */
public class CausewayModuleExtSecmanShiroRealm extends AuthorizingRealm {

    private static final String SECMAN_UNLOCK_DELEGATED_USERS = "causeway.ext.secman.unlockDelegatedUsers";
	@Inject protected ServiceInjector serviceInjector;
    @Inject protected InteractionService interactionService;
    @Inject protected PlatformTransactionManager txMan;
	@Inject protected CausewayConfiguration config;

    @Getter @Setter private AuthenticatingRealm delegateAuthenticationRealm;
    @Getter @Setter private boolean autoCreateUser = true;

    /**
     * Configures a {@link org.apache.shiro.authz.permission.PermissionResolver} that knows how to process the
     * permission strings that are provided by Causeway'
     * {@link Authorizor} for Shiro.
     */
    public CausewayModuleExtSecmanShiroRealm() {
        setPermissionResolver(new PermissionResolverForCausewayShiroAuthorizor());
    }

    /**
     * In order to not provide an attacker with additional information, the exceptions thrown here deliberately have
     * few (or no) details in their exception message. Similarly, the generic
     * {@link org.apache.shiro.authc.CredentialsException} is thrown for both a non-existent user and also an
     * invalid password.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        if (!(token instanceof UsernamePasswordToken)) {
            throw new AuthenticationException();
        }

        var usernamePasswordToken = (UsernamePasswordToken) token;
        var username = usernamePasswordToken.getUsername();
        var password = usernamePasswordToken.getPassword();

        // this code block is just an optimization, entirely optional
        {
            var alreadyAuthenticatedPrincipal =
                    getPrincipal_fromAlreadyAuthenticatedSubjectIfApplicable(token);
            if(alreadyAuthenticatedPrincipal!=null) {
                var credentials = token.getCredentials();
                var realmName = getName();
                return AuthInfoForApplicationUser.of(alreadyAuthenticatedPrincipal, realmName, credentials);
            }
        }

        // lookup from database, for roles/perms
        PrincipalForApplicationUser principal = lookupPrincipal_inApplicationUserRepository(username);

        var autoCreateUserWhenDelegatedAuthentication = hasDelegateAuthenticationRealm() && isAutoCreateUser();
        if (principal == null && autoCreateUserWhenDelegatedAuthentication) {
            // When using delegated authentication, desired behavior is to auto-create user accounts in the
            // DB only if these do successfully authenticate with the delegated authentication mechanism
            // while the newly created user will be disabled by default
            authenticateElseThrow_usingDelegatedMechanism(token);
            var newPrincipal = createPrincipal_inApplicationUserRepository(username);

            _Assert.assertNotNull(newPrincipal);

            var isAutoUnlockIfDelegatedAndAuthenticated =
                    config.getExtensions().getSecman().getDelegatedUsers().getAutoCreatePolicy()
                        == AutoCreatePolicy.AUTO_CREATE_AS_UNLOCKED;

            if(isAutoUnlockIfDelegatedAndAuthenticated) {
                principal = newPrincipal;
            } else {
                _Assert.assertTrue(newPrincipal.isLocked(),
                        ()->"As configured in " + SECMAN_UNLOCK_DELEGATED_USERS + ", auto-created user accounts are initially locked!");
                throw disabledAccountException(username); // default behavior after user auto-creation
            }
        }

        if (principal == null) {
            throw credentialsException();
        }

        if (principal.isLocked()) {
            throw disabledAccountException(principal.getUsername());
        }

        if(principal.getAccountType() == AccountType.DELEGATED) {
            authenticateElseThrow_usingDelegatedMechanism(token);
        } else {
            var checkPasswordResult = checkPassword(password, principal.getEncryptedPassword());
            switch (checkPasswordResult) {
            case OK:
                break;
            case BAD_PASSWORD:
                throw credentialsException();
            case NO_PASSWORD_ENCRYPTION_SERVICE_CONFIGURED:
                throw new AuthenticationException("No password encryption service is installed");
            default:
                throw new AuthenticationException();
            }
        }

        var credentials = token.getCredentials();
        var realmName = getName();
        return AuthInfoForApplicationUser.of(principal, realmName, credentials);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        return principals.oneByType(PrincipalForApplicationUser.class);
    }

    // -- HELPER

    /**
     * @implNote
     * This is just an optimization, entirely optional.
     * <p>
     * We reuse principal information on subjects that are already authenticated,
     * provided we are in a single realm authentication scenario.
     * @param token
     * @return {@code null} if not applicable
     */
    private PrincipalForApplicationUser getPrincipal_fromAlreadyAuthenticatedSubjectIfApplicable(
            final AuthenticationToken token) {

        // this optimization is only implemented for the simple case of a single realm setup
        if(!ShiroUtils.isSingleRealm()) {
            return null;
        }

        var currentSubject = SecurityUtils.getSubject();
        if(currentSubject!=null && currentSubject.isAuthenticated()) {
            var authenticatedPrincipalObject = currentSubject.getPrincipal();
            if(authenticatedPrincipalObject instanceof PrincipalForApplicationUser) {
                var authenticatedPrincipal = (PrincipalForApplicationUser) authenticatedPrincipalObject;
                var authenticatedUsername = authenticatedPrincipal.getUsername();
                var usernamePasswordToken = (UsernamePasswordToken) token;
                var username = usernamePasswordToken.getUsername();
                var isAuthenticatedWithThisRealm = username.equals(authenticatedUsername);
                if(isAuthenticatedWithThisRealm) {
                    return authenticatedPrincipal;
                }
            }
        }
        return null;
    }

    private DisabledAccountException disabledAccountException(final String username) {
        return new DisabledAccountException(String.format("username='%s'", username));
    }

    private CredentialsException credentialsException() {
        return new CredentialsException("Unknown user/password combination") {
            private static final long serialVersionUID = 1L;
            @Override public StackTraceElement[] getStackTrace() {
                // truncate reported stacktraces down to just 1 line
                var fullStackTrace = super.getStackTrace();
                return _NullSafe.size(fullStackTrace)>1
                        ? _Arrays.subArray(super.getStackTrace(), 0, 1)
                        : fullStackTrace;
            }
        };
    }

    private void authenticateElseThrow_usingDelegatedMechanism(final AuthenticationToken token) {
        AuthenticationInfo delegateAccount = null;
        try {
            delegateAccount = delegateAuthenticationRealm.getAuthenticationInfo(token);
        } catch (AuthenticationException ex) {
            // fall through
        }
        if(delegateAccount == null) {
            throw credentialsException();
        }
    }

    private PrincipalForApplicationUser lookupPrincipal_inApplicationUserRepository(final String username) {

        return execute(new Supplier<PrincipalForApplicationUser>() {
            @Override
            public PrincipalForApplicationUser get() {
                var applicationUser = applicationUserRepository.findByUsername(username).orElse(null);
                return PrincipalForApplicationUser.from(applicationUser);
            }
            @Inject private ApplicationUserRepository applicationUserRepository;
        });
    }

    private PrincipalForApplicationUser createPrincipal_inApplicationUserRepository(final String username) {

        return execute(new Supplier<PrincipalForApplicationUser>() {
            @Override
            public PrincipalForApplicationUser get() {
                var applicationUser = applicationUserRepository.findOrCreateUserByUsername(username);
                return PrincipalForApplicationUser.from(applicationUser);
            }
            @Inject private ApplicationUserRepository applicationUserRepository;
        });
    }

    private static enum CheckPasswordResult {
        OK,
        BAD_PASSWORD,
        NO_PASSWORD_ENCRYPTION_SERVICE_CONFIGURED
    }

    private CheckPasswordResult checkPassword(final char[] candidate, final String actualEncryptedPassword) {
        return execute(new Supplier<CheckPasswordResult>() {

            @Autowired(required = false) private @Qualifier("Secman") PasswordEncoder passwordEncoder;

            @Override
            public CheckPasswordResult get() {
                if (passwordEncoder == null) {
                    return CheckPasswordResult.NO_PASSWORD_ENCRYPTION_SERVICE_CONFIGURED;
                }
                return passwordEncoder.matches(new String(candidate), actualEncryptedPassword)
                        ? CheckPasswordResult.OK
                        : CheckPasswordResult.BAD_PASSWORD;
            }

        });
    }

    private boolean hasDelegateAuthenticationRealm() {
        return delegateAuthenticationRealm != null;
    }

    <V> V execute(final Supplier<V> closure) {
        return interactionService.callAnonymous(
                new Callable<V>() {
                    @Override
                    public V call() {
                        serviceInjector.injectServicesInto(closure);
                        return doExecute(closure);
                    }
                }
                );
    }

    <V> V doExecute(final Supplier<V> closure) {
        var txTemplate = new TransactionTemplate(txMan);
        return txTemplate.execute(status->closure.get());
    }

}
