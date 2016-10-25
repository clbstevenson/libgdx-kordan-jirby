package com.exovum.test.animation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

/**
 * Created by exovu_000 on 10/14/2016.
 * This is the main game Screen for displaying the jkirby running animation.
 * It uses a camera and viewport to render the sprites and scale them accordingly.
 * The running animation sprite [AnimatedSprite] stays on the same spot on the screen, but the
 * background will move behind it. Obstacles will appear and the player will need to jump high
 * enough to avoid the traps.
 */

public class AnimatorGameScreen implements Screen {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private BitmapFont font, smallFont;
    private GlyphLayout glyphLayout;
    private FreeTypeFontParameter parameter;
    private FreeTypeFontGenerator generator;

    private Viewport viewport;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Game game;
    private Screen parent;

    private Music gameMusic;

    private TextureAtlas atlas;

    private Stage stage;
    private Skin skin;
    private TextButton backButton;

    private Animation jkirbyAnimation;
    private AnimatedPlayer jkirbyAnimatedSprite;

    // The two Sprites for the map are used to "loop" the map as the player runs
    private Sprite mapSprite, mapSprite2;
    // The current map being displayed rendered
    private Sprite currentMap;
    // Boolean to determine which map is on the main screen; used to know when to switch map positions
    private boolean displayMap1;

    // Sprites for obstacles
    private TrapSprite shortTree, tallTree;
    private Array<TrapSprite> traps;

    private float distanceTraveled;
    private float moveSpeed;
    private float floorPos;

    private boolean paused;


    public AnimatorGameScreen(final Game game, final Screen parentScreen) {
        this.batch = new SpriteBatch();
        this.game = game;
        this.parent = parentScreen;


        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        // Set the floor to be 100 below the middle of the screen
        floorPos = -150;
        // Set the initial movement speed of camera/player [can be changed later]
        moveSpeed = 4.5f;
        // Initialize the player's distance traveled to 0; accumulates based on moveSpeed
        distanceTraveled = 0;

        //font = new BitmapFont(Gdx.files.internal("fonts/boxy_bold_font.png"));
        glyphLayout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Boxy-Bold.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 26; // font size
        font = generator.generateFont(parameter); // font size of 12 pizels
        font.setColor(Color.BLACK);
        parameter.size = 16;
        smallFont = generator.generateFont(parameter);
        smallFont.setColor(Color.BLACK);

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(30, 30 * (h / w));
        viewport = new FitViewport(800, 480, camera);
        camera.update();
        //viewport.update(800, 480);

        stage = new Stage(viewport, this.batch);
        stage.setViewport(viewport);
        stage.getViewport().update(800, 480);
        camera.update();
        camera.position.setZero();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        backButton = new TextButton("Back to Menu", skin, "small-font");
        backButton.setWidth(180f);
        backButton.setHeight(60f);
        //stage.addActor(backButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("AnimatorGameScreen", "Pressed back button");
                game.setScreen(new AnimatorMenuScreen(game));
            }
        });
        //camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        //camera.update();

        // Start the "displayFirstMap" flag as true, because map1 is shown first
        displayMap1 = true;
        mapSprite = new Sprite(new Texture(Gdx.files.internal
                ("beach-ocean-sea-bg/transparent-png/full_background.png")));
        mapSprite2 = new Sprite(new Texture(Gdx.files.internal
                ("beach-ocean-sea-bg/transparent-png/full_background.png")));
        currentMap = mapSprite;
        //viewport.get
        // Position the map to the bottom-left of the screen
        mapSprite.setPosition(-400, -220);
        // Resize the map so the height fits within the view, but keep original width
        mapSprite.setSize(mapSprite.getRegionWidth(), viewport.getScreenHeight());
        // Repeat adjustments for map2
        mapSprite2.setPosition(mapSprite.getX() + mapSprite.getWidth(), mapSprite.getY());
        mapSprite2.setSize(mapSprite.getRegionWidth(), viewport.getScreenHeight());


        // Setup the TextureAtlas for the jkirby running frames
        atlas = new TextureAtlas(Gdx.files.internal("jkirby_atlas.atlas"));
        // Find all frames of jkirby running from the TextureAtlas, and store in an Array
        Array<TextureAtlas.AtlasRegion> jkirbyFrames = atlas.findRegions("jk-frame");

        // Create the animation using the frames from the TextureAtlas
        jkirbyAnimation = new Animation(0.025f, jkirbyFrames);
        jkirbyAnimation.setPlayMode(Animation.PlayMode.LOOP);
        // Create an AnimatedSprite, which contains the Animation and Sprite information
        //jkirbyAnimatedSprite = new AnimatedSprite(jkirbyAnimation);
        jkirbyAnimatedSprite = new AnimatedPlayer(jkirbyAnimation, moveSpeed);
        jkirbyAnimatedSprite.play();
        jkirbyAnimatedSprite.setPosition(currentMap.getX() + 2 * jkirbyAnimatedSprite.getWidth(),
                floorPos);

        // Add jkirbyAnimatedPlayer to the GestureListener's array of sprites

        shortTree = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png")));
        shortTree.setPosition(camera.viewportWidth, floorPos);
        shortTree.setSize(35, 40);
        tallTree = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-2.png")));
        tallTree.setSize(50, 80);
        //tallTree.setPosition(shortTree.getX() + ((float)Math.random()) * 20 + 500, floorPos);

        traps = new Array<>();
        //tallTree.setPosition(shortTree.getX() + ((float)Math.random()) * 20 + 500, floorPos);
        addTrap(shortTree);
        tallTree.setPosition(nextTrapSlot(), floorPos);
        //addTrap(tallTree);
        //addTrapSample(nextTrapSlotFrom(shortTree));
        //addTrap(new TrapSprite(shortTree.getTexture(), nextTrapSlotFrom(traps.get(traps.size -1)),
        //        (int)floorPos, 35, 50));

        // Setup the InputProcessors
        // Use a multiplexer to handle multiple InputProcessors for different events
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(new GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                // Process tapping the screen when the game is paused
                if (paused) {
                    // If the game is paused, then unpause
                    Gdx.app.log("AnimatorGameScreen", "tap from 2nd GestureDetector ");
                    paused = false;
                    // Resume the player's movement and animation
                    jkirbyAnimatedSprite.play();
                    //camera.lookAt(0, 0, 0);
                    //camera.lookAt();
                    // If the game is paused AND the player has lost, then reset everthing
                    if (jkirbyAnimatedSprite.isLost()) {
                        Gdx.app.log("AnimatorGameScreen", "tapping when player has lost to reset");
                        //camera.position.setZero();
                        //viewport.update(800, 480);
                        //batch.flush();
                        //game.setScreen(AnimatorTestGame.menu);
                        //reset();
                        return true;
                    }
                    // else: player has not lost yet, so continue their same run without resetting
                    return true;
                }
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {

            }
        }));
        multiplexer.addProcessor(new GestureDetector(new AnimatorGestureListener(jkirbyAnimatedSprite)));


        multiplexer.addProcessor(new InputAdapter() {

            @Override
            public boolean keyTyped(char character) {
                switch(character) {
                    case 'r':
                        // If you press 'r', reset everything
                        reset();
                        // Pause the game and AnimatedPlayer so you have to press the screen to start
                        paused = true;
                        jkirbyAnimatedSprite.pause();
                        return true;

                };
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                    // Handle the back button
                    Gdx.app.log("AnimatorGameScreen", "KeyDown: BACK pressed");
                    //AnimatorMenuScreen newMenu = new AnimatorMenuScreen(batch, game);
                    //game.setScreen(newMenu);
                    //game.setScreen(parent);
                    //((Game) Gdx.app.getApplicationListener()).setScreen(parent);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new AnimatorMenuScreen(game));
                    return true;
                }
                return false;
            }
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (paused) {
                    // If the game is paused, then unpause
                    Gdx.app.log("AnimatorGameScreen", "touchDown from multiplexer");
                    paused = false;
                    // Resume the player's movement and animation
                    jkirbyAnimatedSprite.play();
                    //camera.lookAt(0, 0, 0);
                    //camera.lookAt();
                    // If the game is paused AND the player has lost, then reset everthing
                    if (jkirbyAnimatedSprite.isLost()) {
                        reset();
                    }
                    // else: player has not lost yet, so continue their same run without resetting
                }
                return true;
            }

            @Override
            public boolean touchUp(int x, int y, int pointer, int button) {
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    private void reset() {
        Gdx.app.log("AnimatorGameScreen", "Resetting all data...");

        // Reset the camera to the starting position
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        //resize((int)w, (int)h);
        //camera.position.set(0, 0, 0);
        //camera = new OrthographicCamera(30, 30 * (h / w));
        //viewport = new FitViewport(800, 480, camera);
        //camera.update();

        //viewport.apply(true);
        camera.position.set(0,0,0);
        viewport = new FitViewport(800, 480, camera);
        camera.update();

        stage.setViewport(viewport);

        //batch.flush();
        // reset the camera location
        //camera.lookAt(0, 0, 0);
        //camera = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
        //viewport = new FitViewport(800, 480, camera);

        //viewport.update(800, 480);

        // Reset the maps
        mapSprite.setPosition(-440, -220);
        // Resize the map so the height fits within the view, but keep original width
        //mapSprite.setSize(mapSprite.getRegionWidth(), viewport.getScreenHeight());
        // Repeat adjustments for map2
        mapSprite2.setPosition(mapSprite.getX() + mapSprite.getWidth(), mapSprite.getY());
        //mapSprite2.setSize(mapSprite.getRegionWidth(), viewport.getScreenHeight());
        displayMap1 = true;
        currentMap = mapSprite;

        // Reset the player sprite and start moving
        jkirbyAnimatedSprite.play();
        //jkirbyAnimatedSprite.setPosition(-1 * viewport.getScreenWidth() / 2 + jkirbyAnimatedSprite.getWidth(),
        jkirbyAnimatedSprite.setPosition(currentMap.getX() + 2 * jkirbyAnimatedSprite.getWidth(),
                floorPos);
        jkirbyAnimatedSprite.setVelocity(moveSpeed, 0);
        jkirbyAnimatedSprite.setLost(false);
        distanceTraveled = 0;
        jkirbyAnimatedSprite.setLastDistance(0);

        // Reset the traps
        for(TrapSprite s: traps) {
            // Not sure if this is necessary, but just in case I suppose
            //s.getTexture().dispose();
            // INCORRECT: do not dispose of the textures because of using the same sprite.
            // If we created new sprites on reset, then disposing would be the correct action.
        }
        traps.clear();
        Gdx.app.log("AnimatorGameScreen", "traps size: " + traps.size);
        shortTree.setPosition(camera.viewportWidth, floorPos);
        shortTree.setSize(35, 40);
        tallTree.setSize(50, 80);
        //tallTree.setPosition(shortTree.getX() + ((float)Math.random()) * 20 + 500, floorPos);
        //tallTree.setPosition(nextTrapSlotFrom(shortTree), floorPos);
        addTrap(shortTree);
        tallTree.setPosition(nextTrapSlot(), floorPos);
        //addTrap(tallTree);
        //tallTree.setPosition(nextTrapSlot(), floorPos);
        //addTrap(tallTree);
        //addTrapSample(nextTrapSlotFrom(shortTree));
        // Use the addTrapSample instead
        //addTrapSample(nextTrapSlot());
        //addTrap(new TrapSprite(shortTree.getTexture(), nextTrapSlotFrom(traps.get(traps.size -1)),
         //       (int)floorPos, 35, 50));

    }

    @Override
    public void render(float delta) {
        handleInput();
        if(!paused) {
            // Game is not paused, so continue moving the screen/player
            // auto-move the background left, so the animation to the left [based on moveSpeed]
            if(jkirbyAnimatedSprite.isRunning() ) {
                camera.translate(
                        jkirbyAnimatedSprite.getVelocityX(), 0, 0);
                distanceTraveled += jkirbyAnimatedSprite.getVelocityX();
                jkirbyAnimatedSprite.updateVelocity(distanceTraveled);

                //backButton.setPosition(camera.position.x - backButton.getWidth() / 2,
                //        camera.viewportHeight / 2);
            }

            // Move the player animation with the camera [based on moveSpeed]
            jkirbyAnimatedSprite.moveUpdate();
            //jkirbyAnimatedSprite.setPosition(jkirbyAnimatedSprite.getX() + moveSpeed,
            //      jkirbyAnimatedSprite.getY());
            //jkirbyAnimatedSprite.update();
            //jkirbyAnimatedSprite.setX(jkirbyAnimatedSprite.getX() + moveSpeed);
            // Increment distanceTraveled based on moveSpeed


        } else {
            // Game is paused
        }

        // Determine which map is currently displayed, switch currentMap to that map,
        // and then draw the old map after the other new currentMap
        if (displayMap1) {
            // check if switching from mapSprite to mapSprite2
            // plus (sprite.width * 3) so map1 isn't visible when it disappears
            if (jkirbyAnimatedSprite.getX() >= mapSprite2.getX() +
                    jkirbyAnimatedSprite.getWidth() * 3) {
                //Once the player has switched to map2, move map1 to after map2
                mapSprite.setPosition(mapSprite2.getX() + mapSprite2.getWidth(),
                        mapSprite2.getY());
                // switch the "displayFirstMap" off
                displayMap1 = false;
                currentMap = mapSprite2;
            }
        } else {
            // check if switching from map2 to map1
            // plus (sprite.width * 3) so map1 isn't visible when it disappears
            if (jkirbyAnimatedSprite.getX() >= mapSprite.getX() +
                    jkirbyAnimatedSprite.getWidth() * 3) {
                //Once the player has switched to map2, move map1 to after map2
                mapSprite2.setPosition(mapSprite.getX() + mapSprite.getWidth(),
                        mapSprite.getY());
                // switch the "displayFirstMap" on
                displayMap1 = true;
                currentMap = mapSprite;
            }
        }

        // Based on how far the player goes and how close they are to the furthest trap,
        // add a new trap

        int nextTrapPos = nextTrapSlot();

        /*
            This would infinitely spawn traps.
            Value of nextTrapPos would continue to increase so nextTrapPos would almost always
            be greater than distanceTraveled.
        if(distanceTraveled < nextTrapPos / 2) {
            addRandomTrapAtPos(nextTrapPos);
        }
        */

        // If the difference between nextTrapPos and distanceTraveled is LESS
        // than the width of the camera viewport, spawn a new trap
        if(nextTrapPos - distanceTraveled < camera.viewportWidth * 2f) {

            //TrapSprite testTrap = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png")));
            //testTrap.setPosition(camera.viewportWidth * 3, floorPos);
            //testTrap.setSize(35, 40);
            //addTrap(testTrap);
            //addRandomTrapAtPos(nextTrapPos);
            addTrapSample(nextTrapPos);
        }

        // For every single trap still in play on the currentMap,
        // check if the player has collided with the trap.
        // If they collide, pause the game and player, and the player has now lost.
        Rectangle jkirbyRectangle = jkirbyAnimatedSprite.getBoundingRectangle();
        for (TrapSprite s : traps) {
            //s.draw(batch);
            if(!paused) {
                // if the trap is in the current map, check for collision
                if (currentMap.getBoundingRectangle().contains(s.getX(), s.getY())) {
                    //Gdx.app.log("AnimatorGameScreen", "current map contains trap");
                    //if (jkirbyRectangle.contains(s.getX(), s.getY())) {
                    if (jkirbyRectangle.overlaps(s.getBoundingRectangle())) {
                        Gdx.app.log("AnimatorGameScreen", "player collided with " + s.getType());
                        jkirbyAnimatedSprite.pause();
                        jkirbyAnimatedSprite.setJumping(false);
                        jkirbyAnimatedSprite.setLost(true);
                        jkirbyAnimatedSprite.setVelocity(0, 0);
                        backButton.setPosition(stage.getViewport().getScreenWidth() / 2,
                                stage.getViewport().getScreenHeight() / 2);
                        //backButton.setPosition(camera.position.x - backButton.getWidth() / 2,
                        //        camera.viewportHeight / 2 );
                        //backButton.setPosition(viewport.getScreenX(), viewport.getScreenY());
                        paused = true;
                        //reset();
                    }
                }
            }
        }

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        /*
         Begin SpriteBatch rendering
          */
        batch.begin();
        mapSprite.draw(batch);
        mapSprite2.draw(batch);
        // Display the distance traveled so far
        // Use glyphlayout so it is easier to find the middle of the displayed text
        glyphLayout.setText(font, "" + (int) distanceTraveled);
        // Draw the distance at the top-middle of the screen
        font.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                camera.viewportHeight / 2);
        // Draw the number of TrapSprites in the traps Array
        glyphLayout.setText(smallFont, traps.size + " TrapSprites");
        smallFont.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                0);
        glyphLayout.setText(smallFont, "Velocity-X: " + jkirbyAnimatedSprite.getVelocityX());
        smallFont.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                glyphLayout.height);


        // Display some text when the game is paused
        if(paused) {
            // paused, so don't move camera or player but draw some text
            glyphLayout.setText(smallFont, "Press the screen to continue!");
            smallFont.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                    camera.viewportHeight / 4 - glyphLayout.height * 2);
            switch(Gdx.app.getType()) {
                case Android:
                    glyphLayout.setText(smallFont, "Press the back button for the menu!");
                    break;
                case Desktop:
                    glyphLayout.setText(smallFont, "Press 'Escape' for the menu!");
                    break;
                default:
                    glyphLayout.setText(smallFont, "Press the back button or 'Escape' for the menu!");
            }

            smallFont.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                    camera.viewportHeight / 4 - glyphLayout.height * 4);
            // If the player has lost, then display the losing text
            if(jkirbyAnimatedSprite.isLost()) {
                font.setColor(Color.FIREBRICK);
                glyphLayout.setText(font, "Such disappoint. Much fail. Wow.");
                font.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                        camera.viewportHeight / 2 - glyphLayout.height * 4);
                font.setColor(Color.BLACK);
            }

        }


        // Render all of the traps
        for(int i = 0; i < traps.size; i++) {
            traps.get(i).draw(batch);
            glyphLayout.setText(smallFont, "(" + traps.get(i).getX() + "," + traps.get(i).getY() + ")");
            smallFont.draw(batch, glyphLayout, traps.get(i).getX() - glyphLayout.width/2 + traps.get(i).getWidth() / 2,
                    traps.get(i).getY() - glyphLayout.height);
        }
        /*
        for(TrapSprite trap: traps) {
            //Gdx.app.log("AnimatorGameScreen", "Drawing trap at " + trap.getX() + ", " + trap.getY());
            trap.draw(batch);
            glyphLayout.setText(smallFont, "(" + trap.getX() + "," + trap.getY() + ")");
            smallFont.draw(batch, glyphLayout, trap.getX() - glyphLayout.width/2 + trap.getWidth() / 2,
                    trap.getY() - glyphLayout.height);
        }
        */
        //shortTree.draw(batch);
        //tallTree.draw(batch);

        // Draw the current frame of the AnimatedPlayer sprite
        jkirbyAnimatedSprite.draw(batch);

        /*
         End SpriteBatch rendering
          */
        batch.end();

        if(paused) {
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }
    }

    private void handleInput() {
    }

    private void addRandomTrapAtPos(int trapPos) {
        int randomValue = (int) Math.random() ;
        Gdx.app.log("AnimatorGameScreen", "addRandomTrap: randomValue = " + randomValue);
        TrapSprite newTrap;
        switch(randomValue) {
            case 0:
                Gdx.app.log("AnimatorGameScreen", "addRandomTrap: tree-1 at " + trapPos);
                newTrap = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png")),
                        trapPos, (int)floorPos, 35, 40);
                newTrap.setPosition(trapPos, floorPos);
                newTrap.setSize(35, 40);
                addTrap(newTrap);

                // Previously, this line caused traps to spawn at (0,0) instead of (trapPos, floorPos)
                // because the constructor for Sprite does not actually set the position.
                // In TrapSprite extension of Sprite, method setPosition(x,y) is now called to fix this.
                //addTrap(new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png")),
                //        trapPos, (int)floorPos, 35, 40));
                /*addTrap(new TrapSprite(TrapSprite.TrapType.SHORT,
                        trapPos, (int)floorPos, 35, 40));*/
                break;
            case 1:
                Gdx.app.log("AnimatorGameScreen", "addRandomTrap: tree-2 at " + trapPos);
                newTrap = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-2.png")),
                        trapPos, (int)floorPos, 50, 80);
                newTrap.setPosition(trapPos, floorPos);
                newTrap.setSize(50, 80);
                addTrap(newTrap);
                //addTrap(new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-2.png")),
                //        trapPos, (int)floorPos, 50, 80));
                //addTrap(new TrapSprite(TrapSprite.TrapType.TALL,
                //        trapPos, (int)floorPos, 50, 80));
                break;
        };
    }

    public void addTrapSample(int trapPos) {
        int randomValue = ((int)(Math.random() * 10));
        Gdx.app.log("AnimatorGameScreen", "addTrapSample: randomValue = " + randomValue +
        ";  trapPos = " + trapPos);
        if(randomValue < 6) {
            // 60% chance to get a small tree
            TrapSprite testTrap = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png")));
            testTrap.setPosition(trapPos, floorPos);
            testTrap.setSize(35, 40);
            addTrap(testTrap);
        } else {
            // 40% chance to get a tall tree
            TrapSprite testTrap = new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-2.png")));
            testTrap.setPosition(trapPos, floorPos);
            testTrap.setSize(50, 80);
            addTrap(testTrap);
        }

    }

    private void addTrap(TrapSprite trap) {
        trap.dispose = false;
        Gdx.app.log("AnimatorGameScreen", "Adding " + trap.getType() + " at (" + trap.getX() +
                ", " + trap.getY() + ")");
        traps.add(trap);
    }

    // Returns the next valid position for a trap based on the last trap in the Array of traps
    private int nextTrapSlot() {
        int slotFromLastTrap = nextTrapSlotFrom(traps.get(traps.size - 1));
        //Gdx.app.log("AnimatorGameScreen", "nextTrapSlot() = " + slotFromLastTrap);
        //return nextTrapSlotFrom(traps.get(traps.size - 1));
        return slotFromLastTrap;
    }

    // Returns the next valid position for a trap based on the given Trap's location
    private int nextTrapSlotFrom(TrapSprite trap) {
        float randomDist = ((float)Math.random()) * (5000 / jkirbyAnimatedSprite.getVelocityX());
        float randomRange= ((int)(Math.random() * 20));
        float minDist = 400;
        float increment = minDist + randomRange * (jkirbyAnimatedSprite.getVelocityX() * 50);
        //int nextSlot = (int)(trap.getX() + trap.getWidth() + 400 + randomDist);
        int nextSlot = (int)(trap.getX() + trap.getWidth() + increment);
        //Gdx.app.log("AnimatorGameScreen", "nextTrapSlotFrom() = " + nextSlot);
        return nextSlot;
    }

    @Override
    public void show() {
        ((AnimatorTestGame)game).stopMenuMusic();
        ((AnimatorTestGame)game).playGameMusic();
    }

    @Override
    public void hide() {
        ((AnimatorTestGame)game).stopGameMusic();
    }

    @Override
    public void resize(int width, int height) {
        //camera.viewportWidth = width / 32f;
        //camera.viewportHeight = camera.viewportWidth * height/width;
        Gdx.app.log("AnimatorGameScreen", "Resizing to " + width + " x " + height);
        viewport.update(width, height);
        stage.setViewport(viewport);
        camera.update();
    }

    @Override
    public void pause() {
        paused = true;
        jkirbyAnimatedSprite.pause();
        ((AnimatorTestGame)game).pauseGameMusic();
    }

    @Override
    public void resume() {
        //paused = false;
        //jkirbyAnimatedSprite.play();
        ((AnimatorTestGame)game).playGameMusic();
    }

    @Override
    public void dispose() {
        mapSprite.getTexture().dispose();
        mapSprite2.getTexture().dispose();
        shortTree.getTexture().dispose();
        tallTree.getTexture().dispose();
        for(Sprite s: traps) {
            s.getTexture().dispose();
        }
        jkirbyAnimatedSprite.getTexture().dispose();
        font.dispose();
        generator.dispose();
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }
}
