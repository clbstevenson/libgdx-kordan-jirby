package com.exovum.test.animation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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

/**
 * Created by exovu_000 on 10/22/2016.
 * A Screen implementation to display the Credits, accessed from the AnimatorMenuScreen.
 */

public class InstructionsScreen implements Screen {

    private SpriteBatch batch;
    private Game game;
    private Screen parent;

    private Stage stage;
    private Table mainTable, baseTable;
    private Skin skin;

    private Texture menuBackground;

    public InstructionsScreen(final Game game, Screen parentScreen) {
        this.batch = new SpriteBatch();
        this.game = game;
        this.parent = parentScreen;

        Gdx.app.log("InstructionsScreen", "Creating InstructionsScreen");

        menuBackground = new Texture(Gdx.files.internal("beach-ocean-sea-bg/transparent-png/full_background.png"));

        stage = new Stage(new FitViewport(800,480));
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        mainTable = new Table(skin);
        //mainTable.defaults().expand().fill().padBottom(4f).padTop(4f);
        mainTable.setFillParent(true);

        baseTable = new Table(skin);
        baseTable.defaults().expand().fill().padBottom(10f).padTop(10f);

        Label myHeader = new Label("Instructions", skin, "small-font");
        myHeader.setColor(Color.FIREBRICK);
        myHeader.setAlignment(Align.center);

        Label instructions = new Label("In accordance with Her Majesty's royal \ndecree 412.G subsection IV,\n" +
                "servants whom exhibit the pinnacle of dexterity,\n" +
                "constitution, and adoration for Oxford commas were selected.\n" +
                "However, due to recent budget cuts \nand the first goblin raids since '42,\n" +
                "you have been randomly chosen for royal duty\n" +
                "as a result of no outstanding mark or merit of your own.\n" +
                "May your service, however short, benefit Her Majesty.",
                skin, "small-font");
        instructions.setColor(Color.DARK_GRAY);
        instructions.setWrap(false);
        instructions.setAlignment(Align.center);
        // NOTE FOR LATER:
        // Can also change this flavor text to "to allow you to jump twice as high over those
        // aborreal abominations...etc
        Label psInstructions = new Label("P.S. You have also been outfitted with an internal hypsometer,\n" +
                "so just swipe the air in front of you for an extra boost.", skin, "small-font");
        psInstructions.setColor(Color.FIREBRICK);
        psInstructions.setAlignment(Align.center);

        TextButton exitButton = new TextButton("Back to Menu", skin, "small-font");


        Table buttonTable = new Table(skin);
        baseTable.add(myHeader).row();
        baseTable.add(instructions).row();
        baseTable.add(psInstructions).row();
        baseTable.add(buttonTable);
        //menuTable.setBackground("console2");
        // Set the color of the BACKGROUND on the buttons
        Color buttonColor = Color.SKY;
        exitButton.setColor(buttonColor);
        // Set the color of the TEXT on the buttons
        exitButton.getLabel().setColor(new Color(0.91f, 0.91f, 0.91f, 1));


        buttonTable.defaults().expand().fill().padBottom(4f).padTop(2f);
        buttonTable.add(exitButton).width(180f).height(60f);
        //buttonTable.padTop(20f).padBottom(20f);
        buttonTable.left();

        final Rectangle exitRectangle = new Rectangle(exitButton.getX(), exitButton.getY(),
                exitButton.getWidth(), exitButton.getHeight());

       // exitButton.padTop(2f);


        // Add baseTable containing buttonTable to the next row of mainTable
        //mainTable.add(buttonTable);
        mainTable.add(baseTable);
        // Add mainTable to the stage
        stage.addActor(mainTable);

        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("InstructionsScreen", "Exiting to main menu");
                //game.setScreen(new AnimatorMenuScreen(game));
                //game.setScreen(parent);
                //((Game) Gdx.app.getApplicationListener()).setScreen(parent);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new AnimatorMenuScreen(game));
            }
        });

        //Use an InputMultiplexer so that the Stage and keyDown input processors can both be used
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter(){
            // If the back key is pressed, go to main menu
            // This also handles the Android 'back' button
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK) {
                    // Handle the back button
                    Gdx.app.log("InstructionsScreen", "KeyDown: BACK pressed");
                    //AnimatorMenuScreen newMenu = new AnimatorMenuScreen(batch, game);
                    //game.setScreen(newMenu);
                    //game.setScreen(parent);
                    //((Game) Gdx.app.getApplicationListener()).setScreen(parent);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new AnimatorMenuScreen(game));
                    return true;
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);
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
