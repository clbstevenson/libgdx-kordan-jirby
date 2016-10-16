package com.exovum.test.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by exovu_000 on 10/15/2016.
 * TrapSprite contains information for any trap / obstacle objects to be rendered on the map.
 */

public class TrapSprite extends Sprite {

    // This flag is used to know when to dispose of this sprite and its textures.
    boolean dispose;

    public TrapSprite (Texture texture) {
        super(texture);
    }

    public TrapSprite (Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
    }

    public TrapSprite (Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

}
