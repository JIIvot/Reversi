package com.alkurlaev.reversi;

import com.badlogic.gdx.graphics.Color;

public enum CellState {
    BLACK(Constants.BLACK_CELL_COLOR),
    WHITE(Constants.WHITE_CELL_COLOR),
    EMPTY(null);

    CellState(Color cellColor) {
        this.cellColor = cellColor;
    }

    public final Color cellColor;
}
