package powerups;

import objects.Ball;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Player;
import com.mygdx.game.Pong;
import com.mygdx.game.Powerup;

public class PassPU extends Powerup {

	public PassPU(Pong gam, int _x, int _y, int _velocity, int _type) {
		super(gam, _x, _y, _velocity, _type);
		
		texture = new Texture("PassPU.png");
	}
	

}
