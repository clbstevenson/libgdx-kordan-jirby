package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by exovu_000 on 10/15/2016.
 * Extension of AnimatedSprite.
 * This is used to process touch, gestures, jumps, and collisions for the player sprite.
 */

class AnimatedPlayer extends AnimatedSprite {

    // A flag to determine if the AnimatedPlayer is currently jumping
    // NOTE: I suppose this could be checked via (vel.x/vel.y > 0), but the boolean may be simpler
    private boolean jumping;

    // Once the player has jumped, store the initial jump position so it can be reset later
    private Vector2f startPos;
    // A vector containing AnimatedPlayer's x- and y-direction velocities
    private Vector2f velocity;
    // A vector containing AnimatedPlayer's x- and y-direction acceleration
    private Vector2f acceleration;

    AnimatedPlayer(Animation animation, float moveSpeed) {
        super(animation);
        initVelocity(moveSpeed);
    }

    public AnimatedPlayer(Animation animation, boolean keepSize, float moveSpeed) {
        super(animation, keepSize);
        initVelocity(moveSpeed);
    }

    private void initVelocity(float moveSpeed) {
        // Initial startPos is the current position of the sprite; update when tryJump()
        startPos = new Vector2f(getX(), getY());
        // Initial velocity is moveSpeed as x-direction and 0 y-direction
        velocity = new Vector2f(moveSpeed, 0);
        // Initial acceleration is 0 y-direction and -10 y-direction ('gravity')
        acceleration = new Vector2f(0, -2);
    }

    @Override
    public void update() {
        super.update();
        //setPosition(getX() + moveSpeed, getY());
        //moveUpdate();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        //moveUpdate(delta);
    }

    // Update player's position based on velocity
    void moveUpdate() {
        setPosition(getX() + velocity.x, getY());
        jumpUpdate();
    }

    // Update player's position based on velocity using delta as time-passed
    private void moveUpdate(float delta) {
        setX((getX() + getVelocityX()) * delta);
        jumpUpdate(delta);
    }

    private void jumpUpdate() {
        // If the player is jumping, update position based on velocity
        // Velocity is also updated based on acceleration
        if(jumping) {
            if(velocity.y > 0) {
                setY(getY() + velocity.y);
                updateVelocityY(acceleration.y);
            } else {
                if(getY() > startPos.y) {
                    setY(getY() + velocity.y);
                    updateVelocityY(acceleration.y);
                } else {
                    // once return to starting point, then stop jumping and reset to startPos.y
                    setY(startPos.y);
                    velocity.y = 0;
                    jumping = false;
                }
            }
        }
    }
    private void jumpUpdate(float delta) {
        // If the player is jumping, update position based on velocity
        // Velocity is also updated based on acceleration
        if(jumping) {
            if(getY() >= startPos.y) {
                setY((getY() + velocity.getY()) * delta);
                updateVelocityY(acceleration.getY() * delta);
            } else {
                // once return to starting point, then stop jumping and reset to startPos.y value
                setY(startPos.y);
                velocity.y = 0;
                jumping = false;
            }
        }
    }

    private void setStartPos(float x, float y) {
        startPos.set(x, y);
    }

    //Setters for velocity
    public void setVelocity(Vector2f newVel) {
        velocity.set(newVel);
    }
    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }
    public void setVelocityX(float x) {
        velocity.setX(x);
    }
    public void setVelocityY(float y) {
        velocity.setY(y);
    }
    public void updateVelocityX(float x) {
        velocity.setX(velocity.getX() + x);
    }
    public void updateVelocityY(float y) {
        velocity.setY(velocity.getY() + y);
    }

    // Getters for velocity
    public Vector2f getVelocity() {
        return velocity;
    }
    public float getVelocityX() {
        return velocity.getX();
    }
    public float getVelocityY() {
        return velocity.getY();
    }

    //Setters for acceleration
    public void setAcceleration(Vector2f newAcc) {
        acceleration.set(newAcc);
    }
    public void setAccleration(float x, float y) {
        acceleration.set(x, y);
    }
    public void setAccelerationX(float x) {
        acceleration.setX(x);
    }
    public void setAccelerationY(float y) {
        acceleration.setY(y);
    }

    // Getters for acceleration
    public Vector2f getAcceleration() {
        return acceleration;
    }
    public float getAccelerationX() {
        return acceleration.getX();
    }
    public float getAccelerationY() {
        return acceleration.getY();
    }

    // Try to jump using the given y-velocity
    void tryJump(float y) {
        // if not already jumping, set velocity to given value
        if(!jumping) {
            setStartPos(getX(), getY());
            setVelocityY(y);
            jumping = true;
        }
        // otherwise, do nothing
    }


}
