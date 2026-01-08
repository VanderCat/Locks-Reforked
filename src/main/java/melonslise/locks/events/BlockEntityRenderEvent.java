package melonslise.locks.events;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface BlockEntityRenderEvent {
    Event<BlockEntityRenderEvent> POST = EventFactory.createArrayBacked(BlockEntityRenderEvent.class,
            (listeners) -> (blockEntityRenderer, blockEntity, light, f, poseStack, multiBufferSource) -> {
                for (BlockEntityRenderEvent listener : listeners)
                    listener.render(blockEntityRenderer, blockEntity, light, f, poseStack, multiBufferSource);
            });

    void render(BlockEntityRenderer blockEntityRenderer, BlockEntity blockEntity, int light, float f, PoseStack poseStack, MultiBufferSource multiBufferSource);
}
