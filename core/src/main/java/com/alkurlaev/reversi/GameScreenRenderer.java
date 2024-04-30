package com.alkurlaev.reversi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreenRenderer implements Disposable {
    private final OrthographicCamera camera = new OrthographicCamera();
    private final FitViewport viewport = new FitViewport(Constants.GAME_RESOLUTION, Constants.GAME_RESOLUTION, camera);

    private final ShapeRenderer renderer = new ShapeRenderer();

    private GameScreenController controller;

    public GameScreenRenderer() {
        camera.position.x += Constants.GAME_RESOLUTION / 2.0f;
        camera.position.y += Constants.GAME_RESOLUTION / 2.0f;
    }

    public void render() {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
        viewport.apply();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderBackground();
        renderField();
        renderHoveredCell();
        renderFieldSeparationLines();

        renderer.end();

        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }

    private void renderField() {
        renderer.setColor(Constants.WHITE_CELL_COLOR);
        for (FieldPosition cell : controller.getWhiteCells()) {
            renderGameChip(cell.x(), cell.y());
        }

        renderer.setColor(Constants.BLACK_CELL_COLOR);
        for (FieldPosition cell : controller.getBlackCells()) {
            renderGameChip(cell.x(), cell.y());
        }
    }

    private void renderGameChip(int fieldPosX, int fieldPosY) {
        renderer.circle((Constants.GAME_CELL_SIZE - Constants.FIELD_SEPARATION_LINE_WIDTH * 0.0f) / 2.0f + (fieldPosX * Constants.GAME_CELL_SIZE), (Constants.GAME_CELL_SIZE - Constants.FIELD_SEPARATION_LINE_WIDTH * 0.0f) / 2.0f + (fieldPosY * Constants.GAME_CELL_SIZE), Constants.GAME_CHIP_RADIUS);
    }

    private void renderBackground() {
        renderer.setColor(Constants.BASE_CELL_FIELD_COLOR);
        renderer.rect(0, 0, Constants.GAME_RESOLUTION, Constants.GAME_RESOLUTION);
    }

    private void renderFieldSeparationLines() {
        renderer.setColor(Constants.FIELD_SEPARATION_LINE_COLOR);

        for (int i = 1; i < Constants.GAME_FIELD_SIZE; i++) {
            renderer.rect(i * Constants.GAME_CELL_SIZE - Constants.FIELD_SEPARATION_LINE_WIDTH / 2.0f, 0, Constants.FIELD_SEPARATION_LINE_WIDTH, Constants.GAME_RESOLUTION);
        }

        for (int i = 1; i < Constants.GAME_FIELD_SIZE; i++) {
            renderer.rect(0, i * Constants.GAME_CELL_SIZE - Constants.FIELD_SEPARATION_LINE_WIDTH / 2.0f, Constants.GAME_RESOLUTION, Constants.FIELD_SEPARATION_LINE_WIDTH);
        }
    }

    private void renderHoveredCell() {
        FieldPosition hoveredCellPos = controller.getHoveredCellPos();
        if (hoveredCellPos == null) {
            return;
        }

        int posX = (int) ((float) hoveredCellPos.x() / Constants.GAME_FIELD_SIZE * Constants.GAME_RESOLUTION);
        int posY = (int) ((float) hoveredCellPos.y() / Constants.GAME_FIELD_SIZE * Constants.GAME_RESOLUTION);

        renderer.setColor(Constants.HOVERED_CELL_COLOR);
        renderer.rect(posX, posY, Constants.GAME_CELL_SIZE, Constants.GAME_CELL_SIZE);

        if (!controller.isHoveredCellAccessibleToMove()) {
            return;
        }

        Color baseChipColor = controller.isWhiteTurn() ? Constants.WHITE_CELL_COLOR : Constants.BLACK_CELL_COLOR;
        renderer.setColor(baseChipColor.r, baseChipColor.g, baseChipColor.b, Constants.HOVERED_CHIP_COLOR_ALPHA_CHANNEL);
        renderGameChip(hoveredCellPos.x(), hoveredCellPos.y());
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public void setController(GameScreenController controller) {
        this.controller = controller;
    }

    public int getRightGutterWidth() {
        return viewport.getRightGutterWidth();
    }

    public int getLeftGutterWidth() {
        return viewport.getLeftGutterWidth();
    }

    public int getTopGutterHeight() {
        return viewport.getTopGutterHeight();
    }

    public int getBottomGutterHeight() {
        return viewport.getBottomGutterHeight();
    }
}
