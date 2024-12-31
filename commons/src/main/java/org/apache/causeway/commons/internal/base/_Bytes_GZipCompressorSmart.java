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
package org.apache.causeway.commons.internal.base;

import java.io.IOException;
import java.util.Arrays;

import org.apache.causeway.commons.internal.exceptions._Exceptions;

import lombok.experimental.UtilityClass;

/**
 * package private utility for {@link _Bytes}
 */
@UtilityClass
class _Bytes_GZipCompressorSmart {

    private static final int INPUT_LENGTH_THRESHOLD_FOR_SMART_COMPRESSION = 256;
    private static final int GZIP_MIN_OVERHEAD = 18;
    private static final byte COMPRESSION_NONE = 0;
    private static final byte COMPRESSION_GZIP = 1;

    byte[] compress(final byte[] input) throws IOException {
        if(input.length<GZIP_MIN_OVERHEAD) {
            return input;
        }
        if(input.length<INPUT_LENGTH_THRESHOLD_FOR_SMART_COMPRESSION) {
            return _Bytes.prepend(input, COMPRESSION_NONE); // prefix the input
        } else {
            return _Bytes.prepend(_Bytes_GZipCompressor.compress(input), COMPRESSION_GZIP); // prefix the input
        }
    }

    byte[] decompress(final byte[] input) throws IOException {
        if(input==null || input.length<GZIP_MIN_OVERHEAD)
            return input;

        final byte[] inputWithoutPrefix = Arrays.copyOfRange(input, 1, input.length);

        return isCompressed(input) ? _Bytes_GZipCompressor.decompress(inputWithoutPrefix) : inputWithoutPrefix;

    }

    // -- HELPER

    private boolean isCompressed(final byte[] input) {
        switch (input[0]) {
        case COMPRESSION_NONE:
            return false;
        case COMPRESSION_GZIP:
            return true;
        default:
            throw _Exceptions.unmatchedCase(input[0]);
        }

    }

}
