package melonslise.locks.network.server;

import io.wispforest.owo.network.ServerAccess;
import melonslise.locks.container.LockPickingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record TryPin(int pin) {
    public static void handler(TryPin packet, ServerAccess serverAccess) {
        AbstractContainerMenu container = serverAccess.player().containerMenu;
        if (container instanceof LockPickingContainer lockPicking)
            lockPicking.tryPin(packet.pin);
    }
}