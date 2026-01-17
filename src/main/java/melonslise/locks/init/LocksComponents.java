package melonslise.locks.init;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.block.BlockComponents;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentFactory;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import melonslise.locks.Locks;
import melonslise.locks.components.*;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.intellij.lang.annotations.Identifier;

public class LocksComponents implements BlockComponentInitializer, ChunkComponentInitializer {

    public static final ComponentKey<Locked> LOCKED =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID, "locked"), Locked.class);
    public static final ComponentKey<ChunkLocked> CHUNK_LOCKED =
            ComponentRegistry.getOrCreate(new ResourceLocation("locks", "chunk_locked"), ChunkLocked.class);
    public static final BlockApiLookup<Locked,Object> LOCKED_LOOKUP =
            BlockApiLookup.get(new ResourceLocation(Locks.ID, "locked"), Locked.class, Object.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(BlockEntity.class, LOCKED, Locked::new);

    }

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
        registry.register(CHUNK_LOCKED, ChunkLocked::new);
    }
}
