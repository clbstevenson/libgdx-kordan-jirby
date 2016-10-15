package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
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

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
        if(player instanceof AnimatedPlayer) {
            // Process jumping for the AnimatedPlayer
            AnimatedPlayer animatedPlayer = ((AnimatedPlayer) player);
            // Convert the fling velocityY to an appropriate jump speed
            // The negation is there because fling from "up" is negative y-value , and
            // "down" is a positive y-value
            float convertedJumpSpeed = velocityY /  -500;
            // Only bother with swipe "ups", or positive y-velocities
            if(convertedJumpSpeed < 0)
                convertedJumpSpeed = 0;
            // do some integer conversion for removing non-regular values, and base jump value is 20
            convertedJumpSpeed = ((int) convertedJumpSpeed) * 2  + 20;
            Gdx.app.log("AnimatorGestureListener", "convertedJumpSpeed: " + convertedJumpSpeed);
            convertedJumpSpeed = Math.min(convertedJumpSpeed, 35);
            Gdx.app.log("AnimatorGestureListener", "min convertedJumpSpeed: " + convertedJumpSpeed);
            animatedPlayer.tryJump(convertedJumpSpeed);
        } else {
            // Otherwise, just set the position normally?
        }
        return true;
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
