package melonslise.locks.datagen.tags;

import melonslise.locks.common.init.LocksEnchantmentTags;
import melonslise.locks.common.init.LocksEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LocksEnchantmentTagsGen extends FabricTagProvider.EnchantmentTagProvider {
    public LocksEnchantmentTagsGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(LocksEnchantmentTags.ON_LOCKS)
                .add(LocksEnchantments.COMPLEXITY)
                .add(LocksEnchantments.SHOCKING)
                .add(LocksEnchantments.STURDY);
    }
}
