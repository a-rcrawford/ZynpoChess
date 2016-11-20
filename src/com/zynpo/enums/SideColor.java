package com.zynpo.enums;


import javafx.geometry.Side;

public enum SideColor {
    None(-1),

    White(0) {
        @Override
        public SideColor opposingSideColor() { return Black; }
    },

    Black(1) {
        @Override
        public SideColor opposingSideColor() { return White; }
    };

    private int value;

    SideColor(int value) {
        if ((value < -1) || (1 < value))
            throw new IllegalArgumentException(String.format("Invalid SideColor: %d", value));

        this.value = value;
    }

    public SideColor opposingSideColor() { return None; }
}
