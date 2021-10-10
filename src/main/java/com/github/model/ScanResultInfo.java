package com.github.model;

import com.github.common.CommonConstant;

public class ScanResultInfo {

    public ScanResultInfo(int line, String text) {
        this.line = line;
        this.text = text;
    }

    public ScanResultInfo(String path, int line, String text) {
        this.path = path;
        this.line = line;
        this.text = text;
    }

    public String path;
    public int line;
    public String text;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format(CommonConstant.MSG_TEMPLATE, this.path, this.text, this.line);
    }
}
