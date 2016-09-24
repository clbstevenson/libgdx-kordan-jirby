package com.exovum.test.collisions;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by exovu_000 on 9/23/2016.
 */

public class SpriteGame extends ApplicationAdapter {

    private SpriteBatch batch;
    /* The Texture class decodes an image file and loads it into GPU memory.
        The image file should be placed in the "assets" folder.
        The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility
        and performance reasons.
     */
    private Texture textureRobot;
    /* The TextureRegion class describes a rectangle inside a texture and is useful
        for drawing only a portion of the texture.
     */
    private TextureRegion region;
    /* The Sprite class describes both a texture region,
        the geometry where it will be drawn, and the color it will be drawn.
     */
    private Sprite spriteRobot;

    public void create() {
        batch = new SpriteBatch();
        textureRobot = new Texture(Gdx.files.internal("robo-penguin-texture.png"));
        region = new TextureRegion(textureRobot, 0, 0, 128, 128);
        spriteRobot = new Sprite(textureRobot, 0, 0, 128, 128);
        spriteRobot.setPosition (10, 10);
        spriteRobot.setRotation(45);
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clears the screen
        batch.begin();
        // Drawing goes here
        ///batch.draw(textureRobot, 100, 100);
        //batch.draw(region, 0, 0, 0, 0, 200, 200, 1, 1, -45);
        spriteRobot.draw(batch);
        batch.end();
    }

}
