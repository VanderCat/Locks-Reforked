package melonslise.locks.item;

import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.init.LocksComponents;
import melonslise.locks.init.LocksSoundEvents;
import melonslise.locks.util.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class LockItem extends LockingItem {
	public final int length;
	public final int enchantmentValue;
	public final float resistance;

	public LockItem(int length, int enchVal, float resist, Properties props) {
		super(props);
		this.length = length;
		this.enchantmentValue = enchVal;
		this.resistance = resist;
	}

	public static final String KEY_OPEN = "Open";
	public static final String KEY_LENGTH = "Length";

	public static boolean isOpen(ItemStack stack)
	{
		return stack.getOrCreateTag().getBoolean(KEY_OPEN);
	}

	public static void setOpen(ItemStack stack, boolean open)
	{
		stack.getOrCreateTag().putBoolean(KEY_OPEN, open);
	}

	public static void toggleOpen(ItemStack stack) {
		setOpen(stack, !isOpen(stack));
	}

	// WARNING: EXPECTS LOCKITEM STACK
	public static byte getOrSetLength(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if(!nbt.contains(KEY_LENGTH))
			nbt.putByte(KEY_LENGTH, (byte) ((LockItem) stack.getItem()).length);
		return nbt.getByte(KEY_LENGTH);
	}

	// WARNING: EXPECTS LOCKITEM STACK
	public static float getResistance(ItemStack stack)
	{
		return ((LockItem) stack.getItem()).resistance;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		var world = ctx.getLevel();
		var pos = ctx.getClickedPos();
		if (!LocksUtil.canLock(world, pos))
			return InteractionResult.PASS;
		var lock = AbstractLocked.getFrom(world, pos);
        if (lock == null)
            return InteractionResult.FAIL;
		lock.setLock(ctx.getItemInHand());
		lock.setDirection(ctx.getClickedFace());
		ctx.getPlayer().getInventory().removeItem(ctx.getItemInHand());
		lock.sync();
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if(!isOpen(stack))
			return super.use(world, player, hand);
		setOpen(stack, false);
		world.playSound(player, player.getX(), player.getY(), player.getZ(), LocksSoundEvents.LOCK_CLOSE, SoundSource.PLAYERS, 1f, 1f);
		return super.use(world, player, hand);
	}

	@Override
	public boolean isEnchantable(ItemStack p_77616_1_)
	{
		return true;
	}

	@Override
	public int getEnchantmentValue()
	{
		return this.enchantmentValue;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag flag) {
		super.appendHoverText(stack, world, lines, flag);
		lines.add(Component.translatable(Locks.ID + ".tooltip.length", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(stack.hasTag() && stack.getTag().contains(KEY_LENGTH) ? stack.getTag().getByte(KEY_LENGTH) : this.length)).withStyle(ChatFormatting.DARK_GRAY));
	}
}