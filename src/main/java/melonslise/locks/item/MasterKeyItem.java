package melonslise.locks.item;

import melonslise.locks.init.LocksSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class MasterKeyItem extends KeyItem
{
	public MasterKeyItem(Properties props)
	{
		super(props.stacksTo(1));
	}

	// TODO Sound pitch
	@Override
    public boolean canOpen(ItemStack key, ItemStack lock) {
        return true;
    }
}