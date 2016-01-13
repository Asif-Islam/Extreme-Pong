package com.mygdx.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import objects.Ball;
import objects.Moveable;

public class Powerup extends Moveable {
public int type;
public Texture texture; 

final Pong game;

public Powerup(final Pong gam, int _x, int _y, int _velocity, int _type) {
game = gam;
x = _x;
y = _y;
velocity = _velocity;
type = _type;
}

public void render() {
	width = texture.getWidth();
	height = texture.getHeight();
	game.batch.begin();
	game.batch.draw(texture,x,y);
	game.batch.end();
	
	
}

public void activate(Player playerAct, Player playerOpp, Ball ball) {


switch (type) {
case 1:
	playerOpp.paddle.controlsflipped = true;
	int subkey = playerOpp.paddle.upkey;
	playerOpp.paddle.upkey = playerOpp.paddle.downkey;
	playerOpp.paddle.downkey = subkey;
	playerOpp.paddle.flippedtimer = TimeUtils.nanoTime();
	break;

case 2:
	ball.passable = true;
	break;

case 3:
	ball.rotatecamera = true;
	break;

case 4:
	playerAct.paddle.paddletype = MathUtils.random(1,5);

	if (playerAct.paddle.isA) {
	playerAct.paddle.initiatepaddle(playerAct, 0, (int)playerAct.paddle.y, Keys.W, Keys.S, Keys.A, Keys.D, playerAct.paddle.paddletype);
	} else {
	playerAct.paddle.initiatepaddle(playerAct, 800 - 10, (int)playerAct.paddle.y, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, playerAct.paddle.paddletype);
	}
break;

case 5:
	ball.uShape = true;
	break;
}

}
}
