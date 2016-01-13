package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pong extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public boolean started;
	@Override
	public void create() {
		font = new BitmapFont();
		batch = new SpriteBatch();
        this.setScreen(new MainScreen(this));
		started = false;
		
	}
	
	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
	}

}
