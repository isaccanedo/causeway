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
package org.apache.causeway.core.metamodel.valuesemantics;

import java.awt.image.BufferedImage;
import java.util.Optional;

import org.apache.causeway.core.metamodel.object.ManagedObject;

public interface ImageValueSemantics {

    int getHeight(ManagedObject object);

    int getWidth(ManagedObject object);

    /**
     * @return optionally the contents as a {@link BufferedImage} based on
     * whether the wrapped content is not empty
     */
    Optional<BufferedImage> getImage(ManagedObject object);
}
