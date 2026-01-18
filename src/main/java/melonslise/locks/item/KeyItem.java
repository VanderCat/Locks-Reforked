package melonslise.locks.item;

import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.init.LocksItemTags;
import melonslise.locks.init.LocksSoundEvents;
import net.minecraft.network.chat.Component;
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
        var pos = ctx.getClickedPos();
        var locked = AbstractLocked.getFrom(world, pos);
        if (locked == null)
            return InteractionResult.PASS;
        var itemLock = locked.getLock();
        if (itemLock.isEmpty())
            return InteractionResult.PASS;
        if (locked.isOpen() && !player.isSecondaryUseActive())
            return InteractionResult.PASS;
        if (canOpen(ctx.getItemInHand(), itemLock)) {
            world.playSound(player, pos, LocksSoundEvents.LOCK_OPEN, SoundSource.BLOCKS, 1f, 1f);
            LockItem.toggleOpen(itemLock);
            locked.sync();
            return InteractionResult.SUCCESS;
        }

        locked.swing(20);
        world.playSound(player, pos, LocksSoundEvents.LOCK_RATTLE, SoundSource.BLOCKS, 1f, 1f);
        if(Locks.CONFIG.deafMode())
            player.displayClientMessage(Component.translatable(Locks.ID+".status.wrong_key"), true);
        return InteractionResult.FAIL;
	}
}