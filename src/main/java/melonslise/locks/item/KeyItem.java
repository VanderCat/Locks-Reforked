package melonslise.locks.item;

import melonslise.locks.Locks;
import melonslise.locks.components.Locked;
import melonslise.locks.init.LocksItemTags;
import melonslise.locks.init.LocksSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import static melonslise.locks.events.LocksEvents.LOCKED_MESSAGE;

public class KeyItem extends LockingItem
{
	public KeyItem(Properties props)
	{
		super(props);
	}

    public boolean canOpen(ItemStack key, ItemStack lock) {
        return KeyItem.getOrSetId(key) == LockingItem.getOrSetId(lock);
    }

	// TODO Sound pitch
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
        var world = ctx.getLevel();
        var player = ctx.getPlayer();
        var be = world.getBlockEntity(ctx.getClickedPos());
        if (be == null)
            return InteractionResult.PASS;
        var locked = Locked.getFrom(be);
        var itemLock = locked.getLock();
        if (itemLock.isEmpty())
            return InteractionResult.PASS;
        if (locked.isOpen() && !player.isSecondaryUseActive())
            return InteractionResult.PASS;
        if (canOpen(ctx.getItemInHand(), itemLock)) {
            world.playSound(player, be.getBlockPos(), LocksSoundEvents.LOCK_OPEN, SoundSource.BLOCKS, 1f, 1f);
            LockItem.toggleOpen(itemLock);
            locked.sync();
            return InteractionResult.SUCCESS;
        }

        locked.swing(20);
        world.playSound(player, be.getBlockPos(), LocksSoundEvents.LOCK_RATTLE, SoundSource.BLOCKS, 1f, 1f);
        if(Locks.CONFIG.deafMode())
            player.displayClientMessage(LOCKED_MESSAGE, true);
        return InteractionResult.FAIL;
	}
}