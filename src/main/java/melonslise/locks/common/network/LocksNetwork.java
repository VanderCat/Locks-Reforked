package melonslise.locks.common.network;

import io.wispforest.owo.network.OwoNetChannel;
import melonslise.locks.Locks;
import melonslise.locks.common.network.client.*;
import melonslise.locks.common.network.server.TryPin;
import net.minecraft.resources.ResourceLocation;

public class LocksNetwork {
    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(new ResourceLocation(Locks.ID, "main"));

    public static final void registerClientbound() {
//        CHANNEL.registerClientbound(AddLockable.class, AddLockable::handler);
//        CHANNEL.registerClientbound(AddLockableToChunk.class, AddLockableToChunk::handler);
//        CHANNEL.registerClientbound(RemoveLockable.class, RemoveLockable::handler);
        CHANNEL.registerClientbound(TryPinResult.class, TryPinResult::handler);
//        CHANNEL.registerClientbound(UpdateLockable.class, UpdateLockable::handler);
    }

    public static final void registerServerbound() {
        CHANNEL.registerServerbound(TryPin.class, TryPin::handler);
    }
}
