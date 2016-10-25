package com.exovum.test.animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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

    protected static AnimatorGameScreen game;
    protected static AnimatorMenuScreen menu;
    protected static CreditsScreen credits;
    protected static InstructionsScreen instructions;

    public Music menuMusic, gameMusic;

    private float rotationSpeed;

    public void create() {

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Carpe Diem.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Pixel Peeker Polka - slower.mp3"));
        menuMusic.setLooping(true);
        gameMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        gameMusic.setVolume(0.5f);
        //music.play();
        //music.setLooping(true);
        batch = new SpriteBatch();

        menu = new AnimatorMenuScreen(this);
        //game = new AnimatorGameScreen(this, menu);
        //credits = new CreditsScreen(batch, this);
        //instructions = new InstructionsScreen(batch, this);

        this.setScreen(menu);

    }

    @Override
    public void dispose() {
        menuMusic.dispose();
        gameMusic.dispose();
    }

    public Music getMenuMusic() {
        return menuMusic;
    }
    public Music getGameMusic() {
        return gameMusic;
    }

    public void playMenuMusic() {
        if(!menuMusic.isPlaying()) {
            menuMusic.play();

        }
    }
    public void stopMenuMusic() {
        menuMusic.stop();
    }

    public void pauseMenuMusic() {
        menuMusic.pause();
    }

    public void playGameMusic() {
        if(!gameMusic.isPlaying()) {
            gameMusic.play();
        }
    }
    public void stopGameMusic() {
        gameMusic.stop();
    }

    public void pauseGameMusic() {
        gameMusic.pause();
    }
}
