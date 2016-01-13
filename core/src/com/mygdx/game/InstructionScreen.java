package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Pong;


public class InstructionScreen implements Screen {
	Texture instexture;
	Texture[] boards;
	final Pong game;
	int position = 0;
	boolean usingAI;
	
	public InstructionScreen(final Pong gam, boolean _usingAI) {
	game = gam;
	usingAI = _usingAI;
	boards = new Texture[6];
	instexture = new Texture("Instructionss.jpg");
	boards[0] = new Texture("PaddleDescription.png");
	boards[1] = new Texture("SpeedPaddleDescription.png");
	boards[2] = new Texture("PowerPaddleDescription.png");
	boards[3] = new Texture("MagicPaddleDescription.png");
	boards[4] = new Texture("ConfusionPaddleDescription.png");
	boards[5] = new Texture("EarthPaddleDescription.png");
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
    game.batch.begin();
	game.batch.draw(instexture,0,0);
	game.batch.draw(boards[position], 8, 136);
	game.batch.end();
	
	if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
		if (position < 5) {
			position++;
		} else {
			position = 0;
		}
	}
	
	if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
		game.setScreen(new GameScreen(game, usingAI));
	}
	
	if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
		game.setScreen(new MainScreen(game));
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
