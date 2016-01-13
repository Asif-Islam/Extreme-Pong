	package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.game.Pong;

public class Ball extends Moveable {


final Pong game;
public int angle;
public boolean direcUp;
public boolean isA = true;
public boolean direcLeft;
//Variables used for specials or paddles
public boolean boosted = false;
public int magical = 0;
public int confused = 0;
public boolean real = true;
public int prevvelocity;
public boolean earthquake = false;
public long earthquaketime;
public int earthquakecounter;
public double velocity3;
public boolean duplicate = false;


//Variables used for powerups
public boolean passable = false;
public boolean rotatecamera = false;
public int rotatecounter = 0;
public int velocity2;


public boolean uShape = false;

public Ball(final Pong gam, float _x, float _y) {
	game = gam;
	texture = new Texture("ball.png");
	height = texture.getHeight();
	width = texture.getWidth();
	x = _x;
	y = _y;
	velocity = 400;
	velocity2 = 400;
	velocity3 = 1.6;
	prevvelocity = velocity;
	direcUp = true;
	direcLeft = false;

}

public void render() {
	//Draws the ball
	game.batch.begin();
	game.batch.draw(texture, x, y);
	game.batch.end();

//While no confusion powerup is active and while it's not able to pass the ceiling/floor
//Then when the ball goes out of bounds, bake it rebound with a angle of 90 - angle
if (!(confused == 2)) {
	if (!(passable)) {
	if (y < 0) {
		direcUp = true;
		angle =  90 - angle;
		velocity2 = MathUtils.random(250,500); //VELOCITY 2 used for parabolic motion
	}
	
	if (y > Gdx.graphics.getHeight() - texture.getHeight()) {
		direcUp = false;
		angle = 90 - angle;
		velocity2 = MathUtils.random(700,900); // VELOCITY 2 used for parabolic motion
	}
	} else {
		if (y < 0) {
			y = Gdx.graphics.getHeight() - texture.getHeight();
			passable = false;
		}
		
		if (y > Gdx.graphics.getHeight() - texture.getHeight()) {
			y = 0;
			passable = false;
		}
	}
} else {
	if (y > Gdx.graphics.getHeight() - texture.getHeight()) {
		direcUp = false;
		if (velocity3 > 1.1) {
		velocity *=velocity3;
		velocity3 -=0.08;
		}
		passable = false;
	}
	if (y < 0) {
		direcUp = true;
		if (velocity3 > 1.1) {
		velocity *=velocity3;
		velocity3 -=0.065;
		}
		passable = false;
	}
}
	
	
	
	//Magical States, state 1 = Normal Magical, state 2 = Special Magical
	int teleport;
	switch (magical) {
	
	case 1: //Number generator chancee that the ball will randomly teleport ONCE
		 teleport = MathUtils.random(0, 38);
			if (x > 105 && x < 695 ) {
		if (teleport == 0) {
				y = MathUtils.random(0, 460);
				magical = 0;
			}
			

		}

		break;
	case 2:  //Number generator chancee that the ball will randomly teleport infinitely
		 teleport = MathUtils.random(0,9);
	if (x > 105 && x < 695 ) {
		if (teleport == 0 ){
			velocity *=1.3;
			y = MathUtils.random(0,460);
			x = MathUtils.random(x - 100, x + 100);
		}
	}
		break;
		
	}
	
	int change;
	switch (confused) { //Number generator chancee that the ball will randomly angle ONCE
	case 1:
		 change = MathUtils.random(0,70);
		if (change == 0 ) {
			if (x > 150 && x < 650 ) {
		direcUp = !direcUp;
		angle = MathUtils.random(15, 75);
		velocity *=1.2;
		confused = 0;
			}
		}
		break;
	case 2: //Very small angle at every bounce (Paddle special)
		angle = MathUtils.random(75,85);
		change = MathUtils.random(0,20);
		if (change == 0 ) {
			passable = true;
		}
		 break;
	}
}

//Initiates the balls movement
public void startmove() {
	double xcomp = velocity * Math.cos(Math.toRadians(angle)); //Sets x-component of velocity
	double ycomp;
	if (uShape) { //If Ushape, set y-comp to velocity2 and have velocity2 decrease per second
	 ycomp = velocity2 * Math.sin(Math.toRadians(angle)); 
	 velocity2 -=10;
	} else{
	 ycomp = velocity * Math.sin(Math.toRadians(angle)); //Else sent y-component of regular velcity
	}
	if (direcLeft) {
		x -= (int)xcomp * Gdx.graphics.getDeltaTime();	
	} else {
		x += (int)xcomp * Gdx.graphics.getDeltaTime();
	}
	
	if (direcUp) {
	y += (int)ycomp * Gdx.graphics.getDeltaTime();
	} else {
		y -= (int)ycomp * Gdx.graphics.getDeltaTime();
	}
}

//Reset ball stats
public void reset() {
	velocity = 400;
	velocity2 = 400;
	velocity3 = 1.6;
	boosted = false;
	if (confused == 2) {
		passable = false;
	}
	confused = 0;
	magical = 0;
	earthquake = false;
	earthquakecounter = 0;
	rotatecamera = false;
	uShape = false;
}



}
