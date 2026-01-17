package melonslise.locks.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonslise.locks.init.LocksBlockTags;
import melonslise.locks.init.LocksFeatures;
import melonslise.locks.worldgen.feature.LocksFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class ScanForTagModifier extends PlacementModifier {

    public static final Codec<ScanForTagModifier> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(ScanForTagModifier::getTag)
            ).apply(instance, ScanForTagModifier::new)
    );

    private final TagKey<Block> _tag;
    public TagKey<Block> getTag() { return this._tag; }

    public ScanForTagModifier(TagKey<Block> tag) {
        _tag = tag;
    }


    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource randomSource, BlockPos pos) {
        Stream.Builder<BlockPos> builder = Stream.builder();

        int minY = context.getLevel().getMinBuildHeight();
        int maxY = context.getLevel().getMaxBuildHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos currentPos = pos.offset(x, y, z);
                    if (context.getBlockState(currentPos).is(getTag()))
                        builder.add(currentPos);
                }
            }
        }

        return builder.build();
    }

    @Override
    public PlacementModifierType<?> type() {
        return LocksFeatures.SCAN_FOR_TAG_MODIFIER;
    }
}