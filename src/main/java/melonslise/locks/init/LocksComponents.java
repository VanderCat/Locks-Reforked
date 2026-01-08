package melonslise.locks.init;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import melonslise.locks.Locks;
import melonslise.locks.components.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LocksComponents implements BlockComponentInitializer {

    public static final ComponentKey<Locked> LOCKED =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID, "locked"), Locked.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(BlockEntity.class, LOCKED, Locked::new);
    }
}
