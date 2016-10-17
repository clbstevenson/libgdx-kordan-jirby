package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

/**
 * Created by exovu_000 on 10/14/2016.
 * This is the main Screen for displaying the jkirby running animation.
 * It uses a camera and viewport to render the sprites and scale them accordingly.
 * The running animation sprite [AnimatedSprite] stays on the same spot on the screen, but the
 * background will move behind it. Obstacles will appear and the player will need to jump high
 * enough to avoid the traps.
 */

public class AnimatorMainScreen implements Screen {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private FreeTypeFontParameter parameter;
    private FreeTypeFontGenerator generator;

    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TextureAtlas atlas;

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

    public AnimatorMainScreen(SpriteBatch batch) {
        this.batch = batch;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        // Set the floor to be 100 below the middle of the screen
        floorPos = -150;
        // Set the initial movement speed of camera/player [can be changed later]
        moveSpeed = 4;
        // Initialize the player's distance traveled to 0; accumulates based on moveSpeed
        distanceTraveled = 0;

        //font = new BitmapFont(Gdx.files.internal("fonts/boxy_bold_font.png"));
        glyphLayout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Boxy-Bold.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 26; // font size
        font = generator.generateFont(parameter); // font size of 12 pizels
        font.setColor(Color.BLACK);

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(30, 30 * (h / w));
        viewport = new FitViewport(800, 480, camera);
        camera.update();
        viewport.update(800, 480);
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
        addTrap(tallTree);
        //addTrap(new TrapSprite(shortTree.getTexture(), nextTrapSlotFrom(traps.get(traps.size -1)),
        //        (int)floorPos, 35, 50));

        // Setup the InputProcessors
        // Use a multiplexer to handle multiple InputProcessors for different events
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GestureDetector(new AnimatorGestureListener(jkirbyAnimatedSprite)));
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                if(paused) {
                    // If the game is paused, then unpause
                    Gdx.app.log("AnimatorMainScreen", "touchDown from multiplexer");
                    paused = false;
                    // Resume the player's movement and animation
                    jkirbyAnimatedSprite.play();
                    //camera.lookAt(0, 0, 0);
                    //camera.lookAt();
                    // If the game is paused AND the player has lost, then reset everthing
                    if(jkirbyAnimatedSprite.isLost()) {
                        reset();
                    }
                    // else: player has not lost yet, so continue their same run without resetting
                }
                return true;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void reset() {
        Gdx.app.log("AnimatorMainScreen", "Resetting all data...");

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
        //viewport.update(800, 480);

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

        // Reset the traps
        for(TrapSprite s: traps) {
            // Not sure if this is necessary, but just in case I suppose
            //s.getTexture().dispose();
            // INCORRECT: do not dispose of the textures because of using the same sprite.
            // If we created new sprites on reset, then disposing would be the correct action.
        }
        traps.clear();
        Gdx.app.log("AnimatorMainScreen", "traps size: " + traps.size);
        shortTree.setPosition(camera.viewportWidth, floorPos);
        shortTree.setSize(35, 40);
        tallTree.setSize(50, 80);
        //tallTree.setPosition(shortTree.getX() + ((float)Math.random()) * 20 + 500, floorPos);
        tallTree.setPosition(nextTrapSlotFrom(shortTree), floorPos);
        addTrap(shortTree);
        addTrap(tallTree);
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

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        // Begin SpriteBatch rendering
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
        glyphLayout.setText(font, traps.size + " TrapSprites");
        font.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                0);


        if(paused) {
            // paused, so don't move camera or player but draw some text
            glyphLayout.setText(font, "Press the screen to continue!");
            font.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                    camera.viewportHeight / 2 - glyphLayout.height * 6);
            // If the player has lost, then display the losing text
            if(jkirbyAnimatedSprite.isLost()) {
                glyphLayout.setText(font, "Such disappoint. Much fail. Wow.");
                font.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
                        camera.viewportHeight / 2 - glyphLayout.height * 4);
            }

        }

        //glyphLayout.setText(font, jkirbyAnimatedSprite.getVelocity().toString());
        //font.draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
        //        camera.viewportHeight / 2 - 50);
        //font.draw(batch, "Distance: " + distanceTraveled, camera.position.x,
        //        camera.viewportHeight / 2);

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
        //shortTree.draw(batch);
        //tallTree.draw(batch);
        jkirbyAnimatedSprite.draw(batch);

        // Based on how far the player goes and how close they are to the furthest trap,
        // add a new trap
        int nextTrapPos = nextTrapSlot();
        if(distanceTraveled < nextTrapPos / 2) {
            addRandomTrapAtPos(nextTrapPos);
        }

        Rectangle jkirbyRectangle = jkirbyAnimatedSprite.getBoundingRectangle();
        for (TrapSprite s : traps) {
            s.draw(batch);
            if(!paused) {
                // if the trap is in the current map, check for collision
                if (currentMap.getBoundingRectangle().contains(s.getX(), s.getY())) {
                    //Gdx.app.log("AnimatorMainScreen", "current map contains trap");
                    //if (jkirbyRectangle.contains(s.getX(), s.getY())) {
                    if (jkirbyRectangle.overlaps(s.getBoundingRectangle())) {
                        Gdx.app.log("AnimatorMainScreen", "player collided with trap");
                        jkirbyAnimatedSprite.pause();
                        jkirbyAnimatedSprite.setLost(true);
                        jkirbyAnimatedSprite.setVelocity(0, 0);
                        paused = true;
                    }
                }
            }
        }
        // End SpriteBatch rendering
        batch.end();
    }

    private void handleInput() {
    }

    private void addRandomTrapAtPos(int trapPos) {
        int randomValue = (int) Math.random() * 2;
        Gdx.app.log("AnimatorMainScreen", "addRanomTrap: randomValue = " + randomValue);
        switch(randomValue) {
            case 0:
                Gdx.app.log("AnimatorMainScreen", "addRandomTrap: tree-1 at " + trapPos);
                addTrap(new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png")),
                        trapPos, (int)floorPos, 35, 40));
                break;
            case 1:
                Gdx.app.log("AnimatorMainScreen", "addRandomTrap: tree-2 at " + trapPos);
                addTrap(new TrapSprite(new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-2.png")),
                        trapPos, (int)floorPos, 50, 80));
                break;
        }
    }

    private void addTrap(TrapSprite trap) {
        trap.dispose = false;
        traps.add(trap);
    }

    // Returns the next valid position for a trap based on the last trap in the Array of traps
    private int nextTrapSlot() {
        int slotFromLastTrap = nextTrapSlotFrom(traps.get(traps.size - 1));
        //Gdx.app.log("AnimatorMainScreen", "nextTrapSlot() = " + slotFromLastTrap);
        //return nextTrapSlotFrom(traps.get(traps.size - 1));
        return slotFromLastTrap;
    }

    // Returns the next valid position for a trap based on the given Trap's location
    private int nextTrapSlotFrom(TrapSprite trap) {
        float randomDist = ((float)Math.random()) * (2000 / jkirbyAnimatedSprite.getVelocityX());
        int nextSlot = (int)(trap.getX() + trap.getWidth() + 400 + randomDist);
        Gdx.app.log("AnimatorMainScreen", "nextTrapSlotFrom() = " + nextSlot);
        return nextSlot;
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        //camera.viewportWidth = width / 32f;
        //camera.viewportHeight = camera.viewportWidth * height/width;
        Gdx.app.log("AnimatorMainScreen", "Resizing to " + width + " x " + height);
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void pause() {
        paused = true;
        jkirbyAnimatedSprite.pause();
    }

    @Override
    public void resume() {
        paused = false;
        jkirbyAnimatedSprite.play();
    }

    @Override
    public void hide() {

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
        batch.dispose();
    }
}
