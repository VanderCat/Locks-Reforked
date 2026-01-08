package melonslise.locks.init;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import melonslise.locks.enchantment.ComplexityEnchantment;
import melonslise.locks.enchantment.ShockingEnchantment;
import melonslise.locks.enchantment.SturdyEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

public final class LocksEnchantments implements AutoRegistryContainer<Enchantment> {
	public static final Enchantment
		SHOCKING = new ShockingEnchantment(),
		STURDY = new SturdyEnchantment(),
		COMPLEXITY = new ComplexityEnchantment();

	@Override
	public Registry<Enchantment> getRegistry() {
		return BuiltInRegistries.ENCHANTMENT;
	}

	@Override
	public Class<Enchantment> getTargetFieldType() {
		return Enchantment.class;
	}
}