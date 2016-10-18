package com.exovum.test.animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by exovu_000 on 10/14/2016.
 */

public class AnimatorTestGame extends Game {

    SpriteBatch batch;

    private Sprite mapSprite;

    private AnimatorGameScreen game;
    private AnimatorMenuScreen menu;


    private float rotationSpeed;

    public void create() {

        batch = new SpriteBatch();

        game = new AnimatorGameScreen(batch);
        menu = new AnimatorMenuScreen(batch);

        this.setScreen(menu);

    }

}
