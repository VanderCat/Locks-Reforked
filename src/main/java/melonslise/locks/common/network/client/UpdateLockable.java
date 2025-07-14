package melonslise.locks.common.network.client;

import io.wispforest.owo.network.ClientAccess;
import melonslise.locks.common.init.LocksComponents;

public record UpdateLockable(int id, boolean locked) {
    public static void handler(UpdateLockable packet, ClientAccess clientAccess) {
        throw new RuntimeException();
//        LocksComponents.LOCKABLE_HANDLER.get(clientAccess.player().level()).getLoaded().get(packet.id).lock.setLocked(packet.locked);
    }
}