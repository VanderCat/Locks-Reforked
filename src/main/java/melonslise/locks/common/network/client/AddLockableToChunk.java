//package melonslise.locks.common.network.client;
//
//import io.wispforest.owo.network.ClientAccess;
//import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
//import melonslise.locks.common.init.LocksComponents;
//import net.minecraft.world.level.ChunkPos;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.chunk.EmptyLevelChunk;
//
//public record AddLockableToChunk(Lockable lkb, ChunkPos pos) {
//    public static void handler(AddLockableToChunk packet, ClientAccess clientAccess) {
//        throw new RuntimeException();
////        Level level = clientAccess.player().level();
////
////        if(level.getChunk(packet.pos.x, packet.pos.z) instanceof EmptyLevelChunk) return;
////
////        //  Gets storage units within the chunk.
////        ILockableStorage st = LocksComponents.LOCKABLE_STORAGE.get(level.getChunk(packet.pos.x, packet.pos.z));
////        ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(level);
////        Int2ObjectMap<Lockable> lkbs = handler.getLoaded();
////        Lockable lkb = lkbs.get(packet.lkb.id);
////        if (lkb == lkbs.defaultReturnValue()) {
////            lkb = packet.lkb;
////            lkb.addObserver(handler);
////            lkbs.put(lkb.id, lkb);
////        }
////        st.add(lkb);
//    }
//}