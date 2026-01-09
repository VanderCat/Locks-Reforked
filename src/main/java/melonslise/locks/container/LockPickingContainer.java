package melonslise.locks.container;

import melonslise.locks.Locks;
import melonslise.locks.client.gui.LockPickingScreen;
import melonslise.locks.components.Locked;
import melonslise.locks.init.*;
import melonslise.locks.item.LockItem;
import melonslise.locks.item.LockPickItem;
import melonslise.locks.network.LocksNetwork;
import melonslise.locks.network.client.TryPinResult;
import melonslise.locks.util.LocksUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Random;

public class LockPickingContainer extends AbstractContainerMenu {
	public static class HiddenSlot extends Slot
	{
		public HiddenSlot(Inventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Environment(EnvType.CLIENT)
		@Override
		public boolean isActive()
		{
			return false;
		}
	}

	public static final Component TITLE = Component.translatable(Locks.ID + ".gui.lockpicking.title");

	public final Player player;
	public final InteractionHand hand;
	public final Locked lock;

	public final BlockPos pos;

	public final int shocking, sturdy, complexity;

	protected int currIndex = 0;
	protected final byte[] combo;
	protected boolean locked;

	//  TODO if lock is reshuffled any time other than during creation, then next time it is loaded it will have the initial combination and not the newly reshuffled one. Thankfully reshuffling like that does happen, but this should be changed if it does happen
	public final Random rng;

	public LockPickingContainer(int id, Player player, InteractionHand hand, BlockPos pos) {
		super(LocksContainerTypes.LOCK_PICKING, id);
		var level = player.level();
		var be = level.getBlockEntity(pos);
		if (be == null)
			throw new NullPointerException("Attempt to open a lockpicking container on non-blockentity");
		this.player = player;
		this.hand = hand;
		this.lock = Locked.getFrom(be);
		this.pos = pos;
		this.rng = new Random(LockItem.getOrSetId(this.lock.getLock()));
		this.combo = shuffle(LockItem.getOrSetLength(this.lock.getLock()));

		this.shocking = EnchantmentHelper.getItemEnchantmentLevel(LocksEnchantments.SHOCKING, this.lock.getLock());
		this.sturdy = EnchantmentHelper.getItemEnchantmentLevel(LocksEnchantments.STURDY, this.lock.getLock());
		this.complexity = EnchantmentHelper.getItemEnchantmentLevel(LocksEnchantments.COMPLEXITY, this.lock.getLock());

//		for (int rows = 0; rows < 3; ++rows)
//			for (int cols = 0; cols < 9; ++cols)
//				this.addSlot(new HiddenSlot(player.getInventory(), cols + rows * 9 + 9, 0, 0));
//
//		for (int slots = 0; slots < 9; ++slots)
//			this.addSlot(new HiddenSlot(player.getInventory(), slots, 0, 0));
	}


	public byte[] shuffle(int length) {
		byte[] combo = new byte[length];
		for(byte a = 0; a < length; ++a)
			combo[a] = a;
		LocksUtil.shuffle(combo, this.rng);
		return combo;
	}

	public int getLength() {
		return this.combo.length;
	}

	public boolean isLockItemOpen() {
		return LockItem.isOpen(this.lock.getLock());
	}

	public void setLockItemOpen(boolean open) {
		LockItem.setOpen(this.lock.getLock(), open);
	}

	public int getPin(int index) {
		return this.combo[index];
	}

	public boolean checkPin(int index, int pin) {
		return this.getPin(index) == pin;
	}

	public boolean isValidPick(ItemStack stack) {
		return stack.is(LocksItemTags.LOCK_PICKS) && LockPickItem.canPick(stack, this.complexity);
	}

	@Override
	public boolean stillValid(Player player) {
		return !isLockItemOpen() && this.isValidPick(player.getItemInHand(this.hand));
	}

	public boolean isOpen() {
		//是否已完全打开
		return this.currIndex == LockItem.getOrSetLength(lock.getLock());
	}

	protected void reset() {
		//重置进度
		this.currIndex = 0;
	}

	// SERVER ONLY
	public void tryPin(int currPin) {
		//尝试开启一个销钉（服务端）
		if(this.isOpen())
			return;
		boolean correct = false;
		boolean reset = false;
		if(checkPin(this.currIndex, currPin)) {
			++this.currIndex;
			correct = true;
			this.player.level().playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), LocksSoundEvents.PIN_MATCH, SoundSource.BLOCKS, 1f, 1f);
		}
		else
			if(this.tryBreakPick(player, currPin)) {
				reset = true;
				this.reset();
				if(this.shocking > 0) {
					this.player.hurt(LocksDamageSources.getDamageSource(this.player.level(), LocksDamageSources.SHOCK), shocking * 1.5f);
					this.player.level().playSound(null, this.player.position().x, this.player.position().y, this.player.position().z, LocksSoundEvents.SHOCK, SoundSource.BLOCKS, 1f, 1f);
				}
			}
			else this.player.level().playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), LocksSoundEvents.PIN_FAIL, SoundSource.BLOCKS, 1f, 1f);

		LocksNetwork.CHANNEL.serverHandle(player).send(new TryPinResult(correct, reset));
	}

	@Environment(EnvType.CLIENT)
	public void handlePin(boolean correct, boolean reset) {
		//处理销钉交互
		Screen screen = Minecraft.getInstance().screen;
		if(screen instanceof LockPickingScreen lockPickingScreen) {
			lockPickingScreen.handlePin(correct, reset);
			if(correct) ++this.currIndex;
			if(reset) this.reset();
		}
	}
	//TODO: if player has increased luck, lower the chance of pin breaking and make it optional
	protected boolean tryBreakPick(Player player, int pin) {
		//是否断开工具
		ItemStack pickStack = player.getItemInHand(this.hand);
		float sturdyModifier = this.sturdy == 0 ? 1f : 0.75f + this.sturdy * 0.5f;
		float ch = LockPickItem.getOrSetStrength(pickStack) / sturdyModifier;
		float ex = (1f - ch) * (1f - this.getBreakChanceMultiplier(pin));
		if (!pickStack.is(LocksItemTags.LOCK_PICKS) || player.getRandom().nextFloat() < ex + ch)
			return false;
		this.player.broadcastBreakEvent(this.hand);
		pickStack.shrink(1);
		if (pickStack.isEmpty())
			for (int a = 0; a < player.getInventory().getContainerSize(); ++a) {
				ItemStack stack = player.getInventory().getItem(a);
				if (this.isValidPick(stack)) {
					player.setItemInHand(hand, stack);
					player.getInventory().removeItemNoUpdate(a);
					break;
				}
			}
		return true;
	}

	/*
	protected float getPinDifficulty(int index)
	{
		// Basically takes the the distance (how many pins away) between the clicked pin and the next correct pin, then divides that by the amount of pins left to click and then plugs it into a simple linear function -ax+a where a = 0.4
		// This way we get a higher chance to break the further away we were from the correct pin
		return -0.5f * ((float) (index - this.currIndex) / (this.lockable.lock.getLength() - this.currIndex - 1)) + 0.5f;
	}
	*/

	protected float getBreakChanceMultiplier(int pin)
	{
		//概率
		return Math.abs(this.getPin(this.currIndex) - pin) == 1 ? 0.33f : 1f;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{
		//禁止交互
        return ItemStack.EMPTY;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		if(!this.isOpen() || this.isLockItemOpen())
			return;
		this.player.level().playSound(player, this.pos.getX(), this.pos.getY(), this.pos.getZ(), LocksSoundEvents.LOCK_OPEN, SoundSource.BLOCKS, 1f, 1f);
		this.setLockItemOpen(this.isOpen());
		this.lock.sync();
	}

	public static final ExtendedScreenHandlerType.ExtendedFactory<LockPickingContainer> FACTORY = (id, inv, buf) ->
		new LockPickingContainer(id, inv.player, buf.readEnum(InteractionHand.class), buf.readBlockPos());


	public static class Provider implements ExtendedScreenHandlerFactory {
		public final ItemStack lockItem;
		public final InteractionHand hand;
		public final BlockPos pos;

		public Provider(InteractionHand hand, ItemStack lock, BlockPos pos) {
			this.hand = hand;
			this.lockItem = lock;
			this.pos = pos;
		}

		@Override
		public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
			return new LockPickingContainer(id, player, this.hand, this.pos);
		}

		@Override
		public Component getDisplayName() {
			return TITLE;
		}

		@Override
		public void writeScreenOpeningData(ServerPlayer serverPlayer, FriendlyByteBuf buf) {
			buf.writeEnum(this.hand);
			buf.writeBlockPos(this.pos);
		}
	}
}