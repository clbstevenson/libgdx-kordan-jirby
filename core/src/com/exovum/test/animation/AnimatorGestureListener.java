package com.exovum.test.animation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by exovu_000 on 10/15/2016.
 * Implementation of GestureListener to process inputs for gestures, such as longPress.
 */

public class AnimatorGestureListener implements GestureDetector.GestureListener {

    // Array of all sprites to track gesture handling for
    private Array<Sprite> sprites;
    private Sprite player;

    public AnimatorGestureListener() {
        sprites = new Array<Sprite>();
    }

    public AnimatorGestureListener(Sprite sprite) {
        sprites = new Array<Sprite>();
        player = sprite;
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.removeValue(sprite, false);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(player instanceof AnimatedPlayer) {
            // Process jumping for the AnimatedPlayer
            AnimatedPlayer animatedPlayer = ((AnimatedPlayer) player);
            animatedPlayer.tryJump(20);
        } else {
            // Otherwise, just set the position normally?
        }

        /*
        for(Sprite s: sprites) {
            if(s instanceof AnimatedPlayer) {
                // Process jumping for the AnimatedPlayer
                AnimatedPlayer animatedPlayer = ((AnimatedPlayer) s);
                animatedPlayer.tryJump(20);
            } else {
                // Otherwise, just set the position normally?
            }
        }
        */

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        //TODO: jump higher
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
}
