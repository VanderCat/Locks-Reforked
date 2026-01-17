package melonslise.locks.components;

import melonslise.locks.events.LocksEvents;
import melonslise.locks.init.LocksComponents;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class Locked extends AbstractLocked {
    private final BlockEntity provider;

    public Locked(BlockEntity blockEntity) {
        this.provider = blockEntity;
        direction = Direction.NORTH; //ensure
    }

    public void sync() {
        LocksComponents.LOCKED.sync(provider);
        provider.setChanged();
    }

    public static AbstractLocked getFrom(BlockEntity ent) {
        return getFrom(ent, ent.getLevel());
    }
    public static AbstractLocked getFrom(BlockEntity ent, Level level) {
        if (ent.getBlockState().getBlock() instanceof AbstractChestBlock<?> chest) {
            var possibleLock = chest.combine(ent.getBlockState(), level, ent.getBlockPos(), true).apply(LocksEvents.LOCK_COMBINER);
            if (possibleLock.isPresent())
                return possibleLock.get();
        }
        return ent.getComponent(LocksComponents.LOCKED);
    };
}
