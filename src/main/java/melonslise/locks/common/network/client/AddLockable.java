//package melonslise.locks.common.network.client;
//
//import io.wispforest.owo.network.ClientAccess;
//import melonslise.locks.common.init.LocksComponents;
//import melonslise.locks.common.util.Lockable;
//
//public record AddLockable(Lockable lkb) {
//    public static void handler(AddLockable packet, ClientAccess clientAccess) {
//        throw new RuntimeException();
////        LocksComponents.LOCKABLE_HANDLER.get(clientAccess.player()).add(packet.lkb,clientAccess.player().level());
//    }
//}