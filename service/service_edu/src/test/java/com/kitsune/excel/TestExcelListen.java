package com.kitsune.excel;

import com.alibaba.excel.EasyExcel;

public class TestExcelListen {
    public static void main(String[] args) {
//      实现Excel读操作
        String fileName = "D:\\GuLiProject\\project\\Resource\\wdnmd.xlsx";

        EasyExcel.read(fileName, DemoData.class, new ExcelListener()).sheet().doRead();
    }
}
