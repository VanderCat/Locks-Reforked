package melonslise.locks.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import melonslise.locks.Locks;
import melonslise.locks.events.LocksEvents;
import melonslise.locks.init.LocksComponents;
import melonslise.locks.item.LockItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractLocked implements Component, AutoSyncedComponent, ClientTickingComponent {

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


    public static AbstractLocked getFrom(Level level, BlockPos pos) {
        var be = level.getBlockEntity(pos);
        if (be == null)
            if (Locks.CONFIG.nonBlockEntityLocking())
                return ChunkLocked.getFrom(level, pos);
            else
                return null;
        return Locked.getFrom(be);

    };

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void removeLock() {
        setLock(ItemStack.EMPTY);
    }

    public Boolean isOpen() {
        return LockItem.isOpen(lock);
    }

    public ItemStack getLock() {
        return lock;
    }

    public int maxSwingTicks, oldSwingTicks, swingTicks;

    public void swing(int ticks) {
        var player = Minecraft.getInstance().player;
        if (player != null)
            maxSwingTicks = player.tickCount+ticks;
        swingTicks = ticks;
        //  /\      _
        //_/  \    / \  /\_
        //     \  /   \/
        //      \/
    }

    public static float swingAnim(float x) {
        return (float)Math.sin((x*Mth.PI*8f)+(1-(Math.pow((x+0.5f)*2f,2)*(-2.2)+3.2)))*(0-Mth.abs(Mth.sin(x*Mth.HALF_PI+ Mth.PI+Mth.HALF_PI))+1);
    }

    @Override
    public void clientTick() {

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

    public abstract void sync();
}
