package melonslise.locks.compat.rei;

import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import melonslise.locks.common.init.LocksItemTags;
import melonslise.locks.common.init.LocksItems;
import net.fabricmc.loader.api.FabricLoader;

import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import me.shedaniel.rei.plugin.common.displays.anvil.AnvilRecipe;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class REIIntegration {
    public static boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded("rei");
    }

    public static void registerDisplays(DisplayRegistry registry) {
        ItemStack leftStack = new ItemStack(LocksItems.NETHERITE_LOCK);

        AnvilRecipe recipe2 = new AnvilRecipe(
                new ResourceLocation("locks", "copy_lock"),
                Arrays.asList(Ingredient.of(LocksItemTags.LOCKS).getItems()),
                Arrays.asList(Ingredient.of(LocksItemTags.LOCKS).getItems()),
                Arrays.asList(Ingredient.of(LocksItemTags.LOCKS).getItems())
        );

        DefaultCustomShapelessDisplay recipe3 = new DefaultCustomShapelessDisplay(
            null, // Optional Recipe<?> reference, can be null
            List.of(
                    EntryIngredients.ofTag(LocksItemTags.LOCKS, (Holder<Item> holder) -> EntryStacks.of(holder.value()) ),
                    EntryIngredients.ofItemStacks(List.of(new ItemStack(LocksItems.KEY_BLANK)))
            ),
            List.of(
                    EntryIngredients.ofItemStacks(List.of(new ItemStack(LocksItems.KEY)))
            )
        );

        registry.add(recipe2);
        registry.add(recipe3);
    }
}
