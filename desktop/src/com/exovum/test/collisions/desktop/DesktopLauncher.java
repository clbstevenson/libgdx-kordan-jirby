package com.exovum.test.collisions.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.exovum.test.animation.AnimatorTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "libgdx-rain";
        config.width = 800;
        config.height = 480;
		//new LwjglApplication(new CollisionsTest(), config);
        // NOTE: When first setting up project, set the Working Directory
        //      for Desktop Application to the android/assets/ folder via Run->Edit Config.
        //      This allows the Desktop Application to find the assets it needs.
        //new LwjglApplication(new Animator(), config);

        // Launch Animator: Jordan Kirby running simulator
        new LwjglApplication(new AnimatorTest(), config);

        // Launch SpriteGame: initial testing of Robot Penguin sprites
        //new LwjglApplication(new SpriteGame(), config);

        // Launch the GraphicsDemo
        //new LwjglApplication(new GraphicsDemo(), config);
	}
}
