package com.exovum.test.collisions.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.exovum.test.animation.AnimatorTestGame;
import com.exovum.test.animation.OrthographicCameraExample;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        //SpriteGame: initial testing of Robot Penguin sprites
		//initialize(new SpriteGame(), config);

        //Animator: Jordan Kirby running simulator
        //initialize(new AnimatorTest(), config);
        //initialize(new AnimatorTestGame(), config);
        initialize(new OrthographicCameraExample(), config);
	}
}
