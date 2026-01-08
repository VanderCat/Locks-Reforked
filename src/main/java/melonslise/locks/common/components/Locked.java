package melonslise.locks.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.item.LockItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class Locked implements Component, AutoSyncedComponent, ClientTickingComponent {

    protected ItemStack lock = ItemStack.EMPTY;
    protected Direction direction = Direction.NORTH;

    /**
     * Set lock on block
     * @apiNote Don't forget to sync!
     * @param lock ItemStack of LockingItem
     */
    public void setLock(ItemStack lock) {
        this.lock = lock;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void removeLock() {
        setLock(ItemStack.EMPTY);
    }

    public void sync() {
        LocksComponents.LOCKED.sync(provider);
        provider.setChanged();
    }

    public Boolean isOpen() {
        return LockItem.isOpen(lock);
    }

    private final BlockEntity provider;

    public Locked(BlockEntity blockEntity) {
        this.provider = blockEntity;
        direction = Direction.NORTH; //ensure
    }

    public ItemStack getLock() {
        return lock;
    }

    public int maxSwingTicks, oldSwingTicks, swingTicks;

    public void swing(int ticks) {
        maxSwingTicks = ticks;
        oldSwingTicks = maxSwingTicks;
        swingTicks = oldSwingTicks;
    }

    @Override
    public void clientTick() {
        this.oldSwingTicks = this.swingTicks;
        if(this.swingTicks > 0)
            --this.swingTicks;
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        var tag = compoundTag.getCompound("lock");
        this.lock = ItemStack.of(tag);
        var dir = Direction.byName(compoundTag.getString("direction"));
        if (dir == null)
            dir = Direction.NORTH;
        this.direction = dir;
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        compoundTag.put("lock", lock.save(new CompoundTag()));
        compoundTag.putString("direction", this.direction.getSerializedName());
    }
}
