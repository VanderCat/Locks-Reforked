package melonslise.locks.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import melonslise.locks.Locks;
import melonslise.locks.init.LocksComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.HashMap;
import java.util.Map;

public class ChunkLocked implements Component, AutoSyncedComponent, ClientTickingComponent {
    private final Map<BlockPos, AbstractLocked> locks = new HashMap<>();
    private final ChunkAccess provider;

    public static AbstractLocked getFrom(Level level, BlockPos pos) {
        if (!Locks.CONFIG.nonBlockEntityLocking())
            throw new RuntimeException("Attempt to get ChunkLocked when disabled!");
        var chunk = level.getChunk(pos);
        var chunkLocked = chunk.getComponent(LocksComponents.CHUNK_LOCKED);
        return chunkLocked.getOrCreateLocked(pos);
    };

    public ChunkLocked(ChunkAccess chunk) {
        this.provider = chunk;
    }

    public void sync() {
        provider.setUnsaved(true);
        LocksComponents.CHUNK_LOCKED.sync(provider);
    }

    @Override
    public void clientTick() {
        for (var entry : locks.entrySet())
            entry.getValue().clientTick();
    }

    public AbstractLocked getOrCreateLocked(BlockPos pos) {
        return locks.computeIfAbsent(pos.immutable(), p -> new AbstractLocked(){
            @Override
            public void sync() {
                ChunkLocked.this.sync();
            }
        });
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        locks.clear();
        var list = tag.getList("locks", Tag.TAG_COMPOUND);
        for (var t : list) {
            var entry = (CompoundTag) t;
            var pos = new BlockPos(entry.getInt("x"), entry.getInt("y"), entry.getInt("z"));

            var locked = getOrCreateLocked(pos);
            locked.readFromNbt(entry.getCompound("data"));
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        ListTag list = new ListTag();
        for (var e : locks.entrySet()) {
            BlockPos pos = e.getKey();
            AbstractLocked locked = e.getValue();
            if (!locked.getLock().isEmpty()) {
                CompoundTag entry = new CompoundTag();
                entry.putInt("x", pos.getX());
                entry.putInt("y", pos.getY());
                entry.putInt("z", pos.getZ());

                CompoundTag data = new CompoundTag();
                locked.writeToNbt(data);
                entry.put("data", data);

                list.add(entry);
            }
        }
        tag.put("locks", list);
    }

    public Map<BlockPos, AbstractLocked> getAll() {
        return locks;
    }
}
