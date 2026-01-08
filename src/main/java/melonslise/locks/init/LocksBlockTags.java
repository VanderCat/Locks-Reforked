package melonslise.locks.init;

import melonslise.locks.Locks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LocksBlockTags {
    public static final TagKey<Block>
        LOCKABLE = bind("lockable"),
        TREASURE = bind("treasure");

    public static TagKey<Block> bind(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(Locks.ID, name));
    }
}
