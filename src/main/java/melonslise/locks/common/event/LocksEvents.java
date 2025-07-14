package melonslise.locks.common.event;

import melonslise.locks.Locks;
import melonslise.locks.common.components.Locked;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.init.LocksItemTags;
import melonslise.locks.common.init.LocksItems;
import melonslise.locks.common.init.LocksSoundEvents;
import melonslise.locks.common.item.*;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;


public final class LocksEvents
{
	public static final Component LOCKED_MESSAGE = Component.translatable(Locks.ID + ".status.locked");

	private LocksEvents() {}



	public static void onLootTableLoad(ResourceManager resourceManager, LootDataManager lootManager, ResourceLocation id, LootTable.Builder tableBuilder, LootTableSource source)
	{
//		// Only modify if it was a vanilla chest loot table
//
//        if (!id.getNamespace().equals("minecraft") || !id.getPath().startsWith("chests"))
//			return;
//		// And only if there is a corresponding inject table...
//		ResourceLocation injectLoc = new ResourceLocation(Locks.ID, "loot_tables/inject/" + id.getPath() + ".json");
//		if (LocksUtil.resourceManager.getResource(injectLoc).isEmpty())
//			return;
//		// todo (kota): bring back
	}

//	public static InteractionResult handleLocked(Player player, Level world, InteractionHand hand, BlockHitResult result, ItemStack itemLock, ItemStack itemInHand) {
//
//		player.swing(InteractionHand.MAIN_HAND);
//	}

	public static InteractionResult onRightClick(Player player, Level world, InteractionHand hand, BlockHitResult result) {
		var pos = result.getBlockPos();
		var ent = world.getBlockEntity(pos);
		if (ent == null)
			return InteractionResult.PASS;
		Locked lock = ent.getComponent(LocksComponents.LOCKED);
		var itemInHand = player.getItemInHand(hand);
		var itemLock = lock.getLock();
		if (itemLock.isEmpty())
			return InteractionResult.PASS;
		if (hasMatchingKey(itemLock, itemInHand, player)) {
			world.playSound(player, pos, LocksSoundEvents.LOCK_OPEN, SoundSource.BLOCKS, 1f, 1f);
			LockItem.toggleOpen(itemLock);
			lock.sync();
			return InteractionResult.SUCCESS;
		}
		if (LockItem.isOpen(itemLock)) {
			//TODO: Allow removing generated locks instead of all locks
			if(Locks.CONFIG.allowRemovingLocks()) {
				if (player.isShiftKeyDown() && itemInHand.isEmpty()) {
					world.playSound(player, pos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 0.8f, 0.8f + player.getRandom().nextFloat() * 0.4f);
					player.swing(hand);
					if(player instanceof ServerPlayer) {
						Locks.LOGGER.info("Removing lockable");
						ItemEntity itemEntity = new ItemEntity(world, result.getLocation().x, result.getLocation().y, result.getLocation().z, lock.getLock());
						itemEntity.setDefaultPickUpDelay();
						world.addFreshEntity(itemEntity);
						lock.removeLock();
						lock.sync();
						return InteractionResult.CONSUME;
					}
				}
			}
			return InteractionResult.PASS;
		}
		if (canLockpick(itemLock, itemInHand, player)) {
			return InteractionResult.PASS;
		}
		//lkb.swing(20);
		world.playSound(player, result.getBlockPos(), LocksSoundEvents.LOCK_RATTLE, SoundSource.BLOCKS, 1f, 1f);
		if(Locks.CONFIG.deafMode())
			player.displayClientMessage(LOCKED_MESSAGE, true);
		return InteractionResult.FAIL;
	}

	public static boolean canLockpick(ItemStack lock, ItemStack key, Player player) {
		return key.is(LocksItemTags.LOCK_PICKS);
	}

	public static boolean hasMatchingKey(ItemStack lock, ItemStack key, Player player) {
		if (!lock.is(LocksItemTags.LOCKS)) {
			return false;
		}
		int lockId = LockingItem.getOrSetId(lock);

		// Check if it's a matching key
		if (key.is(LocksItemTags.KEYS)) {
			int keyId = KeyItem.getOrSetId(key);
			return lockId == keyId;
		}

		// Check if it's the master key
		if (key.getItem() == LocksItems.MASTER_KEY)
			return true;

		// Check if it's a keyring containing the correct key
		if (key.getItem() == LocksItems.KEY_RING)
			return KeyRingItem.containsId(key, lockId);

//		//Checks if the player is the owner of the smart lock.
//		if (!stack.is(LocksItemTags.LOCK_PICKS) && lkb.isSmart() && SmartLockItem.getOrSetOwner(lkb.stack, player).equals(player.getUUID())) return true;

		return false;
	}


//	public static boolean canBreakLockable(Level world,Player player, BlockPos pos)
//	{
//		return (Locks.CONFIG.protectLockables() &&
//				!player.isCreative() &&
//				LocksUtil.lockedAndRelated(world, pos));
//	}

	public static boolean onBlockBreaking(Level world, Player player, BlockPos pos, BlockState state,@Nullable BlockEntity entity)
	{
        //return !canBreakLockable(world,player, pos);
		throw new RuntimeException();
	}

	public static void onBlockBreak(Level world, Player player, BlockPos pos, BlockState state,@Nullable BlockEntity entity)
	{
//		if(!canBreakLockable(world,player, pos)) {
//			world.setBlockAndUpdate(pos, state);
//		}
	}

	public static void register()
	{
//		LootTableEvents.MODIFY.register(LocksEvents::onLootTableLoad);
//		PlayerBlockBreakEvents.BEFORE.register(LocksEvents::onBlockBreaking);
//		PlayerBlockBreakEvents.AFTER.register(LocksEvents::onBlockBreak);
		UseBlockCallback.EVENT.register(LocksEvents::onRightClick);
	}

}