package com.exovum.test.animation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by exovu_000 on 10/22/2016.
 * A Screen implementation to display the Credits, accessed from the AnimatorMenuScreen.
 */

public class CreditsScreen implements Screen {

    SpriteBatch batch;
    Game game;

    private Stage stage;
    private Table mainTable, baseTable;
    private Table infoTable;
    private Skin skin;
    ShapeRenderer renderer;

    Texture menuBackground;

    public CreditsScreen(final SpriteBatch batch, final Game game) {
        this.batch = batch;
        this.game = game;

        Gdx.app.log("CreditsScreen", "Creating CreditsScreen");

        menuBackground = new Texture(Gdx.files.internal("beach-ocean-sea-bg/transparent-png/full_background.png"));

        stage = new Stage(new FitViewport(800,480));
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        mainTable = new Table(skin);
        //mainTable.defaults().expand().fill().padBottom(4f).padTop(4f);
        mainTable.setFillParent(true);

        baseTable = new Table(skin);
        baseTable.defaults().expand().fill().padBottom(10f).padTop(10f);

        Label myHeader = new Label("General", skin, "small-font");
        myHeader.setColor(Color.FIREBRICK);
        myHeader.setAlignment(Align.center);

        Label myCredits = new Label("Programming and Development\n" +
                "Caleb Stevenson", skin, "small-font");
        myCredits.setColor(Color.BLACK);
        myCredits.setAlignment(Align.center);

        Label musicHeader = new Label("Music", skin, "small-font");
        musicHeader.setColor(Color.FIREBRICK);
        musicHeader.setAlignment(Align.center);

        Label musicCredits = new Label("\"Pixel Peeker Polka - slower\", \"Rainbows\"\n" +
                "Kevin MacLeod (incompetech.com)\n" +
                "Licensed under Creative Commons: By Attribution 3.0\n" +
                "http://creativecommons.org/licenses/by/3.0/", skin, "small-font");
        musicCredits.setColor(Color.BLACK);
        musicCredits.setAlignment(Align.center);

        Label artHeader = new Label("Artwork", skin, "small-font");
        artHeader.setColor(Color.FIREBRICK);
        artHeader.setAlignment(Align.center);

        Label artCredits = new Label("Background and Tree Sprites\n" +
                "http://bevouliin.com\nopengameart.org", skin, "small-font");
        artCredits.setColor(Color.BLACK);

        artCredits.setAlignment(Align.center);

        TextButton exitButton = new TextButton("Back to Menu", skin, "small-font");

        Table buttonTable = new Table(skin);
        baseTable.add(myHeader).row();
        baseTable.add(myCredits).row();
        baseTable.add(musicHeader).row();
        baseTable.add(musicCredits).row();
        baseTable.add(artHeader).row();
        baseTable.add(artCredits).row();
        baseTable.add(buttonTable);
        //menuTable.setBackground("console2");
        // Set the color of the BACKGROUND on the buttons
        Color buttonColor = Color.SKY;
        exitButton.setColor(buttonColor);
        // Set the color of the TEXT on the buttons
        exitButton.getLabel().setColor(Color.BLACK);


        buttonTable.defaults().expand().fill().padBottom(4f).padTop(2f);
        buttonTable.add(exitButton).width(180f).height(60f);
        //buttonTable.padTop(20f).padBottom(20f);
        buttonTable.left();

        // Add baseTable containing buttonTable to the next row of mainTable
        //mainTable.add(buttonTable);
        mainTable.add(baseTable);
        // Add mainTable to the stage
        stage.addActor(mainTable);

        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("CreditsScreen", "Exiting to main menu");
                game.setScreen(new AnimatorMenuScreen(batch, game));
            }
        });
        /*exitButton.addAction(run(new Runnable() {

            @Override
            public void run() {
                Gdx.app.log("CreditsScreen", "Exiting to main menu");
                game.setScreen(new AnimatorMenuScreen(batch, game)); //prevScreen);
            }
        }));
        */
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
