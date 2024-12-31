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
package org.apache.causeway.applib.jaxb;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.causeway.commons.internal.base._Bytes;
import org.apache.causeway.commons.internal.base._Strings;

/**
 * @since 2.0 {@index}
 */
public final class PrimitiveJaxbAdapters {

    /**
     * Uses compression. (thread-safe)
     */
    public static final class BytesAdapter extends XmlAdapter<String, byte[]> {

        @Override
        public byte[] unmarshal(String v) throws Exception {
            return _Bytes.ofCompressedUrlBase64.apply(_Strings.toBytes(v, StandardCharsets.UTF_8));
        }

        @Override
        public String marshal(byte[] v) throws Exception {
            return _Strings.ofBytes(_Bytes.asCompressedUrlBase64.apply(v), StandardCharsets.UTF_8);
        }

    }
}
