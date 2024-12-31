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
package org.apache.causeway.testing.h2console.ui;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.apache.causeway.core.webapp.CausewayModuleCoreWebapp;
import org.apache.causeway.testing.h2console.ui.services.H2ManagerMenu;
import org.apache.causeway.testing.h2console.ui.webmodule.WebModuleH2Console;

/**
 * @since 2.0 {@index}
 */
@Configuration
@Import({
        CausewayModuleCoreWebapp.class,

        H2ManagerMenu.class,
        WebModuleH2Console.class
})
public class CausewayModuleTestingH2ConsoleUi {

    public static final String NAMESPACE = "causeway.ext.h2Console";
}
