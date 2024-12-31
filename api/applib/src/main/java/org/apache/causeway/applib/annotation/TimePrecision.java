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
package org.apache.causeway.applib.annotation;

/**
 * Precision for time of day.
 * @since 2.x {@index}
 */
public enum TimePrecision {

    UNSPECIFIED,

    /**
     * 9 fractional digits for <i>Second</i>
     */
    NANO_SECOND,

    /**
     * 6 fractional digits for <i>Second</i>
     */
    MICRO_SECOND,

    /**
     * 3 fractional digits for <i>Second</i>
     */
    MILLI_SECOND,

    /**
     * <i>Second</i>
     */
    SECOND,

    /**
     * <i>Minute</i>
     */
    MINUTE,

    /**
     * <i>Hour</i>
     */
    HOUR;

}
