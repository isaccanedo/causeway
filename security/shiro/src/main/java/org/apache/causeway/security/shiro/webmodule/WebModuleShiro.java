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
 */
package org.apache.causeway.security.shiro.webmodule;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.apache.shiro.config.Ini;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.config.ShiroFilterConfiguration;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.MutableWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.ShiroFilter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.services.inject.ServiceInjector;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal._Constants;
import org.apache.causeway.commons.internal.base._Casts;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.core.webapp.modules.WebModuleAbstract;
import org.apache.causeway.core.webapp.modules.WebModuleContext;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/**
 * WebModule to enable support for Shiro.
 * <p>
 * Can be customized via static {@link WebModuleShiro#setShiroEnvironmentClass(Class)}
 *
 * @since 2.0 {@index}
 */
@Service
@Named("causeway.security.WebModuleShiro")
@javax.annotation.Priority(PriorityPrecedence.FIRST + 100)
@Qualifier("Shiro")
@Log4j2
public class WebModuleShiro extends WebModuleAbstract {

    private static final String SHIRO_FILTER_NAME = "ShiroFilter";

    @Inject
    public WebModuleShiro(final ServiceInjector serviceInjector) {
        super(serviceInjector);
    }

    // -- CONFIGURATION

    public static void setShiroEnvironmentClass(final Class<? extends WebEnvironment> shiroEnvironmentClass) {
        if(shiroEnvironmentClass==null) {
            System.setProperty("shiroEnvironmentClass", null);
            return;
        }
        System.setProperty("shiroEnvironmentClass", shiroEnvironmentClass.getName());
    }

    public static class IniWebEnvironmentUsingSystemProperty extends IniWebEnvironment {
        @Override
        public Ini getIni() {
            var customShiroIniResource = System.getProperty("shiroIniResource");
            if(_Strings.isNotEmpty(customShiroIniResource)) {
                var ini = new Ini();
                ini.loadFromPath(customShiroIniResource);
                return ini;
            }
            return null;
        }
        // see https://issues.apache.org/jira/browse/SHIRO-610
        @Override
        protected Map<String, Object> getDefaults() {
            Map<String, Object> defaults = new HashMap<String, Object>();
            defaults.put(FILTER_CHAIN_RESOLVER_NAME, new PathMatchingFilterChainResolver());
            return defaults;
        }
    }

    public static void setShiroIniResource(final String resourcePath) {
        if(resourcePath==null) {
            System.setProperty("shiroIniResource", null);
            setShiroEnvironmentClass(null);
            return;
        }
        System.setProperty("shiroIniResource", resourcePath);
        setShiroEnvironmentClass(IniWebEnvironmentUsingSystemProperty.class);
    }

    /**
     * Adds support for dependency injection into security realms
     * @since 2.0
     */
    @NoArgsConstructor // don't remove, this is class is managed by Causeway
    public static class EnvironmentLoaderListenerForCauseway extends EnvironmentLoaderListener {

        @Inject private ServiceInjector serviceInjector;

        // testing support
        public EnvironmentLoaderListenerForCauseway(final ServiceInjector serviceInjector) {
            this.serviceInjector = serviceInjector;
        }

        @Override
        public void contextInitialized(final ServletContextEvent sce) {
            super.contextInitialized(sce);
        }

        @Override
        protected WebEnvironment createEnvironment(final ServletContext servletContext) {
            var shiroEnvironment = super.createEnvironment(servletContext);
            var securityManager = shiroEnvironment.getSecurityManager();

            injectServicesIntoRealms(securityManager);

            //[CAUSEWAY-3246] Shiro Filter throws NPE on init since Shiro v1.10.0
            if(shiroEnvironment.getShiroFilterConfiguration()==null) {
                _Casts.castTo(MutableWebEnvironment.class, shiroEnvironment)
                .ifPresent(mutableWebEnvironment->
                    mutableWebEnvironment.setShiroFilterConfiguration(new ShiroFilterConfiguration()));
            }

            return shiroEnvironment;
        }

        @SuppressWarnings("unchecked")
        @SneakyThrows
        public void injectServicesIntoRealms(
                final org.apache.shiro.mgt.SecurityManager securityManager) {

            // reflective access to SecurityManager.getRealms()
            var realmsGetter = ReflectionUtils
                    .findMethod(securityManager.getClass(), "getRealms");
            if(realmsGetter==null) {
                log.warn("Could not find method 'getRealms()' with Shiro's SecurityManager. "
                        + "As a consequence cannot enumerate realms.");
                return;
            }

            var realms = (Collection<Realm>) realmsGetter
                    .invoke(securityManager, _Constants.emptyObjects);

            realms.stream().forEach(serviceInjector::injectServicesInto);
        }

    }

    // --

    @Getter
    private final String name = "Shiro";

    @Override
    public void prepare(final WebModuleContext ctx) {
        super.prepare(ctx);
        var customShiroEnvironmentClassName = System.getProperty("shiroEnvironmentClass");
        if(_Strings.isEmpty(customShiroEnvironmentClassName)) {
            setShiroEnvironmentClass(IniWebEnvironmentUsingSystemProperty.class);
        }
    }

    @Override
    public Can<ServletContextListener> init(final ServletContext ctx) throws ServletException {

        registerFilter(ctx, SHIRO_FILTER_NAME, ShiroFilter.class)
            .ifPresent(filterReg -> {
                filterReg.addMappingForUrlPatterns(
                        EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC),
                        false, // filter is forced first
                        "/*");
            });

        var customShiroEnvironmentClassName = System.getProperty("shiroEnvironmentClass");
        if(_Strings.isNotEmpty(customShiroEnvironmentClassName)) {
            ctx.setInitParameter("shiroEnvironmentClass", customShiroEnvironmentClassName);
        }

        var listener = createListener(EnvironmentLoaderListenerForCauseway.class);
        return Can.ofSingleton(listener);

    }

}
