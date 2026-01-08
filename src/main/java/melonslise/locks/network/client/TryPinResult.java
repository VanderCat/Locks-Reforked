package melonslise.locks.network.client;

import io.wispforest.owo.network.ClientAccess;
import melonslise.locks.container.LockPickingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record TryPinResult(boolean correct, boolean reset) {
    public static void handler(TryPinResult packet, ClientAccess clientAccess) {
        AbstractContainerMenu container = clientAccess.player().containerMenu;
        if(container instanceof LockPickingContainer lockPicking)
            lockPicking.handlePin(packet.correct, packet.reset);
    }
}