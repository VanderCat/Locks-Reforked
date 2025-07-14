package melonslise.locks.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.item.LockingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class Locked implements Component, AutoSyncedComponent {

    protected ItemStack lock = ItemStack.EMPTY;

    /**
     * Set lock on block
     * @apiNote Don't forget to sync!
     * @param lock ItemStack of LockingItem
     */
    public void setLock(ItemStack lock) {
        this.lock = lock;
    }

    public void removeLock() {
        setLock(ItemStack.EMPTY);
    }

    public void sync() {
        LocksComponents.LOCKED.sync(provider);
    }

    private final BlockEntity provider;

    public Locked(BlockEntity blockEntity) {
        this.provider = blockEntity;
    }

    public ItemStack getLock() {
        return lock;
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        var tag = compoundTag.getCompound("lock");
        this.lock = ItemStack.of(tag);
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        compoundTag.put("lock", lock.save(new CompoundTag()));
    }
}
