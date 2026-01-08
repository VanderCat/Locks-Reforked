package melonslise.locks.network;

import io.wispforest.owo.network.OwoNetChannel;
import melonslise.locks.Locks;
import melonslise.locks.network.client.*;
import melonslise.locks.network.server.TryPin;
import net.minecraft.resources.ResourceLocation;

public class LocksNetwork {
    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(new ResourceLocation(Locks.ID, "main"));

    public static final void registerClientbound() {
        CHANNEL.registerClientbound(TryPinResult.class, TryPinResult::handler);
    }

    public static final void registerServerbound() {
        CHANNEL.registerServerbound(TryPin.class, TryPin::handler);
    }
}
