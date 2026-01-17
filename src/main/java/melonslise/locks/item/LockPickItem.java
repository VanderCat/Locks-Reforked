package melonslise.locks.item;

import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.container.LockPickingContainer;
import melonslise.locks.init.LocksEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;

public class LockPickItem extends Item
{
	public static final Component TOO_COMPLEX_MESSAGE = Component.translatable(Locks.ID + ".status.too_complex");

	private final float strength;
    private final int tier;

	public LockPickItem(float strength, int tier, Properties props) {
		super(props);
		this.strength = strength;
        this.tier = tier;
	}

	private static final String KEY_STRENGTH = "strength";
    private static final String KEY_TIER = "tier";

	// WARNING: EXPECTS LOCKPICKITEM STACK
	public static float getOrSetStrength(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if(!nbt.contains(KEY_STRENGTH))
			nbt.putFloat(KEY_STRENGTH, ((LockPickItem) stack.getItem()).strength);
		return nbt.getFloat(KEY_STRENGTH);
	}

    public static float getOrSetTier(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if(!nbt.contains(KEY_TIER))
            nbt.putFloat(KEY_TIER, ((LockPickItem)stack.getItem()).tier);
        return nbt.getFloat(KEY_TIER);
    }

	/**
	 *	Verifies if the lockpick is able to bypass the complexity due to its strength.
	 *
	 * @param stack - The lock pick
	 * @param level - The enchantment level of complexity within the lock
	 * @return - Whether it can lock pick or not
	 */
	public static boolean canPick(ItemStack stack, int level) {
		return (getOrSetTier(stack) >= level);
	}

	public static boolean canPick(ItemStack lockpick, ItemStack lock) {
		return canPick(lockpick, EnchantmentHelper.getItemEnchantmentLevel(LocksEnchantments.COMPLEXITY, lock));
	}

	/**
	 * Should start lock picking if a lock is found.
	 *
	 * @param ctx - Context to the action done by the player
	 * @return - Consequences from the action
	 */
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		var world = ctx.getLevel();
		var player = ctx.getPlayer();
		var pos = ctx.getClickedPos();

		var lock = AbstractLocked.getFrom(world, pos);
        if (lock == null)
            return InteractionResult.PASS;
		var lockStack = lock.getLock();
		if (lockStack.isEmpty())
			return InteractionResult.PASS;
        if (lock.isOpen())
            return InteractionResult.PASS;
		if(!canPick(ctx.getItemInHand(), lockStack)) {
			Locks.LOGGER.warn("Could not pick a lock");
			if(world.isClientSide)
				player.displayClientMessage(TOO_COMPLEX_MESSAGE, true);
			return InteractionResult.PASS;
		}
		if(world.isClientSide)
			return InteractionResult.SUCCESS;

		//It opens the lock picking minigame.
		InteractionHand hand = ctx.getHand();
		if(player instanceof ServerPlayer) {
			player.openMenu(new LockPickingContainer.Provider(hand, lockStack, ctx.getClickedPos()));
		}
		return InteractionResult.SUCCESS;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, lines, flag);
		lines.add(Component.translatable(Locks.ID + ".tooltip.strength", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(stack.hasTag() && stack.getTag().contains(KEY_STRENGTH) ? stack.getTag().getFloat(KEY_STRENGTH) : this.strength)).withStyle(ChatFormatting.DARK_GRAY));
	}
}