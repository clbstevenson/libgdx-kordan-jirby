package com.exovum.test.animation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by exovu_000 on 10/15/2016.
 * Implementation of GestureListener to process inputs for gestures, such as longPress.
 */

public class AnimatorGestureListener implements GestureDetector.GestureListener {

    private Sprite sprite;

    public AnimatorGestureListener(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

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
