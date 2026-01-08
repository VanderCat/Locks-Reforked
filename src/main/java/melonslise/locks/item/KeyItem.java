package melonslise.locks.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class KeyItem extends LockingItem
{
	public KeyItem(Properties props)
	{
		super(props);
	}

	// TODO Sound pitch
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return InteractionResult.SUCCESS;
	}
}