package weapons;

import bus.MessageBus;
import map.MapHandler;
import sound.SoundManager;
import tankAttackers.AntiTankMine;
import tankAttackers.TankAttack;
import tanks.Tank;

public class MineSetter extends Weapon {

	public MineSetter(MessageBus bus) {
		super(bus,"MineSetter", 3, 1f, 1);

	}

	@Override
	protected TankAttack shoot(Tank sender, MessageBus bus) {
		SoundManager.get().playSoundEffect("deployMine");
		return new AntiTankMine(sender, sender.hitbox.getAbsoluteCenter(), bus);
	}

}
