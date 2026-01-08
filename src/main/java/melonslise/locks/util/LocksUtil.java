package melonslise.locks.util;

import melonslise.locks.init.LocksBlockTags;
import melonslise.locks.mixin.accessor.LootPoolAccessor;
import melonslise.locks.mixin.accessor.LootTableAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class LocksUtil {
    public static ResourceManager resourceManager;

    public static boolean canLock(Level world, BlockPos pos) {
        var block = world.getBlockState(pos);
        return block.is(LocksBlockTags.LOCKABLE);
    }

    public static void shuffle(byte[] array, Random rng) {
        for (int a = array.length - 1; a > 0; --a) {
            int index = rng.nextInt(a + 1);
            byte temp = array[index];
            array[index] = array[a];
            array[a] = temp;
        }
    }

    // Only merges entries, not conditions and functions
    public static LootTable mergeEntries(LootTable table, LootTable inject) {
        List<LootPool> list = Arrays.asList(((LootTableAccessor) table).getPools());
        for (LootPool injectPool : ((LootTableAccessor) inject).getPools()) {
            if (list.contains(injectPool)) {
                ((LootPoolAccessor) injectPool).getEntries().addAll(((LootPoolAccessor) injectPool).getEntries());
            } else {
                list.add(injectPool);
            }
        }
        ((LootTableAccessor) inject).setPools(list.toArray(new LootPool[0]));
        return table;
    }

    public static ItemStack getRandomLock(ServerLevel level) {
        var lootTable = level.getServer().getLootData().getLootTable(new ResourceLocation("locks:locks"));
        var params = new LootParams.Builder(level).create(LootContextParamSets.EMPTY);
        var context = new LootContext.Builder(params).withOptionalRandomSeed(0).create(lootTable.randomSequence);
        var loot = lootTable.getRandomItems(context);
        if (loot.size() < 1) {
            return null;
        }
        return loot.pop();
    }

}