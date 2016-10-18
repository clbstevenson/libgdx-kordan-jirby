package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by exovu_000 on 10/18/2016.
 * This is the menu screen for the Kordan Jirby running animation game.
 * It serves as the start screen when the application is opened,
 * and provides access to Credits, Instructions, and the Game itself.
 */

public class AnimatorMenuScreen implements Screen {

    private SpriteBatch batch;

    private Stage stage;
    private Table table;

    public AnimatorMenuScreen(SpriteBatch batch) {
        this.batch = batch;

        // Sample setup for a Stage with a Table UI
        stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true); // This is optional, but enables debug lines for tables.

        // Add widgets to the table
    }



    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
