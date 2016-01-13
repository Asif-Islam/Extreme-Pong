package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Strategize implements ApplicationListener  {
	Texture balltexture;
	SpriteBatch batch;
	int velocity;
	float ballx;
	int bally;
	OrthographicCamera camera;
	@Override
	public void create() {
		batch = new SpriteBatch();
		balltexture = new Texture("pongpong.png");
		velocity = 200;
		ballx = 400;
		bally = 200;
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render() {
	    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    camera.update();
	    batch.setProjectionMatrix(camera.combined);
	    batch.begin();
		batch.draw(balltexture, ballx, bally);
		batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
		ballx -= velocity * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			ballx += velocity * Gdx.graphics.getDeltaTime();
		}
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dispose() {
	    batch.dispose();
		
	}


}
