package com.zynpo.enums;


public enum SideColor {
    None(-1),
    White(0),
    Black(1);

    private int value;

    SideColor(int value) {
        if ((value < -1) || (1 < value))
            throw new IllegalArgumentException(String.format("Invalid SideColor: %d", value));

        this.value = value;
    }
}
