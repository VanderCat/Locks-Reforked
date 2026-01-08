package melonslise.locks.datagen.tags;

import melonslise.locks.init.LocksItemTags;
import melonslise.locks.init.LocksItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LocksItemTagsGen extends FabricTagProvider.ItemTagProvider {

    public LocksItemTagsGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

//    private static final TagKey<Item> LOCKS = TagKey.create(Registries.ITEM, new ResourceLocation("mymod:smelly_items"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(LocksItemTags.LOCKS)
                .add(LocksItems.COPPER_LOCK)
                .add(LocksItems.IRON_LOCK)
                .add(LocksItems.GOLD_LOCK)
                .add(LocksItems.DIAMOND_LOCK)
                .add(LocksItems.NETHERITE_LOCK);

        getOrCreateTagBuilder(LocksItemTags.LOCK_PICKS)
                .add(LocksItems.COPPER_LOCK_PICK)
                .add(LocksItems.IRON_LOCK_PICK)
                .add(LocksItems.GOLD_LOCK_PICK)
                .add(LocksItems.DIAMOND_LOCK_PICK)
                .add(LocksItems.NETHERITE_LOCK_PICK);

        getOrCreateTagBuilder(LocksItemTags.KEYS)
                .add(LocksItems.KEY);
    }
}
