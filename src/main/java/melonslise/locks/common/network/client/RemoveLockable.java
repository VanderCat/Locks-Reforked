//package melonslise.locks.common.network.client;
//
//import io.wispforest.owo.network.ClientAccess;
//import melonslise.locks.common.init.LocksComponents;
//import melonslise.locks.common.util.Lockable;
//
//public record RemoveLockable(Lockable lkb) {
//    public static void handler(RemoveLockable packet, ClientAccess clientAccess) {
//        throw new RuntimeException();
////        LocksComponents.LOCKABLE_HANDLER.get(clientAccess.player()).remove(packet.lkb.id);
//    }
//}