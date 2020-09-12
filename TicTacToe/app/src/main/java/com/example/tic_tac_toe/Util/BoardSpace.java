package com.example.tic_tac_toe.Util;

import android.widget.Button;
import android.widget.ImageButton;

public class BoardSpace {
    private ImageButton button;
    private int select;

    public BoardSpace(ImageButton button, int select) {
        this.button = button;
        this.select = select;
    }

    public ImageButton getButton() {
        return button;
    }

    public void setButton(ImageButton button) {
        this.button = button;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
