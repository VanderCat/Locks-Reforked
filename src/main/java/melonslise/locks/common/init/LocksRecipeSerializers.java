package melonslise.locks.common.init;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import melonslise.locks.Locks;
import melonslise.locks.common.recipe.KeyRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;

public final class LocksRecipeSerializers implements AutoRegistryContainer<RecipeSerializer<?>> {
	public static final RecipeSerializer<KeyRecipe> CRAFTING_KEY = new SimpleCraftingRecipeSerializer<>(KeyRecipe::new);

	@Override
	public Registry<RecipeSerializer<?>> getRegistry() {
		return BuiltInRegistries.RECIPE_SERIALIZER;
	}

	@SuppressWarnings("unchecked")
    @Override
	public Class<RecipeSerializer<?>> getTargetFieldType() {
		return (Class<RecipeSerializer<?>>) (Object) RecipeSerializer.class;
	}
}