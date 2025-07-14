package melonslise.locks.datagen.tags;

import melonslise.locks.common.init.LocksBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class LocksBlockTagsGen extends FabricTagProvider.BlockTagProvider {

    public LocksBlockTagsGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(LocksBlockTags.TREASURE)
                .addOptionalTag(new ResourceLocation("lootr", "containers"))
                .addOptionalTag(new ResourceLocation("c", "chests"))
                .addOptionalTag(new ResourceLocation("c", "wooden_barrels"))
                .addOptionalTag(new ResourceLocation("c", "shulker_boxes"))
                .forceAddTag(BlockTags.SHULKER_BOXES);
        getOrCreateTagBuilder(LocksBlockTags.LOCKABLE)
                .forceAddTag(LocksBlockTags.TREASURE)
                .forceAddTag(BlockTags.TRAPDOORS)
                .forceAddTag(BlockTags.DOORS)
                .forceAddTag(BlockTags.FENCE_GATES)
                .add(Blocks.HOPPER);
    }
}
