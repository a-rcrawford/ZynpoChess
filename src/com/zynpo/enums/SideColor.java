package com.zynpo.enums;


public enum SideColor {
    None(-1),
    White(0),
    Black(1);

    private int value;

    SideColor(int value) {
        this.value = value;
    }
}
