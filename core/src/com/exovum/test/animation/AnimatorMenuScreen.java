package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.tools.hiero.Hiero;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by exovu_000 on 10/18/2016.
 * This is the menu screen for the Kordan Jirby running animation game.
 * It serves as the start screen when the application is opened,
 * and provides access to Credits, Instructions, and the Game itself.
 */

public class AnimatorMenuScreen implements Screen {

    private SpriteBatch batch;

    private Stage stage;
    private Table mainTable, titleTable, baseTable;
    private Skin skin;
    Actor root;
    ShapeRenderer renderer;

    Texture menuBackground;

    public AnimatorMenuScreen(SpriteBatch batch) {
        this.batch = batch;

        menuBackground = new Texture(Gdx.files.internal("beach-ocean-sea-bg/transparent-png/full_background.png"));

        stage = new Stage(new StretchViewport(800,480));
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        mainTable = new Table(skin);
        //mainTable.defaults().expand().fill().padBottom(4f).padTop(4f);
        mainTable.setFillParent(true);

        // Add the title of the game at the top of the MenuScreen
        titleTable = new Table(skin);
        Label titleLabel = new Label("Kordan Jirby", skin, "title");
        titleLabel.setColor(Color.FIREBRICK);
        //titleLabel.setStyle(new Label.LabelStyle(titleFont, Color.FIREBRICK));
        titleLabel.setAlignment(Align.center, Align.center);
        titleLabel.setPosition(stage.getWidth() / 2, stage.getHeight());
        //titleLabel.setFontScale(2.0f);
        titleTable.defaults().expand().fill().padBottom(4f).padTop(4f);

        titleTable.add(titleLabel);
        titleTable.padBottom(10f);

        // Bottom/Base Table: left-child holds buttonTable for menu buttons,
        //                    right-child holds some text info ie instructions or credits
        baseTable = new Table(skin);
        baseTable.defaults().expand().fill().padBottom(10f).padTop(10f);

        // Add the buttons for the user to press: play, help, credits, exit
        TextButton playButton = new TextButton("Play Game", skin, "small-font");
        TextButton helpButton = new TextButton("Instructions", skin, "small-font");
        TextButton creditsButton = new TextButton("Credits", skin, "small-font");
        TextButton exitButton = new TextButton("Exit", skin, "small-font");

        Table buttonTable = new Table(skin);
        // Add the button table as the left-child to baseTable
        baseTable.add(buttonTable);
        //menuTable.setBackground("console2");
        // Set the color of the BACKGROUND on the buttons
        Color buttonColor = Color.SKY;
        playButton.setColor(buttonColor);
        helpButton.setColor(buttonColor);
        creditsButton.setColor(buttonColor);
        exitButton.setColor(buttonColor);
        // Set the color of the TEXT on the buttons
        buttonColor = Color.BLACK;
        playButton.getLabel().setColor(buttonColor);
        helpButton.getLabel().setColor(buttonColor);
        creditsButton.getLabel().setColor(buttonColor);
        exitButton.getLabel().setColor(buttonColor);


        buttonTable.defaults().expand().fill().padBottom(10f).padTop(10f);
        buttonTable.add(playButton).width(180f).height(60f).row();
        buttonTable.add(helpButton).width(180f).height(60f).row();
        buttonTable.add(creditsButton).width(180f).height(60f).row();
        buttonTable.add(exitButton).width(180f).height(60f);
        buttonTable.padTop(30f).padBottom(30f);

        mainTable.add(titleTable).row();
        mainTable.add(buttonTable);
        stage.addActor(mainTable);

        // Event Listeners for the menu buttons
        playButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Press playButton");
            }
        });
        helpButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Press helpButton");
            }
        });
        creditsButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Press creditsButton");
            }
        });
        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Press exitButton");
            }
        });

    }



    @Override
    public void show() {

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
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Start SpriteBatch rendering
        batch.begin();
        batch.draw(menuBackground, 0, 0, stage.getWidth(), stage.getHeight());
        // End SpriteBatch rendering
        batch.end();

        // Handle Stage rendering and updates
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.line(x1, y1, x2, y2);
        renderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        menuBackground.dispose();
    }

}
