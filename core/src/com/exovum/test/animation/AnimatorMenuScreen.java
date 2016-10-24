package com.exovum.test.animation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.EventAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by exovu_000 on 10/18/2016.
 * This is the menu screen for the Kordan Jirby running animation game.
 * It serves as the start screen when the application is opened,
 * and provides access to Credits, Instructions, and the Game itself.
 */

public class AnimatorMenuScreen implements Screen {

    private SpriteBatch batch;
    private Game game;
    private Screen screen;

    private Stage stage;
    private Table mainTable, titleTable, baseTable;
    private Table infoTable;
    private Skin skin;
    private ShapeRenderer renderer;

    private Texture menuBackground;

    public AnimatorMenuScreen(final Game game) {
        this.batch = new SpriteBatch();
        this.game = game;
        screen = this;

        menuBackground = new Texture(Gdx.files.internal("beach-ocean-sea-bg/transparent-png/full_background.png"));

        stage = new Stage(new FitViewport(800,480));
        Gdx.input.setInputProcessor(stage);
        // Setup the UI skin. Pass the TextureAtlas too so it can find the default settings.
        TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"), skinAtlas);

        mainTable = new Table(skin);
        //mainTable.defaults().expand().fill().padBottom(4f).padTop(4f);
        mainTable.setFillParent(true);

        // Add the title of the game at the top of the MenuScreen
        titleTable = new Table(skin);
        final Label titleLabel = new Label("Kordan Jirby", skin, "title");
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

        Gdx.app.log("AnimatorMenuScreen", "Color RGB SKY: " + Color.SKY.toString());
        playButton.setColor(buttonColor);
        helpButton.setColor(buttonColor);
        creditsButton.setColor(buttonColor);
        exitButton.setColor(buttonColor);
        // Set the color of the TEXT on the buttons
        //buttonColor = new Color(0.845f, 0.845f, 0.845f, 1);
        buttonColor = new Color(0.91f, 0.91f, 0.91f, 1);
        playButton.getLabel().setColor(buttonColor);
        helpButton.getLabel().setColor(buttonColor);
        creditsButton.getLabel().setColor(buttonColor);
        exitButton.getLabel().setColor(buttonColor);

        buttonTable.defaults().expand().fill().padBottom(8f).padTop(2f);
        buttonTable.add(playButton).width(180f).height(60f).row();
        buttonTable.add(helpButton).width(180f).height(60f).row();
        buttonTable.add(creditsButton).width(180f).height(60f).row();
        buttonTable.add(exitButton).width(180f).height(60f);
        buttonTable.padTop(30f).padBottom(30f);
        buttonTable.left();

        /*
            Temporary removal of infoTable
            It was not easy for me to add hidden/new text and for the layouts to update correctly.
            Changing plan to create a new Screen for Credits (and instructions too).
         */
        infoTable = new Table(skin); // = new Label("", skin, "small-font");
        infoTable.setVisible(false);
        infoTable.defaults().expand().fill().padBottom(8f).padTop(2f);
        //baseTable.add(infoTable);

        // Add title table at the top of mainTable
        mainTable.add(titleTable).row();
        // Add baseTable containing buttonTable to the next row of mainTable
        //mainTable.add(buttonTable);
        mainTable.add(baseTable);
        // Add mainTable to the stage
        stage.addActor(mainTable);

        // Event Listeners for the menu buttons
        playButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Pressed playButton");
                //game.setScreen(new AnimatorGameScreen(game, screen));
                ((Game) Gdx.app.getApplicationListener()).setScreen(new AnimatorGameScreen(game, screen));
            }
        });
        helpButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Pressed helpButton");
                //game.setScreen(new InstructionsScreen(game, screen));
                ((Game) Gdx.app.getApplicationListener()).setScreen(new InstructionsScreen(game, screen));
            }
        });
        creditsButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Pressed creditsButton");
                //game.setScreen(new CreditsScreen(game, screen));
                ((Game) Gdx.app.getApplicationListener()).setScreen(new CreditsScreen(game, screen));
                /*
                *  Attempt at adding text next to the buttons.
                *  Status: Unsuccessful. Updating the layout once adding the credits text
                *           does not update as easily as I hoped.
                *          Changing plan to use different Screens for Credits & Instructions.
                // If the table is visible and already showing credits, then 'minimize' infoTable
                if(infoTable.isVisible() && infoTable.getName() != null && infoTable.getName().equals("Credits")) {
                    Gdx.app.log("AnimatorMenuScreen", "Hide the credits menu");
                    infoTable.setVisible(false);
                    infoTable.clearChildren();
                } else {
                    Gdx.app.log("AnimatorMenuScreen", "Display the credits text");
                    // Otherwise, make the infoTable visible and set text to the credits
                    infoTable.setVisible(true);
                    infoTable.clearChildren();
                    //infoTable.center();
                    Label musicLabel = new Label("Music\n" + "Pixel Peeker Polka - slower Kevin MacLeod (incompetech.com)\n" +
                            "Licensed under Creative Commons: By Attribution 3.0 License\n" +
                            "http://creativecommons.org/licenses/by/3.0/",
                            skin, "small-font");
                    musicLabel.setColor(Color.BLACK);
                    //infoTable.addActor(musicLabel);

                    infoTable.addActor(musicLabel);
                    infoTable.padLeft(20f);

                    //musicLabel.setPosition(stage.getWidth() / 2 + 50, stage.getHeight() / 2 + 100, Align.right);
                    infoTable.defaults().expand().fill().padBottom(8f).padTop(2f);
                    musicLabel.setWidth(200f);
                    //musicLabel.setWrap(true);
                    musicLabel.setAlignment(Align.center);

                    //musicLabel.setFillParent(true);
                    // "Invalidates this actor's layout, causing layout() to happen next time
                    // validate() is called
                    infoTable.invalidate();
                    baseTable.invalidate();
                    infoTable.center();

                    //infoTable.setFillParent(true);

                    //titleTable.add(musicLabel);
                }
                infoTable.setName("Credits");
                */
            }
        });
        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorMenuScreen", "Pressed exitButton - exiting application");
                Gdx.app.exit();
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
        // Fit the background image to the viewport's screen size, instead of the stage's size
        batch.draw(menuBackground, 0, 0, stage.getViewport().getScreenWidth(),
                stage.getViewport().getScreenHeight());
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
