package melonslise.locks.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;
import melonslise.locks.Locks;

@Modmenu(modId = Locks.ID)
@Config(name = "locks", wrapperName = "LocksConfig")
public class LocksConfigModel {
    @SectionHeader("client")
    public boolean deafMode = true;
    public boolean overlay = true;
    @SectionHeader("generation")
    public boolean randomizeLoadedLocks = true;
    @SectionHeader("server")
    @RangeConstraint(min = 1, max = 128)
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public int maxLockableVolume = 6;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean allowRemovingLocks = true;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean protectLockables = true;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean strongPrevention = false;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean hideIdsFromClient = false;
}