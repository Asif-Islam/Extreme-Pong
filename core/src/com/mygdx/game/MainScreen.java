package com.mygdx.game;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MainScreen implements Screen {
	Texture mainscreentexture;
	Rectangle vsAI;
	Rectangle vsFriend;
	final Pong game;
	Vector2 userinput;
	
	MainScreen(final Pong gam) {
		mainscreentexture = new Texture("MainScreen.png");
		game = gam;
		userinput = new Vector2(0,0);
		vsAI = new Rectangle(396, 159, 347, 75);
		vsFriend = new Rectangle(396, 58,  347, 75);
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
	userinput.x = Gdx.input.getX();
	userinput.y = 480 -Gdx.input.getY();
	game.batch.begin();
	game.batch.draw(mainscreentexture, 0,0);
	game.batch.end();
	
	
	if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
		if (vsAI.contains(userinput)) {
			game.setScreen(new InstructionScreen(game, true));
		} else if (vsFriend.contains(userinput)) {
			game.setScreen(new InstructionScreen(game, false));
		}
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
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
