package com.se339.pixel_hockey.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.se339.pixel_hockey.PixelHockeyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Pixel Hockey";
		config.width = 3200;
		config.height = 1800;
		config.fullscreen = false;

		new LwjglApplication(new PixelHockeyGame(), config);
	}
}
