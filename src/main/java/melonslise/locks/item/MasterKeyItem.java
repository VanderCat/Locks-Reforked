package melonslise.locks.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class MasterKeyItem extends Item
{
	public MasterKeyItem(Properties props)
	{
		super(props.stacksTo(1));
	}

	// TODO Sound pitch
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return InteractionResult.SUCCESS;
	}
}