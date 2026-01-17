package melonslise.locks.worldgen.feature;

import com.mojang.serialization.Codec;

import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.components.Locked;
import melonslise.locks.init.LocksBlockTags;
import melonslise.locks.init.LocksLootParamSets;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.CHEST_TYPE;

public class LocksFeature extends Feature<NoneFeatureConfiguration> {
    public LocksFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean SpawnLock(FeaturePlaceContext<NoneFeatureConfiguration> context, BlockState state, BlockPos pos) {
        var level = context.level();
        var lootTable = level.getServer().getLootData().getLootTable(new ResourceLocation(Locks.ID, "locks"));
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
            return true;
        }
        var stack = loot.pop();
        if (stack == null)
            return true;
        //Log.info(LogCategory.LOG, "yes lock "+stack.toString());
        var blockPos = context.origin();

        BlockPos pos1 = blockPos;
//        Direction dir = null;
//        if (state.hasProperty(FACING)) {
//            dir = state.getValue(FACING);
//        } else if (state.hasProperty(HORIZONTAL_FACING)) {
//            dir = state.getValue(HORIZONTAL_FACING);
//        } else {
//            return false;
//        }

        if (state.hasProperty(CHEST_TYPE)) {
            switch (state.getValue(CHEST_TYPE)) {
                case LEFT -> pos1 = blockPos.relative(ChestBlock.getConnectedDirection(state));
                case RIGHT -> {
                    return false;
                }
            }
        }
//        if (state.hasProperty(DOUBLE_BLOCK_HALF)) {
//            if (state.getValue(DOUBLE_BLOCK_HALF) == LOWER) return false;
//            pos1 = blockPos.below();
//            if (state.hasProperty(DOOR_HINGE)) {
//                if (state.hasProperty(DOOR_HINGE) && state.hasProperty(HORIZONTAL_FACING)) {
//                    BlockPos pos2 = pos1.relative(state.getValue(DOOR_HINGE) == LEFT ? dir.getClockWise() : dir.getCounterClockWise());
//                    if (level.getBlockState(pos2).is(state.getBlock())) {
//                        if (state.getValue(DOOR_HINGE) == LEFT) {
//                            return false;
//                        }
//                        pos1 = pos2;
//                    }
//                }
//                dir = dir.getOpposite();
//            }
//        }
        var be = level.getBlockEntity(pos);
        if (be == null)
            return false;
        var locked = Locked.getFrom(be, level.getLevel());
        if (locked == null)
            return false;

        locked.setLock(stack);
        locked.sync();

        return true;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var level = context.level();
        var origin = context.origin();
        BlockPos testPos = new BlockPos(origin);
        //Log.info(LogCategory.LOG, ""+origin);
        //Log.info(LogCategory.LOG, ""+origin);
//        if (testPos.getX()%16 == 0 && testPos.getZ()%16 == 0 && testPos.getY() == level.getMinBuildHeight()) {
//            //highly likely generating for chunk
//            var success = false;
//
//            for (int x = origin.getX(); x < origin.getX()+16; x++)
//            for (int z = origin.getZ(); z < origin.getZ()+16; z++)
//                for (int y = origin.getY(); y < level.getHeight(); y++) {
//                    var pos = new BlockPos(x, y, z);
//                    var state = level.getBlockState(pos);
//
//                    if (state.is(LocksBlockTags.TREASURE))
//                        success |= SpawnLock(context, state, origin);
//                }
//            return success;
//        }
        //for manual invoking /place feature locks:lock
        var state = level.getBlockState(testPos);
        //Log.info(LogCategory.LOG, ""+origin.getY()+" "+state);
        return SpawnLock(context, state, origin);
    }
}
