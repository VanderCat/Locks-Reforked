package melonslise.locks.mixin;

import melonslise.locks.components.Locked;
import melonslise.locks.init.LocksComponents;
import melonslise.locks.init.LocksItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class OnBlockUseMixin {
    @Inject(at = @At(value = "RETURN"), method = "use", cancellable = true)
    private void cancelUse(Level level, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockBehaviour.BlockStateBase self = (BlockBehaviour.BlockStateBase)(Object)this;
        var be = level.getBlockEntity(blockHitResult.getBlockPos());
        if (be == null)
            return;
        if (Locked.getFrom(be).getLock().isEmpty())
            return;
        var item = player.getItemInHand(interactionHand);
        if (item.isEmpty())
            return;
        if (item.is(LocksItemTags.KEYS) || item.is(LocksItemTags.LOCK_PICKS))
            cir.setReturnValue(InteractionResult.PASS);
    }
}
