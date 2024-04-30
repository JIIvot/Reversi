package com.alkurlaev.reversi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameScreenController implements InputProcessor {
    private final CellState[][] field = new CellState[Constants.GAME_FIELD_SIZE][Constants.GAME_FIELD_SIZE];

    private Set<FieldPosition> whiteCells;
    private Set<FieldPosition> blackCells;

    private Set<FieldPosition> accessibleToMoveCells;

    private FieldPosition hoveredCellPos = new FieldPosition(-1, -1);

    private GameScreenRenderer renderer;

    private boolean whiteTurn = false;

    public GameScreenController() {
        resetGame();
    }

    private void setFieldCellState(int x, int y, CellState cellState) {
        field[y][x] = cellState;

        if (cellState == CellState.WHITE) {
            blackCells.remove(new FieldPosition(x, y));
            whiteCells.add(new FieldPosition(x, y));
        } else {
            whiteCells.remove(new FieldPosition(x, y));
            blackCells.add(new FieldPosition(x, y));
        }
    }

    public void update(float deltaTime) {
        /* Exiting */
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        /* Toggle fullscreen mode */
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(Constants.DEFAULT_WINDOW_SIZE_WIDTH, Constants.DEFAULT_WINDOW_SIZE_HEIGHT);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            processPlayerClick();
        }
    }

    private void processPlayerClick() {
        if (!isHoveredCellAccessibleToMove()) {
            return;
        }

        setFieldCellState(hoveredCellPos.x(), hoveredCellPos.y(), whiteTurn ? CellState.WHITE : CellState.BLACK);
        captureEnemyCells(hoveredCellPos);

        whiteTurn = !whiteTurn;
        updateAccessibleCells();

        if (accessibleToMoveCells.isEmpty()) {
            whiteTurn = !whiteTurn;
            updateAccessibleCells();

            if (accessibleToMoveCells.isEmpty()) {
                finishGame();
            }
        }
    }

    private void resetGame() {
        whiteCells = new HashSet<>();
        blackCells = new HashSet<>();

        whiteTurn = false;

        for (int y = 0; y < Constants.GAME_FIELD_SIZE; y++) {
            for (int x = 0; x < Constants.GAME_FIELD_SIZE; x++) {
                field[y][x] = CellState.EMPTY;
            }
        }

        setFieldCellState(Constants.GAME_FIELD_SIZE / 2, Constants.GAME_FIELD_SIZE / 2 - 1, CellState.WHITE);
        setFieldCellState(Constants.GAME_FIELD_SIZE / 2 - 1, Constants.GAME_FIELD_SIZE / 2, CellState.WHITE);
        setFieldCellState(Constants.GAME_FIELD_SIZE / 2, Constants.GAME_FIELD_SIZE / 2, CellState.BLACK);
        setFieldCellState(Constants.GAME_FIELD_SIZE / 2 - 1, Constants.GAME_FIELD_SIZE / 2 - 1, CellState.BLACK);

        updateAccessibleCells();
    }

    private void finishGame() {
        int whiteCellsNum = whiteCells.size();
        int blackCellsNum = blackCells.size();

        String message;
        if (whiteCellsNum > blackCellsNum) {
            message = "White wins!";
        } else if (whiteCellsNum < blackCellsNum) {
            message = "Black wins!";
        } else {
            message = "Draw!";
        }

        // TODO: Show it somehow

        resetGame();
    }

    private void captureEnemyCells(FieldPosition rootCell) {
        tryCaptureEnemyCellsInDirection(rootCell, 1, 1);
        tryCaptureEnemyCellsInDirection(rootCell, 1, 0);
        tryCaptureEnemyCellsInDirection(rootCell, 0, 1);
        tryCaptureEnemyCellsInDirection(rootCell, -1, -1);
        tryCaptureEnemyCellsInDirection(rootCell, -1, 0);
        tryCaptureEnemyCellsInDirection(rootCell, 0, -1);
        tryCaptureEnemyCellsInDirection(rootCell, 1, -1);
        tryCaptureEnemyCellsInDirection(rootCell, -1, 1);
    }

    private void tryCaptureEnemyCellsInDirection(FieldPosition rootCell, int xDir, int yDir) {
        int currentX = rootCell.x() + xDir;
        int currentY = rootCell.y() + yDir;

        List<FieldPosition> possibleCapturedCells = new ArrayList<>();
        possibleCapturedCells.add(new FieldPosition(currentX, currentY));

        CellState enemyState;
        CellState playerState;
        if (whiteTurn) {
            enemyState = CellState.BLACK;
            playerState = CellState.WHITE;
        } else {
            enemyState = CellState.WHITE;
            playerState = CellState.BLACK;
        }

        while (isPosInFieldBounds(currentX, currentY) && field[currentY][currentX] == enemyState) {
            currentX += xDir;
            currentY += yDir;

            possibleCapturedCells.add(new FieldPosition(currentX, currentY));
        }

        if (!isPosInFieldBounds(currentX, currentY) || field[currentY][currentX] != playerState) {
            return;
        }

        for (FieldPosition capturedCell : possibleCapturedCells) {
            setFieldCellState(capturedCell.x(), capturedCell.y(), playerState);
        }
    }

    private boolean isPosInFieldBounds(int x, int y) {
        return (x >= 0 && x < Constants.GAME_FIELD_SIZE) && (y >= 0 && y < Constants.GAME_FIELD_SIZE);
    }

    private void updateAccessibleCells() {
        accessibleToMoveCells = new HashSet<>();

        for (FieldPosition fieldPos : whiteTurn ? whiteCells : blackCells) {
            addAllAccessibleCellsFromFieldPosition(fieldPos);
        }
    }

    private void addAllAccessibleCellsFromFieldPosition(FieldPosition fieldPosition) {
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, 1, 1);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, 1, 0);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, 0, 1);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, -1, -1);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, -1, 0);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, 0, -1);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, 1, -1);
        tryAddAccessibleCellFromFieldPositionInDirection(fieldPosition, -1, 1);
    }

    private void tryAddAccessibleCellFromFieldPositionInDirection(FieldPosition fieldPos, int xDir, int yDir) {
        int currentX = fieldPos.x() + xDir;
        int currentY = fieldPos.y() + yDir;

        CellState enemyState = whiteTurn ? CellState.BLACK : CellState.WHITE;
        while (isPosInFieldBounds(currentX, currentY) && field[currentY][currentX] == enemyState) {
            currentX += xDir;
            currentY += yDir;
        }

        if (!isPosInFieldBounds(currentX, currentY) || field[currentY][currentX] != CellState.EMPTY || field[currentY - yDir][currentX - xDir] != enemyState) {
            return;
        }

        accessibleToMoveCells.add(new FieldPosition(currentX, currentY));
    }

    public FieldPosition getHoveredCellPos() {
        return hoveredCellPos;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        int leftGutterWidth = renderer.getLeftGutterWidth();
        int bottomGutterHeight = renderer.getBottomGutterHeight();

        x -= leftGutterWidth;
        y -= bottomGutterHeight;

        int gameViewportWidth = Gdx.graphics.getWidth() - leftGutterWidth - renderer.getRightGutterWidth();
        int gameViewportHeight = Gdx.graphics.getHeight() - bottomGutterHeight - renderer.getTopGutterHeight();

        if ((x < 0 || x >= gameViewportWidth) || (y < 0 || y >= gameViewportHeight)) {
            return false;
        }

        hoveredCellPos = new FieldPosition((int) ((float) x / gameViewportWidth * Constants.GAME_FIELD_SIZE), Constants.GAME_FIELD_SIZE - 1 - (int) ((float) y / gameViewportHeight * Constants.GAME_FIELD_SIZE));

        return true;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public void setRenderer(GameScreenRenderer renderer) {
        this.renderer = renderer;
    }

    public CellState[][] getField() {
        return field;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isHoveredCellAccessibleToMove() {
        return isCellAccessibleToMove(hoveredCellPos);
    }

    private boolean isCellAccessibleToMove(FieldPosition fieldPosition) {
        return accessibleToMoveCells.contains(fieldPosition);
    }
}
