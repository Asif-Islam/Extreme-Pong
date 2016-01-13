package paddles;

import objects.Ball;
import objects.Moveable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Player;
import com.mygdx.game.Pong;
import com.mygdx.game.Powerup;

public class Paddle extends Moveable {
final Pong game;
public int centre;
public int upkey;
public int downkey;
public int PUkey;
public int SPkey;
public boolean specialactive = false;
public boolean controlsflipped = false;
public boolean paddlechange = false;
public boolean isA;
public int paddletype;
public long flippedtimer;
public int flipcounter = 0;
public boolean showmessage = false;
public int messagetype;



//Constructor takes in the keys, its position, velocity and type of paddle, as well as whether its paddle A or paddle B
public Paddle(final Pong gam, int _upkey, int _downkey, int _PUkey, int _SPkey, int _x, int _y, int _velocity, int type, boolean _isA) {
	game = gam;
	texture = new Texture("paddle.png");
	height = texture.getHeight();
	width = texture.getWidth();
	centre = texture.getHeight() / 2;
	x = _x;
	y = _y;
	upkey = _upkey;
	downkey = _downkey;
	PUkey = _PUkey;
	SPkey = _SPkey;
	velocity = _velocity;
	paddletype = type;
	isA = _isA;
	
}

public void render() {
	//Draw Paddle
	game.batch.begin();
	game.batch.draw(texture, x, y);
	game.batch.end();
	
	//React to a powerup that moentarily flips the controls;
	if (controlsflipped) {
		if (TimeUtils.nanoTime() - flippedtimer > 500000000) {
			flipcounter++;
			flippedtimer = TimeUtils.nanoTime();
			if (flipcounter == 6) {
				switchkeys();
				controlsflipped = false;

			}
		}
	}
	
	//Movement going upwards 	while the paddle is below the max height
	if (y < Gdx.graphics.getHeight() - texture.getHeight()) 
	{
		if (Gdx.input.isKeyPressed(upkey)) 
		{
		 y += velocity * Gdx.graphics.getDeltaTime();
		}
	}
	
	//Movement going downwards while the paddle is above the min height
	if (y > 0) 
	{  
		if (Gdx.input.isKeyPressed(downkey)) 
		{
		 y -= velocity * Gdx.graphics.getDeltaTime();
		}
	} 
	

	
	}

//Reset the paddle
public void reset() {
	specialactive = false;
	flipcounter = 0;
	if (controlsflipped) {
		switchkeys();
	controlsflipped = false;
	}
	
}

//Reset the keys if the controls were flipped
public void switchkeys() {
	int subkey = upkey;
	upkey = downkey;
	downkey = subkey;
}

//Activate the powerup while powerups exist
public void powerupclicked(Player playerAct, Player playerOpp, Ball ball) {

		if (playerAct.playerPUs.size > 0) {
			
			java.util.Iterator<Powerup> iter = playerAct.playerPUs.iterator();
			if (Gdx.input.isKeyJustPressed(PUkey)) {
				Powerup powerup = iter.next();
				powerup.activate(playerAct, playerOpp, ball);
				showmessage = true;
				messagetype = powerup.type;
				iter.remove();
			}	
		}
	}

//Activate the special power while there are 10 points in the power bar
public void specialsclicked(Player player, Ball ball) {
	if (player.power == 10) {
		if (Gdx.input.isKeyPressed(SPkey)) {
			specialactive = true;
		}
	}
	
	
}
//Resets the ball to a regular moving state and reacts to the collision with the paddle
public void collisionresetball(Player player, Ball ball) {
	//Increment power by 1
	if (ball.overlaps(this)) {
		if (player.power < 10) {
			player.power++;
		}
		
	//Change the ownership of the ball
	if (ball.isA) {
		ball.isA = false;
	} else {
		ball.isA = true;
	}
		
		if (ball.direcLeft) {
			ball.x = this.width;
		} else {
			ball.x = 800 - this.width - ball.width;
		}
		
		//Change directions and angle of 90 - angle
			ball.direcLeft = !ball.direcLeft;
			ball.direcUp = !ball.direcUp;
			ball.angle = 90 - ball.angle;
			
			//Resets the balls values as if it were a regular ball
			if (ball.boosted) {
				ball.velocity = ball.prevvelocity;
				ball.boosted = false;
			} else {
			ball.prevvelocity = ball.velocity;
			}
			ball.magical = 0;
			
			if (ball.confused == 2) {
				ball.passable = false;
			}
			ball.confused = 0;	
			
			//Calls the collisioninteraction to see how the paddle wil uniquely
			//act against the ball
			collisioninteract(player, ball);
}
}

//How each individual paddle will uniquely act against the ball
public void collisioninteract(Player player, Ball ball) {
	switch (paddletype) {
	case 0: //Original paddle,
		ball.velocity *= 1.1;
		
		
		//Change paddles
		if (specialactive) {
			initiatepaddle(player, (int)x, (int)y, upkey, downkey, PUkey, SPkey, MathUtils.random(1,5));
			player.power = 0;
		}
		break;
	
	case 1: //Speed Paddle
		ball.velocity *=1.1;
		if (specialactive) { //Make fakeball copies
			ball.duplicate = true;
			player.power = 0;
			specialactive = false;

			
		}
		break;
	case 2: //Power Paddle
		ball.boosted = true; //boost the paddle's speed
		if (specialactive) {
			ball.velocity *=4.0; //Special: 4x speed
			player.power = 0;
			specialactive = false;
		} else {
		ball.velocity *= 1.8;
		}
		break;
	case 3: //Magical Paddle
		ball.velocity *=1.1;
		ball.magical = 1; //set the magical state to 1
		
		if (specialactive) { //Set magical state to 2
			ball.magical = 2;
			specialactive = false;
			player.power = 0;
		} 


		break;
	case 4: //Confusion Paddle
		
		ball.velocity *=1.1;
		ball.confused = 1; //Set confused state to 1
		
		if (specialactive) { 
			ball.confused = 2; //Set confused state to 2
			ball.boosted = true;
			specialactive = false;
			player.power = 0;
		}
		break;
	case 5: //Earth Paddle
		if (specialactive) {
			ball.velocity *=2; //Double speed
			ball.earthquake = true; //Earthquake the field
			specialactive = false;
			player.power = 0;
			y = 0;
			height = 480;	
		} else {
			ball.velocity *=1.1;
		}
		break;
		
	}
	}



//Initiates the paddles based on the prescribed paddletype
//(Polymorphism since the player.paddle is regular Paddle type but is set
//to various derived paddle classes
public void initiatepaddle(Player player, int xpos, int ypos, int _upkey, int _downkey, int _PUkey, int _SPkey, int type){
	Paddle mypaddle;
	boolean _isA;
	if (player.paddle.isA) {
	_isA = true;
	} else {
		_isA = false;
	}
	switch (type) {
	case 0:
		mypaddle = new Paddle(game, _upkey , _downkey , _PUkey, _SPkey, xpos, ypos, 520, 0, _isA);
		player.paddle = mypaddle;
		break;
	case 1:
		mypaddle = new SpeedPaddle(game, _upkey , _downkey , _PUkey, _SPkey, xpos, ypos, 720, 1, _isA);
		player.paddle = mypaddle;
		break;
	case 2:
		mypaddle = new PowerPaddle(game, _upkey , _downkey , _PUkey, _SPkey, xpos, ypos, 400, 2, _isA);
		player.paddle = mypaddle;
		break;
	case 3:
		mypaddle = new MagicPaddle(game, _upkey , _downkey , _PUkey, _SPkey, xpos, ypos, 440, 3, _isA);
		player.paddle = mypaddle;
		break;
	case 4:
		mypaddle = new ConfusionPaddle(game, _upkey , _downkey , _PUkey, _SPkey, xpos, ypos, 425, 4, _isA);
		player.paddle = mypaddle;
		break;
	case 5:
		mypaddle = new EarthPaddle(game, _upkey, _downkey, _PUkey, _SPkey, xpos, ypos, 325, 5, _isA);
		player.paddle = mypaddle;
		break;
	
	}
}


}
