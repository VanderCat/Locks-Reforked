package melonslise.locks.mixin;

import melonslise.locks.common.init.LocksComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class OnLockedRemoveMixin {
    @Inject(at = @At(value = "HEAD"), method = "onRemove(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V")
    private void dropLock(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        if (blockState.hasBlockEntity() && !blockState.is(blockState2.getBlock())) {
            var be = level.getBlockEntity(blockPos);
            if (be == null)
                return;
            var locked = be.getComponent(LocksComponents.LOCKED);
            var item = locked.getLock();
            if (item.isEmpty())
                return;
            Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), item);
        }
    }
}
