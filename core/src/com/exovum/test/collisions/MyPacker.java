package com.exovum.test.collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by exovu_000 on 10/10/2016.
 */

public class MyPacker {
    public static void main (String[] args) throws Exception {
        String parentDir = "android/assets/";
        String inputDir = "jkirby_frames/";
        String outputDir = "";
        String atlasName = "jkirby_atlas";
        TexturePacker.process(parentDir + inputDir,
                parentDir + outputDir,
                atlasName);

    }
}
