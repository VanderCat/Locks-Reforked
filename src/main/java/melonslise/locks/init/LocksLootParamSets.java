package melonslise.locks.init;

import melonslise.locks.Locks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class LocksLootParamSets {
    public static final LootContextParamSet LOCK = LootContextParamSets.register(Locks.ID+ ":lock", (builder) -> {
      builder.required(LootContextParams.BLOCK_STATE).optional(LootContextParams.ORIGIN).optional(LootContextParams.BLOCK_ENTITY);
    });

    public static void register() {}
}
