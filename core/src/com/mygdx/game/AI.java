package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import objects.Ball;
import paddles.Paddle;

public class AI extends Player {

int SPprob, PUprob;
int SPtimecounter, PUtimecounter;
long lastSPtime, lastPUtime;


public AI(Pong gam) {
	super(gam);
	lastSPtime = TimeUtils.nanoTime();
	lastPUtime = TimeUtils.nanoTime();
	PUtimecounter = 0;
	SPtimecounter = 0;
}

//Initiates the AI 
public void initiateAI(Player player, Player opponent,  Paddle paddle, Ball ball) {
	
	
//Makes the paddle follow the ball	
	if (paddle.y < 480 - paddle.height) {
	if (ball.y > paddle.centre + paddle.y) {
			if (!(paddle.controlsflipped)) {
		paddle.y += paddle.velocity * Gdx.graphics.getDeltaTime();
			} else {
		if (paddle.y > 0) paddle.y -= paddle.velocity * Gdx.graphics.getDeltaTime();	
			}
		}
	}
	

	if (paddle.y > 0){
	if (ball.y < paddle.centre + paddle.y) {
			if (!(paddle.controlsflipped)) {
		paddle.y -= paddle.velocity * Gdx.graphics.getDeltaTime() ;
			} else {
		if (paddle.y < 480 - paddle.height) paddle.y += paddle.velocity * Gdx.graphics.getDeltaTime() ;	
			}

	}
}
	
	//React to the Earthquake effect
	if (ball.earthquake && ball.isA == true) {
		paddle.controlsflipped = true;
		checkconfusion(3);
	}
	


	//If camera rotated, make the AI confused for 1 second
	if (ball.rotatecamera) {
		paddle.controlsflipped = true;
		checkconfusion(1);
	}
	
	//When a control flip is used AGAINST The AI, have it take one second to react to the shift 	

	
	//Determine if the AI wants to use it's special power
	if (power == 10) {
		
		if (TimeUtils.nanoTime() - lastSPtime > 1000000000) {
			SPtimecounter++;
		if (SPtimecounter == 3) {
			SPprob = MathUtils.random(0, 2);
			SPtimecounter =0;
		}
		lastSPtime = TimeUtils.nanoTime();
		}
		
		if (SPprob == 2) {
		paddle.specialactive = true;
		SPprob = 0;
		}
	}
	
	
	//Code section to determine when to activate various powerups
	if (player.playerPUs.size > 0) {
		numbergenerator();
	switch (player.playerPUs.first().type) {
	case 1:
			numbergenerator();
			if (PUprob == 1) {
				if (!(ball.isA)) {
				if (ball.x - opponent.paddle.x < 400 && ball.x - opponent.paddle.x > -400 ) {
				java.util.Iterator<Powerup> iter = player.playerPUs.iterator();
					Powerup powerup = iter.next();
					powerup.activate(player, opponent, ball);
					iter.remove();
					PUprob = 0;
				
				}
				}
			}
		break;
		
	case 2:
		numbergenerator();
		if (PUprob == 1) {
			if (!(ball.isA)) {
			if ((ball.y < 100 && !ball.direcUp) || (ball.y < 380 && ball.direcUp) ) {
				if (ball.x - opponent.paddle.x < 400 && ball.x - opponent.paddle.x > -400 ) {
				java.util.Iterator<Powerup> iter = player.playerPUs.iterator();
				Powerup powerup = iter.next();
				powerup.activate(player, opponent, ball);
				iter.remove();
				PUprob = 0;
				}
			}
			}
		}
		break;
	case 3:
	case 5:
		numbergenerator();
		if (PUprob == 1) {
			if (!(ball.isA)) {
				java.util.Iterator<Powerup> iter = player.playerPUs.iterator();
				Powerup powerup = iter.next();
				powerup.activate(player, opponent, ball);
				iter.remove();
				PUprob = 0;
			}
		}
		break;
	case 4:
		java.util.Iterator<Powerup> iter = player.playerPUs.iterator();
		Powerup powerup = iter.next();
		powerup.activate(player, opponent, ball);
		iter.remove();
		break;
	}
	}
	//numbergenerator(lastPUtime, PUtimecounter, 3, 2, PUprob);


}
//Sets the confusion timing
public void checkconfusion(int time) {
	if (paddle.controlsflipped) {
		if (paddle.flipcounter == time) {
			paddle.controlsflipped = false;
		}
	}
}
public void numbergenerator() {
	if (TimeUtils.nanoTime() - lastPUtime > 1000000000) {
		
		PUtimecounter = PUtimecounter + 1;
	if (PUtimecounter == 3) {
		PUprob = MathUtils.random(0,1);
		PUtimecounter =0;

	}
	lastPUtime = TimeUtils.nanoTime();
	}

}
	
	
}
	

