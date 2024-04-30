package com.alkurlaev.reversi;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    public static final int GAME_RESOLUTION = 320;

    public static final int DEFAULT_WINDOW_SIZE_WIDTH = 600;
    public static final int DEFAULT_WINDOW_SIZE_HEIGHT = 600;

    public static final int GAME_FIELD_SIZE = 8;

    public static final float GAME_CELL_SIZE = (float) GAME_RESOLUTION / GAME_FIELD_SIZE;

    public static final Color BASE_CELL_FIELD_COLOR = Color.LIGHT_GRAY;
    public static final Color HOVERED_CELL_COLOR = new Color(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 0.6f);
    public static final Color FIELD_SEPARATION_LINE_COLOR = Color.DARK_GRAY;
    public static final Color BLACK_CELL_COLOR = Color.BLACK;
    public static final Color WHITE_CELL_COLOR = Color.WHITE;

    public static final float HOVERED_CHIP_COLOR_ALPHA_CHANNEL = 0.5f;

    public static final int FIELD_SEPARATION_LINE_WIDTH = 2;

    public static final int GAME_CHIP_RADIUS = 15;

    private Constants() {
    }
}
