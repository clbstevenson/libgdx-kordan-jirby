package com.exovum.test.collisions.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.exovum.test.animation.AnimatorTest;
import com.exovum.test.animation.AnimatorTestGame;
import com.exovum.test.collisions.CollisionsTest;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(800, 480);
        }

        @Override
        public ApplicationListener getApplicationListener () {
            return new AnimatorTestGame();
                //return new CollisionsTest();
        }

    @Override
    public ApplicationListener createApplicationListener() {
        return new AnimatorTestGame();
    }
}