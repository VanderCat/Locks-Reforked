package melonslise.locks.datagen.recipes;

import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class LocksRecipesGen extends FabricRecipeProvider {
    public LocksRecipesGen(FabricDataOutput output) {
        super(output);
    }

    public static ShapedRecipeBuilder lock(ItemLike item) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item)
                .pattern(" # ")
                .pattern("#@#")
                .pattern("###")
                .group("locks");
    }

    public static ShapedRecipeBuilder lockpick(ItemLike item) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item, 4)
                .pattern(" ##")
                .pattern(" # ")
                .pattern("#  ")
                .group("lockpicks");
    }

    public static ShapedRecipeBuilder mechanism(ItemLike item) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item)
                .group("lock_mechanisms");
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        lock(LocksItems.WOOD_LOCK)
            .define('#', ItemTags.PLANKS)
            .define('@', LocksItems.WOOD_LOCK_MECHANISM)
                .unlockedBy("has_string", has(Items.STRING))
            .save(exporter);

        lock(LocksItems.IRON_LOCK)
            .define('#', Items.IRON_INGOT)
            .define('@', LocksItems.IRON_LOCK_MECHANISM)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .save(exporter);

        lock(LocksItems.GOLD_LOCK)
            .define('#', Items.IRON_INGOT)
            .define('@', LocksItems.STEEL_LOCK_MECHANISM)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
            .save(exporter);

        lock(LocksItems.DIAMOND_LOCK)
            .define('#', Items.IRON_INGOT)
            .define('@', LocksItems.STEEL_LOCK_MECHANISM)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
            .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LocksItems.WOOD_LOCK_PICK, 1)
            .pattern(" # ")
            .pattern("# #")
            .pattern("## ")
            .group("lockpicks")
            .define('#', Items.STICK)
                .unlockedBy("has_log", has(ItemTags.LOGS))
            .save(exporter);

        lockpick(LocksItems.IRON_LOCK_PICK)
            .define('#', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .save(exporter);

        lockpick(LocksItems.GOLD_LOCK_PICK)
            .define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(exporter);

        lockpick(LocksItems.DIAMOND_LOCK_PICK)
            .define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(exporter);

        mechanism(LocksItems.WOOD_LOCK_MECHANISM)
            .pattern("III")
            .pattern("SSS")
            .pattern("###")
            .define('I', Items.STICK)
            .define('S', Items.STRING)
            .define('#', ItemTags.PLANKS)
                .unlockedBy("has_string", has(Items.STRING))
            .save(exporter);

        mechanism(LocksItems.IRON_LOCK_MECHANISM)
            .pattern("SSS")
            .pattern("...")
            .pattern("___")
            .define('S', LocksItems.SPRING)
            .define('.', Items.IRON_NUGGET)
            .define('_', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LocksItems.KEY_BLANK, 4)
            .pattern("nn ")
            .pattern("nn ")
            .pattern("I  ")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .define('n', Items.IRON_NUGGET)
            .define('I', Items.IRON_INGOT)

            .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LocksItems.SPRING, 4)
            .pattern(" n ")
            .pattern(" n ")
            .pattern(" n ")
            .define('n', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LocksItems.KEY_RING, 4)
                .pattern(" n ")
                .pattern("n n")
                .pattern(" n ")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .define('n', Items.IRON_NUGGET)
                .save(exporter);

        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(LocksItems.DIAMOND_LOCK),
            Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.MISC, LocksItems.NETHERITE_LOCK)
                .unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(exporter, new ResourceLocation(Locks.ID, "netherite_lock"));

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(LocksItems.DIAMOND_LOCK_PICK),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.MISC, LocksItems.NETHERITE_LOCK_PICK)
                .unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(exporter, new ResourceLocation(Locks.ID, "netherite_lock_pick"));


//        mechanism(LocksItems.STEEL_LOCK_MECHANISM)
//                .pattern("SSS")
//                .pattern("...")
//                .pattern("___")
//                .define('S', LocksItems.SPRING)
//                .define('.', Items.IRON_NUGGET)
//                .define('_', Items.IRON_INGOT)
//                .save(exporter);

        //TODO: Investigate
//        lock(LocksItems.STEEL_LOCK)
//                .define('#',)
//                .define('@', LocksItems.STEEL_LOCK_MECHANISM)
//                .save(exporter);
    }
}
