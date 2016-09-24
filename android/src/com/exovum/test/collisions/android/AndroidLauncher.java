package com.exovum.test.collisions.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.exovum.test.collisions.Animator;
import com.exovum.test.collisions.CollisionsTest;
import com.exovum.test.collisions.SpriteGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new SpriteGame(), config);
        initialize(new Animator(), config);
	}
}
