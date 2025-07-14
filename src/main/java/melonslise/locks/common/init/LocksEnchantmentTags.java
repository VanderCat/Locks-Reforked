package melonslise.locks.common.init;

import melonslise.locks.Locks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class LocksEnchantmentTags {
    public static TagKey<Enchantment> ON_LOCKS = bind("on_locks");

    public static TagKey<Enchantment> bind(String name) {
        return TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(Locks.ID, name));
    }
}
