package melonslise.locks.init;

import melonslise.locks.Locks;
import melonslise.locks.worldgen.ScanForTagModifier;
import melonslise.locks.worldgen.feature.LocksFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;


public class LocksFeatures {
    public static final LocksFeature LOCK = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Locks.ID, "lock"), new LocksFeature(NoneFeatureConfiguration.CODEC));
    public static final PlacementModifierType<ScanForTagModifier> SCAN_FOR_TAG_MODIFIER = Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
            new ResourceLocation(Locks.ID, "scan_for_tag"),
            () -> ScanForTagModifier.CODEC);
    public static final void register() {
        BiomeModifications.addFeature(
                BiomeSelectors.all(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Locks.ID, "lock")));
    }
}
