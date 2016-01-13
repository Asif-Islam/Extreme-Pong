package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PowerBar extends Rectangle {
Texture texture;
Pong game;
Texture[] states;
Player player;

public PowerBar(final Pong gam, Player _player, int _x, int _y) {
	game = gam;
	initiatestates();
	texture = states[0];
	player = _player;
	x = _x;
	y = _y;

}

public void render() {
	game.batch.begin();
	game.batch.draw(texture, x, y);
	game.batch.end();
	
	texture = states[player.power];
	
}


public void initiatestates() {
	states = new Texture[11];
	states[0] = new Texture("P0.png");
	states[1] = new Texture("P1.png");
	states[2] = new Texture("P2.png");
	states[3] = new Texture("P3.png");
	states[4] = new Texture("P4.png");
	states[5] = new Texture("P5.png");
	states[6] = new Texture("P6.png");
	states[7] = new Texture("P7.png");
	states[8] = new Texture("P8.png");
	states[9] = new Texture("P9.png");
	states[10] = new Texture("P10.png");
	
	
}


}
