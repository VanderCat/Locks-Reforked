package melonslise.locks.mixin.compat.lootr;

import com.llamalad7.mixinextras.sugar.Local;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import melonslise.locks.components.AbstractLocked;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.zestyblaze.lootr.api.LootrAPI;
import net.zestyblaze.lootr.api.blockentity.ILootBlockEntity;
import net.zestyblaze.lootr.block.entities.TileTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "net.zestyblaze.lootr.block.entities.TileTicker")
public class LootrMixin {
    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private static boolean lockLootr(ServerLevel instance, BlockPos blockPos, boolean b) {
        return b;
    }

    @Unique
    private static final ThreadLocal<CompoundTag> capturedComponents = new ThreadLocal<>();

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private static void lootr$captureComponents(CallbackInfo ci, @Local ServerLevel level, @Local TileTicker.Entry entry){
        ComponentProvider provider = (ComponentProvider) level.getBlockEntity(entry.getPosition());
        CompoundTag tag = new CompoundTag();

        // Serialize all components attached to this BlockEntity
        provider.getComponentContainer().toTag(tag);

        capturedComponents.set(tag);
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/RandomizableContainerBlockEntity;setLootTable(Lnet/minecraft/resources/ResourceLocation;J)V"))
    private static void lootr$pasteComponents(CallbackInfo ci, @Local BlockEntity blockEntity) {
        CompoundTag tag = capturedComponents.get();

        if (tag != null && blockEntity != null) {
            ComponentProvider provider = (ComponentProvider) blockEntity;

            provider.getComponentContainer().fromTag(tag);

            capturedComponents.remove();
        }
    }
}
