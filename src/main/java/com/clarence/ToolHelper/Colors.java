package com.clarence.ToolHelper;

public enum Colors {
    GRAY("&7"),
    GREEN("&a");

    private String code;

    Colors(String code) {
        this.code = code;
    }
    public String getCode() {return code; }
}
