package melonslise.locks.common.worldgen.feature;

import com.mojang.serialization.Codec;

import melonslise.locks.Locks;
import melonslise.locks.common.components.LockableHandler;
import melonslise.locks.common.components.interfaces.ILockableHandler;
import melonslise.locks.common.components.interfaces.ILockableStorage;
import melonslise.locks.common.config.LocksConfig;
import melonslise.locks.common.config.LocksServerConfig;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.init.LocksLootParamSets;
import melonslise.locks.common.network.toclient.AddLockablePacket;
import melonslise.locks.common.util.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import org.intellij.lang.annotations.Identifier;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;
import static net.minecraft.world.level.block.state.properties.DoorHingeSide.LEFT;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;

import java.rmi.registry.Registry;
import java.util.List;

public class LocksFeature extends Feature<NoneFeatureConfiguration> {
    public LocksFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean SpawnLock(FeaturePlaceContext<NoneFeatureConfiguration> context, BlockState state, BlockPos pos) {
        var level = context.level();
        var lootTable = level.getServer().getLootData().getLootTable(new ResourceLocation("locks:locks"));
        var params = new LootParams.Builder(level.getLevel())
            .withParameter(LootContextParams.BLOCK_STATE, state)
            .create(LocksLootParamSets.LOCK);
        var lootctx = new LootContext.Builder(params);
        //lootctx.random = context.random();
        if (state.hasBlockEntity()){
            var tag = new CompoundTag();
            var ent = context.level().getBlockEntity(pos);
            if (ent != null) {
                ent.load(tag);
                if (tag.contains("LootTableSeed")) {
                    lootctx.withOptionalRandomSeed(tag.getLong("LootTableSeed"));
                } else {
                    lootctx.random = context.random();
                }
            } else {
                lootctx.random = context.random();
            }
        } else {
            lootctx.random = context.random();
        }
        
        var loot = lootTable.getRandomItems(lootctx.create(lootTable.randomSequence));
        if (loot.size() < 1) {
            return false;
        }
        var stack = loot.pop();
        if (stack == null) 
            return false;
        Log.info(LogCategory.LOG, "yes lock "+stack.toString());
        var blockPos = context.origin();
        Log.info(LogCategory.LOG, "yes treasure");
        BlockPos pos1 = blockPos;
        Direction dir = null;
        if (state.hasProperty(FACING)) {
            dir = state.getValue(FACING);
        } else if (state.hasProperty(HORIZONTAL_FACING)) {
            dir = state.getValue(HORIZONTAL_FACING);
        } else {
            return false;
        }

        if (state.hasProperty(CHEST_TYPE)) {
            switch (state.getValue(CHEST_TYPE)) {
                case LEFT -> pos1 = blockPos.relative(ChestBlock.getConnectedDirection(state));
                case RIGHT -> {
                    return false;
                }
            }
        }
        if (state.hasProperty(DOUBLE_BLOCK_HALF)) {
            if (state.getValue(DOUBLE_BLOCK_HALF) == LOWER) return false;
            pos1 = blockPos.below();
            if (state.hasProperty(DOOR_HINGE)) {
                if (state.hasProperty(DOOR_HINGE) && state.hasProperty(HORIZONTAL_FACING)) {
                    BlockPos pos2 = pos1.relative(state.getValue(DOOR_HINGE) == LEFT ? dir.getClockWise() : dir.getCounterClockWise());
                    if (level.getBlockState(pos2).is(state.getBlock())) {
                        if (state.getValue(DOOR_HINGE) == LEFT) {
                            return false;
                        }
                        pos1 = pos2;
                    }
                }
                dir = dir.getOpposite();
            }
        }
        Cuboid6i bb = new Cuboid6i(blockPos, pos1);
        Lock lock = Lock.from(stack);
        Transform tr = Transform.fromDirection(dir, dir);
        var lkb = new Lockable(bb, lock, tr, stack, level.getLevel());
        LockableHandler handler = (LockableHandler)LocksComponents.LOCKABLE_HANDLER.get(level.getLevel());
        if(lkb.bb.volume() > LocksServerConfig.MAX_LOCKABLE_VOLUME.get())
            return false;
        List<ILockableStorage> sts = lkb.bb.containedChunksTo((x, z) ->
        {
            try {
                var levelChunk = level.getChunk(x, z);
                ILockableStorage st = LocksComponents.LOCKABLE_STORAGE.get(levelChunk);
                return st.get().values().stream().anyMatch(lkb1 -> lkb1.bb.intersects(lkb.bb)) ? null : st;
            } catch (Exception e){
                Locks.LOGGER.warn("Chunk not gen");
            }
            return null;
        }, true);
        if(sts == null)
            return false;
        // Add to chunk
        for(int a = 0; a < sts.size(); ++a)
            sts.get(a).add(lkb);
        // Add to world
        handler.lockables.put(lkb.id, lkb);
        lkb.addObserver(handler);
        return true;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        var tag = TagKey.create(Registries.BLOCK, new ResourceLocation("locks:treasure"));
        BlockPos testPos = new BlockPos(origin);
        //Log.info(LogCategory.LOG, ""+origin);
        Log.info(LogCategory.LOG, ""+origin);
        if (testPos.getX()%16 == 0 && testPos.getZ()%16 == 0 && testPos.getY() == level.getMinBuildHeight()) {
            //highly likely generating for chunk
            var success = false;

            for (int x = origin.getX(); x < origin.getX()+16; x++)
            for (int z = origin.getZ(); z < origin.getZ()+16; z++)
                for (int y = origin.getY(); y < level.getHeight(); y++) {
                    var pos = new BlockPos(x, y, z);
                    var state = level.getBlockState(pos);

                    if (state.is(tag))
                        success |= SpawnLock(context, state, origin);
                }
            return success;
        }
        //for manual invoking /place feature locks:lock
        var state = level.getBlockState(testPos);
        //Log.info(LogCategory.LOG, ""+origin.getY()+" "+state);
        if (state.is(tag)) 
            return SpawnLock(context, state, origin);
        return false;
    }
}
