package com.mygdx.game;


import objects.Ball;
import paddles.ConfusionPaddle;
import paddles.EarthPaddle;
import paddles.MagicPaddle;
import paddles.Paddle;
import paddles.PowerPaddle;
import paddles.SpeedPaddle;
import powerups.ControlPU;
import powerups.PaddleChangePU;
import powerups.PassPU;
import powerups.RotatePU;
import powerups.UShapePU;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
//Screen Class for the Pong Game
public class GameScreen implements Screen {
	
	final Pong game;
	Texture backgroundtexture;
	OrthographicCamera camera;
	public Ball ball;
	Player playerA, playerB;
	PowerBar powerbarA, powerbarB;
	Array<Powerup> powerups;
	Array<Ball> fakeballs;
	long lastpowerupspawn;
	int powerupcounter = 0;
	boolean usingAI = false;
	boolean camerastatic = false;
	AI ai;
	
	Timer poweruptimer;
	int powerupcount = 0;
	boolean poweruptimerstarted = false;
	
	
	public GameScreen(final Pong gam, boolean _usingAI) {
		//Initialize variables such as whether we're using an AI, the texture
		//camera, players, player paddles (set to the original paddle)
		//powerups
		usingAI = _usingAI;
		game = gam;
		backgroundtexture = new Texture("background2.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		playerA = new Player(game);
		playerB = new Player(game);
		
		if (usingAI) {
		ai = new AI(game);
		playerB = ai;
		}
		
		
		
		powerbarA = new PowerBar(game, playerA, 0,0);
		powerbarB = new PowerBar(game, playerB, 600, 0);
		playerA.paddle = new Paddle(game, Keys.W , Keys.S , Keys.A, Keys.D, 0, 240, 520, 0, true);
		playerB.paddle = new Paddle(game, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, 800-10, 240, 520, 0, false);
	
		playerA.paddle.isA = true;
		playerB.paddle.isA = false;
		ball = new Ball(game,playerA.paddle.x + 8, playerA.paddle.y + 42);
		fakeballs = new Array<Ball>();
		powerups = new Array<Powerup>();
		
		//InitiateMessages



		
		//Initiates the timers that would be used for in-game effects
		initiatetimers();
		
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
	Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    

	camera.update();
	game.batch.setProjectionMatrix(camera.combined);

    //Draw Scores
	game.batch.begin();
	game.batch.draw(backgroundtexture,0,0);
	game.font.draw(game.batch, Integer.toString(playerA.score), 50, 50);
	game.font.draw(game.batch, Integer.toString(playerB.score), 750, 50);
	
	if (playerA.score >= 20) {
		game.batch.draw(new Texture("PlayerAWin.png"),0,0);
	} else if (playerB.score >= 20) {
		game.batch.draw(new Texture("PlayerAWin.png"),0,0);
	}
	game.batch.end();

	//Render the powerbars, paddles
	powerbarA.render();
	powerbarB.render();
	playerA.paddle.render();
	playerB.paddle.render();
	for (Ball fball : fakeballs) {
		fball.render();
	}

	
//Render falling powerups and player-owned powerups
	for (Powerup powerup: powerups) {
		powerup.render();
	}
	
	for (Powerup powerup : playerA.playerPUs){
		powerup.render();
	}
	for (Powerup powerup : playerB.playerPUs) {
		powerup.render();
	}
	
	
	//Renders the balls
	ball.render();
	
	//Iterates through powerups and removes them if they are out of bounds
	java.util.Iterator<Powerup> iter = powerups.iterator();
	while(iter.hasNext()) {
	Powerup powerup = iter.next();
	powerup.y -= powerup.velocity*Gdx.graphics.getDeltaTime();
	
	if (powerup.y + 32 < 0) {
		iter.remove();
	}
	
	//Allows the user to store powerups while they have space
	if (powerup.overlaps(ball)) {
		if (ball.isA) {
			if (playerA.playerPUs.size < 5) {
			playerA.playerPUs.add(powerup);
			powerup.x = playerA.playerPUs.indexOf(powerup, true) * 32 + 205;
			powerup.y = 0;
			iter.remove();
			}
		} else {
			if (playerB.playerPUs.size < 5) {
				playerB.playerPUs.add(powerup);
				powerup.x = 800 - 205 - playerB.playerPUs.indexOf(powerup, true) * 32;
				powerup.y = 0;
				iter.remove();
			}
		}

	}
	
	//Update the powerups
	updatepowerups();

	try {
	if (!(game.started)) {
		iter.remove();
	}
	} catch (ArrayIndexOutOfBoundsException e) {
		
	}
	}
	

	
	//Check if the round has started
	if (!(game.started)) {
		//Initiate ball position based on who won the last point
		if (ball.isA) {
		ball.x = playerA.paddle.x + 8;
		ball.y = playerA.paddle.y + (playerA.paddle.height / 2) - ball.height / 2;
		} else {
		ball.x = playerB.paddle.x - ball.width;
		ball.y = playerB.paddle.y + (playerB.paddle.height / 2) - ball.height/2;
		}
		
		//Resets the camera if it was originally flipped from powerup
		if (camerastatic) {
			camera.rotate(180);
			camerastatic = false;
		}
		//Resets ball stats, player paddles and the camera position
		ball.reset();
		playerA.paddle.reset();
		playerB.paddle.reset();
		camera.position.x = 400;
		camera.position.y = 240;
		
//Starts the game
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			game.started = true;

			ball.angle = MathUtils.random(25,65);
		}
		
//Lets the user return to main screen when done
		if (playerA.score >= 20 || playerB.score >= 20) {
			if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				game.setScreen(new MainScreen(game));
			}
		}
	}

	
	//If the game has started then start moving the ball
	if (game.started) {
		ball.startmove();
		//Check if there is a collision
		checkcollisions();
		//Initiate the AI if playing with an AI
		if (usingAI) { 
		ai.initiateAI(playerB, playerA, playerB.paddle, ball);
		}
		
	}
	
//check if the balls go out of bounds
	if (ball.x > 800) {
		game.started = false;
		ball.direcLeft = false;
		ball.isA = true;
		playerA.score++;
		//restore the state of the Earth-Paddle if it were causing an earthquake
		earthquakererender();
	}
	if (ball.x < 0) {
		game.started = false;
		ball.direcLeft = true;
		ball.isA = false;
		playerB.score++;
		earthquakererender();
	}
	

//If an earthquake effect is active, call the code that rumbles the camera
	if (ball.earthquake) {
		earthquakerumble();
	}

//if the camera isn't already rotated, rotate the camera;
	if (!(camerastatic)) {
	if (ball.rotatecamera) {
		camera.rotate(180);
		camerastatic = true;
	}
	}
	
	if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
		playerA.paddle.initiatepaddle(playerA, 0, (int)playerA.paddle.y, Keys.W, Keys.S, Keys.A, Keys.D, 5);
		playerB.paddle.initiatepaddle(playerB, 800-10, (int)playerB.paddle.y, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, 0);
	}

	
//If the ball is to duplicate, then create five FAKEBALLS 
	if (ball.duplicate) {
		
		for (int x = 0; x < 5; x++) {
			Ball newball = new Ball(game, ball.x,ball.y);
			//randomize their direction
			if (x % 2 == 0) {
				newball.direcUp = false;
			} else {
				newball.direcUp = true;
			}
			newball.direcLeft = ball.direcLeft;
			newball.velocity = MathUtils.random(ball.velocity - 100, ball.velocity + 100);
			newball.angle = MathUtils.random(25,65);
			fakeballs.add(newball);
			
		}
		ball.duplicate = false;
		
	} 
	
//remove the fakeballs if they go beyond the range;
	if (fakeballs.size > 0) {
		java.util.Iterator<Ball> balliter = fakeballs.iterator();
		while(balliter.hasNext()) {
		Ball bal = balliter.next();
		bal.startmove();
		if (bal.x > 800 || bal.x < 0 || bal.overlaps(playerA.paddle) || ball.overlaps(playerB.paddle)) {
		 balliter.remove();
		 
		}
	

		
		}
	
	}


	
	
	
	}
	
	
	
	//Initiates gamer timers
	public void initiatetimers() {
	
		Timer.schedule(new Task() {
			@Override
			public void run() {

//Generates a powerup every 3 seconds
				powerupcount++;
				if (powerupcount == 3) {
					if (game.started) {
						spawnpowerup();
					}
					powerupcount =0;
				}
				
				//Causes an earthquake for 5 seconds
				if (ball.earthquake) {
					ball.earthquakecounter++;
					if (ball.earthquakecounter == 5) {
						camera.position.x = 400;
						camera.position.y = 240;
						ball.earthquakecounter = 0;
						ball.earthquake = false;
						earthquakererender();

					}
				}
				
				//Rotates the camera for a maximum of 4 seconds
				if (ball.rotatecamera) {
					ball.rotatecounter++;
					if (ball.rotatecounter == 4) {
						camera.rotate(180);
						ball.rotatecounter = 0;
						ball.rotatecamera = false;
						camerastatic = false;
					}
				}
			}
		},0	,1); //Execute Timer only once
	
	}
	
	//Randomly shakes the screen
	public void earthquakerumble() {
		int delta = MathUtils.random(-100,100);
		camera.position.x = 400 + delta;
		camera.position.y = 240 + delta;
	}
	
	//Resets the earthquake paddles to original height 
	//(This is because during the earthquake active the EarthPaddle filles it's entire
	//goal zone for the entire period of the earthquake)
	public void earthquakererender() {
		if (playerA.paddle.paddletype == 5) {
			playerA.paddle.height = 180;
		}
		
		if (playerB.paddle.paddletype == 5) {
			playerB.paddle.height = 180;
		}
	}
	
//Spawns a powerup randomly
	public void spawnpowerup() {
		int type = MathUtils.random(1,5);
		Powerup powerup;
		switch (type) {
		case 1:
			powerup = new ControlPU(game, MathUtils.random(0, 800-32), 480-32, MathUtils.random(50, 150), 1);
			powerups.add(powerup);
			break;
		case 2:
			powerup = new PassPU(game, MathUtils.random(0,800-32), 480-32, MathUtils.random(50,100), 2);
			powerups.add(powerup);
			break;
		case 3:
			powerup = new RotatePU(game, MathUtils.random(0,800 - 32), 480 - 32, MathUtils.random(50,100), 3);
			powerups.add(powerup);
			break;
		case 4:
			powerup = new PaddleChangePU(game, MathUtils.random(0,800 - 32), 480 - 32, MathUtils.random(10,50), 4);
			powerups.add(powerup);
			break;
		case 5:
			powerup = new UShapePU(game, MathUtils.random(0,800 - 32), 480 - 32, MathUtils.random(10,50), 5);
			powerups.add(powerup);
			break;
	}
	}
	
	//Checks the collisions with the ball at every render
	//Checks the specialactivation at every render
	//Checks the powerupactivation at every render
	public void checkcollisions() {
	playerA.paddle.collisionresetball(playerA, ball);
	playerA.paddle.specialsclicked(playerA, ball);
	playerB.paddle.collisionresetball(playerB, ball);
	playerB.paddle.specialsclicked(playerB, ball);
	playerA.paddle.powerupclicked(playerA, playerB, ball);
	playerB.paddle.powerupclicked(playerB, playerA, ball);
	}
	
//Rerender the powerups that are stored for each player
	public void updatepowerups() {
		for (Powerup powerup : playerA.playerPUs) {
			powerup.x = playerA.playerPUs.indexOf(powerup, true) * 32 + 205;
		}
		
		for (Powerup powerup : playerB.playerPUs) {
			powerup.x = 800 - 235 - playerB.playerPUs.indexOf(powerup, true) * 32;
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
