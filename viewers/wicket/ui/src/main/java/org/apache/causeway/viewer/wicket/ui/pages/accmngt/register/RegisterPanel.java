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
package org.apache.causeway.viewer.wicket.ui.pages.accmngt.register;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.userreg.UserDetails;
import org.apache.causeway.applib.services.userreg.UserRegistrationService;
import org.apache.causeway.applib.services.xactn.TransactionService;
import org.apache.causeway.viewer.wicket.ui.components.widgets.bootstrap.FormGroup;
import org.apache.causeway.viewer.wicket.ui.pages.accmngt.AccountConfirmationMap;
import org.apache.causeway.viewer.wicket.ui.pages.accmngt.UsernameAvailableValidator;
import org.apache.causeway.viewer.wicket.ui.panels.PanelBase;

/**
 * A panel with a form for self-registration of a user
 */
public abstract class RegisterPanel extends PanelBase<UserDetails> {

    private static final long serialVersionUID = 1L;

    /*sonar-ignore-on*/
    protected static final String ID_REGISTER_FORM = "registerForm";
    private static final String ID_USERNAME = "username";
    private static final String ID_USERNAME_FORM_GROUP = "usernameFormGroup";
    private static final String ID_PASSWORD = "password";
    private static final String ID_PASSWORD_FORM_GROUP = "passwordFormGroup";
    private static final String ID_CONFIRM_PASSWORD = "confirmPassword";
    private static final String ID_CONFIRM_PASSWORD_FORM_GROUP = "confirmPasswordFormGroup";
    private static final String ID_EMAIL = "email";
    /*sonar-ignore-off*/

    private final UserDetails userDetails;
    private final String uuid;

    public RegisterPanel(
            final String id,
            final UserDetails userDetails,
            final String uuid) {

        super(id);
        this.userDetails = userDetails;
        this.uuid = uuid;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        RegisterForm registerForm = new RegisterForm(ID_REGISTER_FORM, uuid, userDetails); //FIXME might need injection
        add(registerForm);

        Component username = newUsernameField();
        MarkupContainer password = newPasswordField();
        MarkupContainer confirmPassword = newConfirmPasswordField();
        registerForm.add(newEqualPasswordInputValidator(password, confirmPassword));
        TextField<String> emailField = newEmailField(userDetails);

        registerForm.add(username, password, confirmPassword, emailField);

        registerForm.add(newExtraFieldsContainer("extraFieldsContainer"));
    }

    protected abstract MarkupContainer newExtraFieldsContainer(String id);

    private IFormValidator newEqualPasswordInputValidator(
            final MarkupContainer password,
            final MarkupContainer confirmPassword) {

        var passwordField = (FormComponent<?>) password.get(ID_PASSWORD);
        var confirmPasswordField = (FormComponent<?>) confirmPassword.get(ID_CONFIRM_PASSWORD);
        return new EqualPasswordInputValidator(passwordField, confirmPasswordField);
    }

    /**
     * Register user form.
     */
    private class RegisterForm extends StatelessForm<UserDetails> {

        private static final long serialVersionUID = 1L;

        @Inject private transient UserRegistrationService userRegistrationService;
        @Inject private transient TransactionService transactionService;
        @Inject private transient InteractionService interactionService;

        private final String uuid;

        /**
         * Constructor.
         *
         * @param id
         *            id of the form component
         * @param userDetails
         */
        public RegisterForm(final String id, final String uuid, final UserDetails userDetails) {

            super(id, new CompoundPropertyModel<>(userDetails));

            this.uuid = uuid;

            String email = getEmail();

            userDetails.setEmailAddress(email);
        }

        @Override
        public final void onSubmit() {
            final UserDetails userDetails = getModelObject();

            interactionService.runAnonymous(() -> {
                transactionService.runWithinCurrentTransactionElseCreateNew(() -> {
                    userRegistrationService.registerUser(userDetails);
                    removeAccountConfirmation();
                });
            });

            signIn(userDetails.getUsername(), userDetails.getPassword());
            setResponsePage(getApplication().getHomePage());
        }

        private boolean signIn(final String username, final String password) {
            return AuthenticatedWebSession.get().signIn(username, password);
        }

        private String getEmail() {
            AccountConfirmationMap accountConfirmationMap = getApplication().getMetaData(AccountConfirmationMap.KEY);
            return accountConfirmationMap.get(uuid);
        }

        private void removeAccountConfirmation() {
            AccountConfirmationMap accountConfirmationMap = getApplication().getMetaData(AccountConfirmationMap.KEY);
            accountConfirmationMap.remove(uuid);
        }
    }

    protected TextField<String> newEmailField(final UserDetails userDetails) {
        // use readonly-ish model to prevent changing the email with manual form submits
        TextField<String> emailField = new TextField<>(ID_EMAIL, new Model<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                return userDetails.getEmailAddress();
            }
        });
        return emailField;
    }

    protected MarkupContainer newConfirmPasswordField() {
        PasswordTextField confirmPassword = new PasswordTextField(ID_CONFIRM_PASSWORD);
        confirmPassword.setLabel(new ResourceModel("confirmPasswordLabel"));
        FormGroup confirmPasswordFormGroup = new FormGroup(ID_CONFIRM_PASSWORD_FORM_GROUP, confirmPassword);
        confirmPasswordFormGroup.add(confirmPassword);
        return confirmPasswordFormGroup;
    }

    protected MarkupContainer newPasswordField() {
        PasswordTextField password = new PasswordTextField(ID_PASSWORD);
        password.setLabel(new ResourceModel("passwordLabel"));
        FormGroup passwordFormGroup = new FormGroup(ID_PASSWORD_FORM_GROUP, password);
        passwordFormGroup.add(password);
        return passwordFormGroup;
    }

    protected MarkupContainer newUsernameField() {
        RequiredTextField<String> username = new RequiredTextField<>(ID_USERNAME);
        username.add(UsernameAvailableValidator.newInstance());
        FormGroup usernameFormGroup = new FormGroup(ID_USERNAME_FORM_GROUP, username);
        usernameFormGroup.add(username);
        return usernameFormGroup;
    }

}
