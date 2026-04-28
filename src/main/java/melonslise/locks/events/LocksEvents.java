package melonslise.locks.events;

import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.init.LocksComponents;
import melonslise.locks.init.LocksItemTags;
import melonslise.locks.init.LocksItems;
import melonslise.locks.init.LocksSoundEvents;
import melonslise.locks.item.*;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public final class LocksEvents
{
	public static final Component LOCKED_MESSAGE = Component.translatable(Locks.ID + ".status.locked");

    public static final DoubleBlockCombiner.Combiner<BlockEntity, Optional<AbstractLocked>> LOCK_COMBINER =
        new DoubleBlockCombiner.Combiner<>() {
            public Optional<AbstractLocked> acceptDouble(BlockEntity blockEntity, BlockEntity blockEntity2) {
                var locked = blockEntity.getComponent(LocksComponents.LOCKED);
                if (locked.getLock().isEmpty())
                    return Optional.of(blockEntity2.getComponent(LocksComponents.LOCKED));
                return Optional.of(locked);
            }

            public Optional<AbstractLocked> acceptSingle(BlockEntity blockEntity) {
                return Optional.of(blockEntity.getComponent(LocksComponents.LOCKED));
            }

            public Optional<AbstractLocked> acceptNone() {
                return Optional.empty();
            }
    };

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

	public static InteractionResult onRightClick(Player player, Level world, InteractionHand hand, BlockHitResult result) {
		var pos = result.getBlockPos();

        var lock = AbstractLocked.getFrom(world, pos);
        if (lock == null)
            return InteractionResult.PASS;
		var itemInHand = player.getItemInHand(hand);
		var itemLock = lock.getLock();
		if (itemLock.isEmpty())
			return InteractionResult.PASS;
		if (LockItem.isOpen(itemLock)) {
			//TODO: Allow removing generated locks instead of all locks
			if(Locks.CONFIG.allowRemovingLocks()) {
				if (player.isSecondaryUseActive() && itemInHand.isEmpty() ) {
					world.playSound(player, pos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 0.8f, 0.8f + player.getRandom().nextFloat() * 0.4f);
					player.swing(hand);
					if(player instanceof ServerPlayer) {
						Locks.LOGGER.info("Removing lockable");
						ItemEntity itemEntity = new ItemEntity(world, result.getLocation().x, result.getLocation().y, result.getLocation().z, lock.getLock());
						itemEntity.setDefaultPickUpDelay();
						world.addFreshEntity(itemEntity);
						lock.removeLock();
						lock.sync();
                        return InteractionResult.SUCCESS;
					}
				}
			}
            return InteractionResult.PASS;
		}
        if (itemInHand.is(LocksItemTags.KEYS) || itemInHand.is(LocksItemTags.LOCK_PICKS)) {
            var ctx = new UseOnContext(world, player, hand, itemInHand, result);
            var result1 = itemInHand.useOn(ctx);
            if (result1.consumesAction()) {
                return result1;
            }
        }
        player.swing(hand);
		lock.swing(20);
		world.playSound(player, result.getBlockPos(), LocksSoundEvents.LOCK_RATTLE, SoundSource.BLOCKS, 1f, 1f);
		if(Locks.CONFIG.deafMode())
			player.displayClientMessage(LOCKED_MESSAGE, true);
		return InteractionResult.FAIL;
	}


	public static boolean canBreakLockable(Player player, Level world, BlockPos pos, @Nullable BlockEntity entity) {
        if (entity == null)
            return true;
        if (!Locks.CONFIG.protectLockables())
            return true;
		var locked = AbstractLocked.getFrom(world, pos);
        if (locked == null)
            return true;
        if (player.isCreative())
            return true;
        if (locked.getLock().isEmpty())
            return true;
        if (!locked.isOpen())
            return false;
        return true;
	}

	public static boolean onBlockBreaking(Level world, Player player, BlockPos pos, BlockState state,@Nullable BlockEntity entity) {
        return canBreakLockable(player, world, pos, entity);
	}

    private static InteractionResult onAttackBlock(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction) {
        if (player.isSpectator())
            return InteractionResult.PASS;
        if (!canBreakLockable(player, world, pos, world.getBlockEntity(pos)))
            return InteractionResult.FAIL;
        return InteractionResult.PASS;
    }

	public static void register() {
//		LootTableEvents.MODIFY.register(LocksEvents::onLootTableLoad);
		PlayerBlockBreakEvents.BEFORE.register(LocksEvents::onBlockBreaking);
        AttackBlockCallback.EVENT.register(LocksEvents::onAttackBlock);
		UseBlockCallback.EVENT.register(LocksEvents::onRightClick);
	}
}