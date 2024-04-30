package com.alkurlaev.reversi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
    private final GameScreenRenderer renderer = new GameScreenRenderer();
    private final GameScreenController controller = new GameScreenController();

    public GameScreen() {
        Gdx.input.setInputProcessor(controller);

        controller.setRenderer(renderer);
        renderer.setController(controller);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        controller.update(deltaTime);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
