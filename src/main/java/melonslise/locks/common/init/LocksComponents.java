package melonslise.locks.common.init;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import melonslise.locks.Locks;
import melonslise.locks.common.components.*;
import melonslise.locks.common.components.interfaces.IItemHandler;
import melonslise.locks.common.components.interfaces.ISelection;
import melonslise.locks.common.item.LockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LocksComponents implements
//        EntityComponentInitializer,
//        WorldComponentInitializer,
//        ChunkComponentInitializer,
//        ItemComponentInitializer,
        BlockComponentInitializer {

//    public static final ComponentKey<ILockableHandler> LOCKABLE_HANDLER =
//            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"lockable_handler"),ILockableHandler.class);
//
//    public static final ComponentKey<ILockableStorage> LOCKABLE_STORAGE =
//            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"lockable_storage"), ILockableStorage.class);
//
//    public static final ComponentKey<ISelection> SELECTION =
//            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"selection"), ISelection.class);
//
//    public static final ComponentKey<IItemHandler> ITEM_HANDLER =
//            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"item_handler"), IItemHandler.class);

    public static final ComponentKey<Locked> LOCKED =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID, "locked"), Locked.class);

//    @Override
//    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
////        registry.register(LOCKABLE_STORAGE, LockableStorage::new);
//    }
//
//    @Override
//    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
////        registry.registerForPlayers(SELECTION, (player) -> new Selection(player.getOnPos()), RespawnCopyStrategy.ALWAYS_COPY);
//    }
//
//    @Override
//    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
////        registry.register(LOCKABLE_HANDLER, LockableHandler::new);
//    }
//
//    @Override
//    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
////        registry.registerTransient(item -> item instanceof LockItem,ITEM_HANDLER, (stack) -> new ItemHandler());
////        registry.registerTransient(LocksItems.KEY_RING,ITEM_HANDLER, (stack) -> new ItemHandler());
//    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(BlockEntity.class, LOCKED, Locked::new);
    }
}
