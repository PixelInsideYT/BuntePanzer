package steamTanks.weapons;

import org.joml.Vector2f;

import engine.bus.MessageBus;
import engine.math.Maths;
import engine.sound.SoundManager;
import steamTanks.tankAttackers.Projectile;
import steamTanks.tankAttackers.TankAttack;
import steamTanks.tanks.Tank;

public class MachineGun extends Weapon {

	public MachineGun(MessageBus bus) {
		super(bus, "MachineGun", 2, 0.1f, 5);
	}

	@Override
	protected TankAttack shoot(Tank sender, MessageBus bus) {
		float shootingError = (float) Math.toRadians(10);
		float releaseAngle = (float) (Math.random() * 2f - 1f) * shootingError + sender.angle;
		TankAttack bullet = new Projectile(sender, new Vector2f(), Maths.createUnitVecFromAngle(releaseAngle).mul(4f),
				sender.texture.getCustomColor(), bus);
		bullet.hitbox.setCenter(this.getCenterOfSpawnPosition(sender.hitbox.getAbsoluteCenter(), releaseAngle));
		mapHandler.testColliderAgainstMap(bullet);
SoundManager.get().playSoundEffect("machineGunShot");
		return bullet;
	}

}
