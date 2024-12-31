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
package org.apache.causeway.extensions.secman.applib;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.apache.causeway.extensions.secman.applib.feature.api.ApplicationFeatureChoices;
import org.apache.causeway.extensions.secman.applib.feature.contributions.ApplicationFeatureViewModel_permissions;
import org.apache.causeway.extensions.secman.applib.permission.app.ApplicationOrphanedPermissionManager;
import org.apache.causeway.extensions.secman.applib.permission.app.mixins.ApplicationOrphanedPermissionManager_relocateSelected;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_allow;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_changing;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_delete;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_feature;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_updateRole;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_veto;
import org.apache.causeway.extensions.secman.applib.permission.dom.mixins.ApplicationPermission_viewing;
import org.apache.causeway.extensions.secman.applib.permission.menu.ApplicationPermissionMenu;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_addPermission;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_addUser;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_delete;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_removePermissions;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_removeUsers;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_updateDescription;
import org.apache.causeway.extensions.secman.applib.role.dom.mixins.ApplicationRole_updateName;
import org.apache.causeway.extensions.secman.applib.role.man.mixins.ApplicationRoleManager_allRoles;
import org.apache.causeway.extensions.secman.applib.role.man.mixins.ApplicationRoleManager_exportAsYaml;
import org.apache.causeway.extensions.secman.applib.role.man.mixins.ApplicationRoleManager_newRole;
import org.apache.causeway.extensions.secman.applib.role.menu.ApplicationRoleMenu;
import org.apache.causeway.extensions.secman.applib.seed.SeedSecurityModuleService;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_addChild;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_addUser;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_delete;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_removeChild;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_removeUser;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_updateName;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.mixins.ApplicationTenancy_users;
import org.apache.causeway.extensions.secman.applib.tenancy.man.mixins.ApplicationTenancyManager_allTenancies;
import org.apache.causeway.extensions.secman.applib.tenancy.man.mixins.ApplicationTenancyManager_newTenancy;
import org.apache.causeway.extensions.secman.applib.tenancy.menu.ApplicationTenancyMenu;
import org.apache.causeway.extensions.secman.applib.user.contributions.HasUsername_associatedUser;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_addRole;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_delete;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_duplicate;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_lock;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_removeRoles;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_resetPassword;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_timeZone;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_unlock;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateAccountType;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateAtPath;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateEmailAddress;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateFaxNumber;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateLocale;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateName;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updatePassword;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updatePhoneNumber;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateUsername;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.perms.ApplicationUser_effectiveMemberPermissions;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.perms.ApplicationUser_filterEffectiveMemberPermissions;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.perms.UserPermissionViewModel;
import org.apache.causeway.extensions.secman.applib.user.man.ApplicationUserManager;
import org.apache.causeway.extensions.secman.applib.user.man.mixins.ApplicationUserManager_allUsers;
import org.apache.causeway.extensions.secman.applib.user.man.mixins.ApplicationUserManager_newDelegateUser;
import org.apache.causeway.extensions.secman.applib.user.man.mixins.ApplicationUserManager_newLocalUser;
import org.apache.causeway.extensions.secman.applib.user.menu.ApplicationUserMenu;
import org.apache.causeway.extensions.secman.applib.user.menu.MeService;
import org.apache.causeway.testing.fixtures.applib.CausewayModuleTestingFixturesApplib;

/**
 * @since 2.0 {@index}
 */
@Configuration
@Import({
        // Modules
        CausewayModuleTestingFixturesApplib.class,

        ApplicationFeatureChoices.class,

        // @DomainService
        ApplicationOrphanedPermissionManager.class,
        ApplicationPermissionMenu.class,
        ApplicationRoleMenu.class,
        ApplicationTenancyMenu.class,
        ApplicationUserMenu.class,

        MeService.class,
        MeService.UserMenuMeActionAdvisor.class,

        // -- ViewModels
        ApplicationUserManager.class,
        UserPermissionViewModel.class,
        ApplicationOrphanedPermissionManager.class,

        // -- Mixins
        ApplicationOrphanedPermissionManager_relocateSelected.class,

        //ApplicationPermission
        ApplicationPermission_allow.class,
        ApplicationPermission_changing.class,
        ApplicationPermission_delete.class,
        ApplicationPermission_feature.class,
        ApplicationPermission_updateRole.class,
        ApplicationPermission_veto.class,
        ApplicationPermission_viewing.class,

        //ApplicationRole
        ApplicationRole_addPermission.class,
        ApplicationRole_addUser.class,
        ApplicationRole_delete.class,
        ApplicationRole_removePermissions.class,
        ApplicationRole_removeUsers.class,
        ApplicationRole_updateDescription.class,
        ApplicationRole_updateName.class,

        //ApplicationTenancy
        ApplicationTenancy_addChild.class,
        ApplicationTenancy_addUser.class,
        ApplicationTenancy_delete.class,
        ApplicationTenancy_removeChild.class,
        ApplicationTenancy_removeUser.class,
        ApplicationTenancy_updateName.class,
        ApplicationTenancy_users.class,

        //ApplicationUser
        ApplicationUser_addRole.class,
        ApplicationUser_delete.class,
        ApplicationUser_duplicate.class,
        ApplicationUser_filterEffectiveMemberPermissions.class,
        ApplicationUser_lock.class,
        ApplicationUser_effectiveMemberPermissions.class,
        ApplicationUser_removeRoles.class,
        ApplicationUser_resetPassword.class,
        ApplicationUser_timeZone.class,
        ApplicationUser_unlock.class,
        ApplicationUser_updateAccountType.class,
        ApplicationUser_updateAtPath.class,
        ApplicationUser_updateEmailAddress.class,
        ApplicationUser_updateFaxNumber.class,
        ApplicationUser_updateLocale.class,
        ApplicationUser_updateName.class,
        ApplicationUser_updatePassword.class,
        ApplicationUser_updatePhoneNumber.class,
        ApplicationUser_updateUsername.class,

        // ApplicationFeatureViewModel
        ApplicationFeatureViewModel_permissions.class,

        // HasUsername
        HasUsername_associatedUser.class,

        // ApplicationUserManager
        ApplicationUserManager_allUsers.class,
        ApplicationUserManager_newDelegateUser.class,
        ApplicationUserManager_newLocalUser.class,

        // ApplicationRoleManager
        ApplicationRoleManager_allRoles.class,
        ApplicationRoleManager_newRole.class,
        ApplicationRoleManager_exportAsYaml.class,

        // ApplicationRoleManager
        ApplicationTenancyManager_allTenancies.class,
        ApplicationTenancyManager_newTenancy.class,

        // other @Services
        SeedSecurityModuleService.class,

})
public class CausewayModuleExtSecmanApplib {

    public static final String NAMESPACE = "causeway.ext.secman";
    public static final String SCHEMA = "causewayExtSecman";

    public abstract static class TitleUiEvent<S>
            extends org.apache.causeway.applib.events.ui.TitleUiEvent<S> { }

    public abstract static class IconUiEvent<S>
            extends org.apache.causeway.applib.events.ui.IconUiEvent<S> { }

    public abstract static class CssClassUiEvent<S>
            extends org.apache.causeway.applib.events.ui.CssClassUiEvent<S> { }

    public abstract static class LayoutUiEvent<S>
            extends org.apache.causeway.applib.events.ui.LayoutUiEvent<S> { }

    public abstract static class ActionDomainEvent<S>
    extends org.apache.causeway.applib.events.domain.ActionDomainEvent<S> {}

    public abstract static class CollectionDomainEvent<S, T>
    extends org.apache.causeway.applib.events.domain.CollectionDomainEvent<S, T> {}

    public abstract static class PropertyDomainEvent<S, T>
    extends org.apache.causeway.applib.events.domain.PropertyDomainEvent<S, T> {}

}
