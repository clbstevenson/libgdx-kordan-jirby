package com.exovum.test.animation;

import com.badlogic.gdx.Gdx;
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

    // The type of trap or Texture for this TrapSprite
    TrapType type;

    enum TrapType {
        SHORT, TALL;
        static Texture shortTexture = new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-1.png"));
        static Texture tallTexture = new Texture(Gdx.files.internal("flat-tree-game-ornaments/tree-2.png"));
        Texture getTexture() {
            switch(this) {
                case SHORT:
                    return shortTexture;
                case TALL:
                    return tallTexture;
                default:
                    return shortTexture;
            }
        }

        public String toString() {
            switch(this) {
                case SHORT:
                    return "Short trap";
                case TALL:
                    return "Tall trap";
                default:
                    return "Default short trap";
            }
        }
    }

    public TrapSprite (Texture texture) {
        super(texture);
        if(texture.equals(TrapType.tallTexture)) {
            type = TrapType.TALL;
        } else {
            type = TrapType.SHORT;
        }
    }

    public TrapSprite (Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
        setSize(srcWidth, srcHeight);
        if(texture.equals(TrapType.tallTexture)) {
            type = TrapType.TALL;
        } else {
            type = TrapType.SHORT;
        }
    }

    public TrapSprite (Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setTexture(texture);
        setPosition(srcX, srcY);
        setSize(srcWidth, srcHeight);
        if(texture.equals(TrapType.tallTexture)) {
            type = TrapType.TALL;
        } else {
            type = TrapType.SHORT;
        }
    }

    public TrapSprite (TrapType type, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(type.getTexture(), srcX, srcY, srcWidth, srcHeight);
        setTexture(type.getTexture());
        setPosition(srcX, srcY);
        setSize(srcWidth, srcHeight);
        this.type = type;
    }

    public TrapType getType() {
        return type;
    }

    public void setType(TrapType newType) {
        type = newType;
    }
}
