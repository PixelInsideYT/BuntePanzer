package tankAttackers;

import org.joml.Vector2f;

import bus.MessageBus;
import collision.CollisionShape;
import entities.Entity;
import tanks.AiTank;
import tanks.Tank;
import texture.Texture;

public abstract class TankAttack extends Entity {

	public static final String TYPE_NAME = "TankAttack";
	public boolean enabled = true;
	private Tank homeTank;

	public TankAttack(Tank shooter, Vector2f position, CollisionShape collisionShape, Texture texture,MessageBus bus) {
		super(position, TYPE_NAME, collisionShape, texture, bus);
		this.homeTank = shooter;
	}

	public void onCollision(CollisionShape collidedWith, Vector2f mtv, String flag) {
		super.onCollision(collidedWith, mtv, flag);
		if (flag.equals("KILLER") && homeTank != null && homeTank instanceof AiTank) {
			if (mtv != null) {
				AiTank t = (AiTank) homeTank;
			}
		}
	}

}
