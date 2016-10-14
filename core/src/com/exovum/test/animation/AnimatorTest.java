package com.exovum.test.animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by exovu_000 on 9/23/2016.
 * Code is taken from libgdx wiki page for 2D Animation:
 *      https://github.com/libgdx/libgdx/wiki/2D-Animation
 */

public class AnimatorTest implements ApplicationListener, InputProcessor {

    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 5;

    private SpriteBatch spriteBatch;
    private BitmapFont font;

    private Animation walkAnimation;
    private Texture walkSheet;
    private TextureRegion[] walkFrames;

    private TextureAtlas atlas;
    private TextureRegion currentFrame;
    private Sprite jkirbySprite;
    private Animation jkirbyAnimation;
    AnimatedSprite jkirbyAnimatedSprite;

    private Viewport viewport;
    private Camera camera;

    private String message = "Touch to Start!";
    private int w, h;
    private float posX, posY, centerX, centerY;
    private float velX, velY;
    private float accX, accY;
    private float scale, scaledWidth, scaledHeight;
    private float originalFrameWidth, originalFrameHeight;

    private boolean jumping;
    private int touchDownTimer;

    float stateTime;


    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

    @Override
    public void create() {

        camera = new PerspectiveCamera();
        viewport = new FitViewport(800, 480, camera);

        // The stateTime is used to find the current Animation frame based time passed per update
        stateTime = 0f;

        walkSheet = new Texture(Gdx.files.internal("animation_sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);
        //walkAnimation = new Animation(1f, walkFrames);

        spriteBatch = new SpriteBatch();
        //font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"),false);
        font = new BitmapFont();
        font.setColor(Color.ORANGE);
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        // Setup touch listeners, handling up to 5 locations
        Gdx.input.setInputProcessor(this);
        for(int i = 0; i < 5; i++) {
            touches.put(i, new TouchInfo());
        }

        // Setup the TextureAtlas for the jkirby running frames
        atlas = new TextureAtlas(Gdx.files.internal("jkirby_atlas.atlas"));
        // Find all frames of jkirby running from the TextureAtlas, and store in an Array
        Array<AtlasRegion> jkirbyFrames = atlas.findRegions("jk-frame");

        // Create the jkirbySprite based on the first image in the jkirby_atlas
        //jkirbySprite = atlas.createSprite("jk-frame", 1);

        // Create the jkirbyAnimation using the array of frames in the TextureAtlas
        // frame duration: 0.15f [WIP], jkirbyFrames is array of frames to be animated
        jkirbyAnimation = new Animation(0.00025f, jkirbyFrames);
        //jkirbySprite = new Sprite(jkirbyAnimation.getKeyFrame(stateTime, true).getTexture());
        jkirbySprite = atlas.createSprite("jk-frame");
        jkirbyAnimatedSprite = new AnimatedSprite(jkirbyAnimation);
        jkirbyAnimatedSprite.play();

        /*
        The following code scales the size of the running sprite based on the screen width/height.
        It finds the proportions of the original texture [originalFrameWidth/Height].
        The scaledWidth/Height is the desired space on the screen for the sprite to use,
            in this case 1/4th width and 1/4th height.
        The frameWidth/Heights are scaled up or down from originalFrameWidth/Height
            so that at least one of the width/height values matches scaledWidth/Height.
        The remaining value, the width/height not set to match scaledWidth/Height, is then
            adjusted to keep the original frame's width:height ratio.
         */
        scaledWidth = Gdx.graphics.getWidth() / 4;
        scaledHeight = Gdx.graphics.getHeight() / 4;
        float frameWidth = jkirbySprite.getRegionWidth();
        float frameHeight = jkirbySprite.getRegionHeight();
        originalFrameWidth = frameWidth;
        originalFrameHeight = frameHeight;
        // Use the minimum value between screen width and screen height
        if(scaledWidth < scaledHeight) {
            frameWidth = scaledWidth;
            frameHeight = originalFrameHeight * frameWidth / originalFrameWidth;
            //frameHeight = scaledHeight / (scaledWidth / frameWidth);
        } else { // scaledHeight <= scaledWidth
            frameHeight = scaledHeight;
            frameWidth = originalFrameWidth * frameHeight / originalFrameHeight;
            //frameWidth = scaledWidth * (scaledHeight / frameHeight);
        }

        // Set the width/height of jkirbySprite to be proportional to screen size
        jkirbySprite.setSize(frameWidth, frameHeight);

        // Set x/y position to the middle of the screen, adjusting for size of the sprite
        posX = w/2 - frameWidth / 2;
        posY = h/2 - frameHeight / 2;
        centerX = posX;
        centerY = posY;
        // Initialize the sprite's velocity and general acceleration values; used while jumping
        velX = 0;
        velY = 0;
        accX = 0;
        accY = -2;

        // Set the touchDownTimer to 0; used to track how 'long' touchDown before touch is released
        // This can be used to increase jump height/velocity based on time touchdown is held
        touchDownTimer = 0;

        // set the starting position of the running sprite
        jkirbySprite.setPosition(posX, posY);


    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        walkSheet.dispose();
        // Note to self: do not dispose atlas in create method, even if it's a local variable
        // because the sprites using the atlas will not have a resource to draw the textures.
        atlas.dispose();
    }



    @Override
    public void render() {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Increase state time based on delta (change in) time between frames
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        // Get the next frame from the running animation, and store in TextureRegion
        // The 2nd parameter sets if the animation is looping
        currentFrame = jkirbyAnimation.getKeyFrame(stateTime, true);
        //jkirbySprite.setTexture(currentFrame.getTexture());
        // Update the sprite's texture based on current frame in the animation
        jkirbySprite.setTexture(currentFrame.getTexture());
        jkirbyAnimatedSprite.play();

        // begin SpriteBatch rendering
        spriteBatch.begin();

        /*
         * DEBUG MESSAGES
         * Draw any messages for debugging purposes here.
         */
        message = "";
        for(int i = 0; i < 5; i++) {
            if(touches.get(i).touched)
                message += "Finger: " + Integer.toString(i) + " touch at: " +
                        Float.toString(touches.get(i).touchX) + ", " +
                        Float.toString(touches.get(i).touchY) + "\n";
        }
        //TextBounds tb = font.getBounds(message);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, message);
        float x = w/2 - layout.width/2;
        float y = h/2 + layout.height/2;
        font.draw(spriteBatch, message, x, y);

        String scaleValue = "ScaledWidth: " + Float.toString(scaledWidth) +
                "   ScaledHeight:" + Float.toString(scaledHeight);
        font.draw(spriteBatch, scaleValue, 10, Gdx.graphics.getHeight());
        String frameValue = "FrameWidth: " + Float.toString(jkirbySprite.getWidth()) +
                "   FrameHeight:" + Float.toString(jkirbySprite.getHeight());
        font.draw(spriteBatch, frameValue, 10, Gdx.graphics.getHeight() - 40);
        String originalFrameValue= "OriginalWidth: " + Float.toString(walkFrames[0].getRegionWidth()) +
                "   OriginalHeight:" + Float.toString(walkFrames[0].getRegionHeight());
        font.draw(spriteBatch, originalFrameValue, 10, Gdx.graphics.getHeight() - 80);
        font.draw(spriteBatch, "Touch Down Timer: " + touchDownTimer, 10,
                Gdx.graphics.getHeight() - 120);
        /*
            END DEBUG MESSAGES
         */

        // If the player sprite is jumping, update position based on velocity.
        // Velocity is also updated based on the acceleration.
        if(jumping) {
            if(posY >= centerY) {
                posY += velY;
                velY += accY;
            } else {
                posY = centerY;
                jumping = false;
            }
        }

        // Draw the current frame from the player's animation
        //spriteBatch.draw(currentFrame, posX, posY); //, 64, 64); //, 256, 307.2f);
        //spriteBatch.draw(currentFrame, posX, posY, frameWidth, frameHeight);
        // Use the jkirbySprite instead of currentFrame textures to render the animation
        //jkirbySprite.draw(spriteBatch);
        jkirbyAnimatedSprite.draw(spriteBatch);
        //spriteBatch.draw(currentFrame, posX, posY, walkFrames[0].getRegionWidth(), walkFrames[0].getRegionHeight());
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    // Override the InputProcessor interface methods



    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5) {
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
            //if(touchDownTimer < 5)
             //   touchDownTimer ++;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5) {
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
            if(!jumping) {
                jumping = true;
                // Use touchDownTimer to determine velocity: longer touchDown is held, jump higher
                // Also, the frameHeight / 4 is intended to scale the jump based
                //      on the sprite size and thus screen size
                velY = jkirbySprite.getHeight() / 4 * touchDownTimer;
                // Reset the touchDownTimer to be used for next time
                touchDownTimer = 0;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //if(touchDownTimer < 6)
        //    touchDownTimer++;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
