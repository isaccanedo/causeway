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
package org.apache.causeway.testing.fakedata.applib.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.causeway.applib.value.Clob;
import org.apache.causeway.commons.internal.base._Strings;
import org.apache.causeway.commons.internal.resources._Resources;

/**
 * Returns random {@link Clob} values, optionally constrained to either XMLs or RTFs.
 *
 * @since 2.0 {@index}
 */
public class CausewayClobs extends AbstractRandomValueGenerator {

    public CausewayClobs(final FakeDataService fakeDataService) {
        super(fakeDataService);
    }

    private static final List<String> fileNames = Arrays.asList(
            "a_and_c.xml",
            "all_well.xml",
            "as_you.xml",
            "com_err.xml",
            "coriolan.xml",
            "cymbelin.xml",
            "dream.xml",
            "hamlet.xml",
            "hen_iv_1.xml",
            "hen_iv_2.xml",
            "hen_v.xml",
            "hen_vi_1.xml",
            "hen_vi_2.xml",
            "hen_vi_3.xml",
            "hen_viii.xml",
            "j_caesar.xml",
            "john.xml",
            "lear.xml",
            "lll.xml",
            "m_for_m.xml",
            "m_wives.xml",
            "macbeth.xml",
            "merchant.xml",
            "much_ado.xml",
            "othello.xml",
            "pericles.xml",
            "r_and_j.xml",
            "rich_ii.xml",
            "rich_iii.xml",
            "t_night.xml",
            "taming.xml",
            "tempest.xml",
            "timon.xml",
            "titus.xml",
            "troilus.xml",
            "two_gent.xml",
            "win_tale.xml",
            "config.rtf",
            "RTF-Spec-1.7.rtf",
            "sample.rtf",
            "testrtf.rtf");

    public Clob any() {
        final List<String> fileNames = CausewayClobs.fileNames;
        return asClob(fileNames);
    }

    public Clob anyXml() {
        return asClob(fileNamesEndingWith(".xml"));
    }

    public Clob anyRtf() {
        return asClob(fileNamesEndingWith(".rtf"));
    }

    private Clob asClob(final List<String> fileNames) {
        final int randomIdx = fake.ints().upTo(fileNames.size());
        final String randomFileName = fileNames.get(randomIdx);
        return asClob(randomFileName);
    }

    private static List<String> fileNamesEndingWith(final String suffix) {
        return CausewayClobs.fileNames.stream()
                .filter(input -> input.endsWith(suffix))
                .collect(Collectors.toList());
    }

    private static Clob asClob(final String fileName) {
        try(var is = _Resources.load(CausewayBlobs.class, "clobs/" + fileName)) {
            return new Clob(fileName, mimeTypeFor(fileName), _Strings.read(is, StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String mimeTypeFor(final String fileName) {
        if(fileName.endsWith("xml")) {
            return "text/xml";
        }
        return "application/rtf";
    }

}
