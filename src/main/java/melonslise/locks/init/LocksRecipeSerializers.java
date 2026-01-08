package melonslise.locks.init;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import melonslise.locks.recipe.KeyRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

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