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
package org.apache.causeway.extensions.excel.applib.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PivotUtilsTest {

    @Test
    void createAnnotationRowTest() throws IOException {

        // given
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            Row r = sheet.createRow(0);

            List<String> l = Arrays.asList("a", "b", "c");

            // when
            PivotUtils.createAnnotationRow(r,l);

            // then
            Assertions.assertThat(r.getCell(0).getStringCellValue()).isEqualTo("a");
            Assertions.assertThat(r.getCell(1).getStringCellValue()).isEqualTo("b");
            Assertions.assertThat(r.getCell(2).getStringCellValue()).isEqualTo("c");
        }

    }

    @Test
    void createOrderRowTest() throws IOException {

        // given
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            Row r = sheet.createRow(1);

            List<Integer> l = Arrays.asList(1, 0, 2);

            // when
            PivotUtils.createOrderRow(r,l);

            // then
            Assertions.assertThat(r.getCell(0).getNumericCellValue()).isEqualTo(1);
            Assertions.assertThat(r.getCell(1).getNumericCellValue()).isEqualTo(0);
            Assertions.assertThat(r.getCell(2).getNumericCellValue()).isEqualTo(2);
        }
    }

    @Test
    void cellValueEqualsTest() throws IOException {

        // given
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            Row r = sheet.createRow(0);
            Cell c1 = r.createCell(0);
            Cell c2 = r.createCell(1);

            // when numeric (double)
            c1.setCellValue(1.000000000000001);
            c2.setCellValue(1);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when (limits of double comparison)
            c1.setCellValue(1.0000000000000001);
            c2.setCellValue(1);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(true);

            // when
            c1.setCellValue(1);
            c2.setCellValue(0.9999999999999999);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when (limits of double comparison)
            c1.setCellValue(1);
            c2.setCellValue(0.99999999999999999);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(true);

            // when (empty cells or cells with values not set, can't compare)
            c1 = r.createCell(0);
            c2 = r.createCell(1);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(true);

            // when (type not equal)
            c1 = r.createCell(0);
            c1.setBlank();
            c2 = r.createCell(1);
            c2.setCellValue(false);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when string
            c1 = r.createCell(0);
            c2 = r.createCell(1);
            c1.setCellValue("a");
            c2.setCellValue("a");

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(true);

            // when case sensitive
            c1.setCellValue("a");
            c2.setCellValue("A");

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when empty string
            c1.setCellValue("");
            c2.setCellValue("");

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(true);

            // when empty string and empty cell
            c1 = r.createCell(0);
            c2 = r.createCell(1);
            c1.setCellValue("");

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when string and numeric
            c1 = r.createCell(0);
            c2 = r.createCell(1);
            c1.setCellValue("a");
            c2.setCellValue(1.2);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when string and numeric
            c1 = r.createCell(0);
            c2 = r.createCell(1);
            c1.setCellValue("a");
            c2.setCellValue(0);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

            // when boolean
            c1.setCellValue(true);
            c2.setCellValue(true);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(true);

            // when
            c1.setCellValue(false);
            c2.setCellValue(true);

            // then
            Assertions.assertThat(PivotUtils.cellValueEquals(c1,c2)).isEqualTo(false);

        }
    }

    @Test
    void addCellValueToTest() throws IOException{

        // given
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            Row r = sheet.createRow(0);

            // when source null
            Cell cSource = null;
            Cell cTarget = r.createCell(1);
            cTarget.setCellValue(1);

            PivotUtils.addCellValueTo(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget.getNumericCellValue()).isEqualTo(1);

            // when numeric
            cSource = r.createCell(0);
            cTarget = r.createCell(1);
            cSource.setCellValue(1);
            cTarget.setCellValue(2);

            PivotUtils.addCellValueTo(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget.getNumericCellValue()).isEqualTo(3);

            // when mixed: adding ignored
            cSource.setCellValue(1);
            cTarget.setCellValue("b");

            PivotUtils.addCellValueTo(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget.getStringCellValue()).isEqualTo("b");

            // when boolean: adding ignored
            cSource.setCellValue(true);
            cTarget.setCellValue(false);

            PivotUtils.addCellValueTo(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget.getBooleanCellValue()).isEqualTo(false);
        }
    }

    public void copyCellTest() throws IOException {

        // given
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            Row r = sheet.createRow(0);
            Cell cTarget = r.createCell(0);
            cTarget.setCellValue(1);

            // given source null, do nothing
            Cell cSource = null;

            // when
            PivotUtils.copyCell(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget.getNumericCellValue()).isEqualTo(1);

            // given source has no value
            cSource = r.createCell(1);

            // when
            PivotUtils.copyCell(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget).isEqualTo(cSource);
            Assertions.assertThat(cTarget.getCellType()).isEqualTo(CellType.BLANK);

            // given source has a value
            cSource = r.createCell(1);
            cSource.setCellValue("a");

            // when
            PivotUtils.copyCell(cSource, cTarget);

            // then
            Assertions.assertThat(cTarget).isEqualTo(cSource);
            Assertions.assertThat(cTarget.getStringCellValue()).isEqualTo("a");

            // given styles
            CellStyle styleSource = workbook.createCellStyle();
            cSource.setCellStyle(styleSource);
            CellStyle styleTarget = workbook.createCellStyle();
            cTarget.setCellStyle(styleTarget);

            // when
            PivotUtils.copyCell(cSource, cTarget);

            // then style is written over
            Assertions.assertThat(cTarget.getCellStyle()).isEqualTo(cSource.getCellStyle());
            Assertions.assertThat(cTarget.getCellStyle()).isEqualTo(styleSource);
        }
    }

}
