package com.example.demo;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MyWorkBook {
    XSSFWorkbook xssfWorkbook;
    String filename;

    public MyWorkBook(XSSFWorkbook xssfWorkbook, String filename) {
        this.xssfWorkbook = xssfWorkbook;
        this.filename = filename;
    }

    public XSSFWorkbook getXssfWorkbook() {
        return xssfWorkbook;
    }

    public void setXssfWorkbook(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
