package com.exovum.test.collisions.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.exovum.test.collisions.Animator;
import com.exovum.test.collisions.CollisionsTest;

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
        new LwjglApplication(new Animator(), config);
	}
}
