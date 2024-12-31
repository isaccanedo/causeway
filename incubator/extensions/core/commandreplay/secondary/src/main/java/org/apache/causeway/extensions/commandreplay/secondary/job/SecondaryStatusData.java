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
package org.apache.causeway.extensions.commandreplay.secondary.job;

import org.quartz.JobExecutionContext;

import org.apache.causeway.extensions.commandreplay.secondary.status.SecondaryStatus;

/**
 * @since 2.0 {@index}
 */
class SecondaryStatusData {

    private static final String KEY_SECONDARY_STATUS = SecondaryStatusData.class.getCanonicalName();

    private final JobExecutionData jobExecutionData;

    SecondaryStatusData(final JobExecutionContext jobExecutionContext) {
        this.jobExecutionData = new JobExecutionData((jobExecutionContext));
    }

    SecondaryStatus getSecondaryStatus() {
        return getSecondaryStatus(SecondaryStatus.UNKNOWN_STATE);
    }

    SecondaryStatus getSecondaryStatus(final SecondaryStatus defaultStatus) {
        var mode = jobExecutionData.getString( KEY_SECONDARY_STATUS, defaultStatus.name());
        return SecondaryStatus.valueOf(mode);
    }

    void setSecondaryStatus(final SecondaryStatus mode) {
        jobExecutionData.setString(KEY_SECONDARY_STATUS, mode.name());
    }

}

