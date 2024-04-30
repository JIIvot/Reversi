package com.alkurlaev.reversi;

import com.badlogic.gdx.graphics.Color;

public interface Constants {
    int GAME_RESOLUTION = 320;

    int DEFAULT_WINDOW_SIZE_WIDTH = 600;
    int DEFAULT_WINDOW_SIZE_HEIGHT = 600;

    int GAME_FIELD_SIZE = 8;

    float GAME_CELL_SIZE = (float) GAME_RESOLUTION / GAME_FIELD_SIZE;

    Color BASE_CELL_FIELD_COLOR = new Color(168.0f / 255.0f, 218.0f / 255.0f, 220.0f / 255.0f, 1.0f);
    Color HOVERED_CELL_COLOR = new Color(69.0f / 255.0f, 123.0f / 255.0f, 157.0f / 255.0f, 0.6f);
    Color FIELD_SEPARATION_LINE_COLOR = new Color(29.0f / 255.0f, 53.0f / 255.0f, 87.0f / 255.0f, 1.0f);
    Color BLACK_CELL_COLOR = new Color(230.0f / 255.0f, 57.0f / 255.0f, 70.0f / 255.0f, 1.0f);
    Color WHITE_CELL_COLOR = new Color(241.0f / 255.0f, 250.0f / 255.0f, 238.0f / 255.0f, 1.0f);

    float HOVERED_CHIP_COLOR_ALPHA_CHANNEL = 0.5f;

    int FIELD_SEPARATION_LINE_WIDTH = 2;

    int GAME_CHIP_RADIUS = 15;
}
