package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by exovu_000 on 10/14/2016.
 */

public class AnimatorMainScreen implements Screen {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private OrthographicCamera camera;
    SpriteBatch batch;
    private TextureAtlas atlas;

    Animation jkirbyAnimation;
    AnimatedSprite jkirbyAnimatedSprite;

    private Sprite mapSprite;

    public AnimatorMainScreen(SpriteBatch batch) {
        this.batch = batch;

        mapSprite = new Sprite(new Texture(Gdx.files.internal("sc_map.png")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera(30, 30 * (h / w));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        // Setup the TextureAtlas for the jkirby running frames
        atlas = new TextureAtlas(Gdx.files.internal("jkirby_atlas.atlas"));
        // Find all frames of jkirby running from the TextureAtlas, and store in an Array
        Array<TextureAtlas.AtlasRegion> jkirbyFrames = atlas.findRegions("jk-frame");

        // Create the animation using the frames from the TextureAtlas
        jkirbyAnimation = new Animation(0.025f, jkirbyFrames);
        jkirbyAnimation.setPlayMode(Animation.PlayMode.LOOP);
        // Create an AnimatedSprite, which contains the Animation and Sprite information
        jkirbyAnimatedSprite = new AnimatedSprite(jkirbyAnimation);
        jkirbyAnimatedSprite.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        // Begin SpriteBatch rendering
        batch.begin();
        mapSprite.draw(batch);
        jkirbyAnimatedSprite.draw(batch);
        // End SpriteBatch rendering
        batch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 30f;
        camera.viewportHeight = 30f * height/width;
        camera.update();
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
        mapSprite.getTexture().dispose();
        jkirbyAnimatedSprite.getTexture().dispose();
        batch.dispose();
    }
}
