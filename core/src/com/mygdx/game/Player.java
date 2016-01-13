package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

import paddles.Paddle;

public class Player{
	final Pong game;
public Paddle paddle;
public int score = 0;
public int power = 0;
public Array<Powerup> playerPUs;

public Player(final Pong gam)  {
	game = gam;
	playerPUs = new Array<Powerup>();

}


}
