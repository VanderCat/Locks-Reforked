package melonslise.locks.events;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.config.LocksConfig;
import melonslise.locks.init.LocksComponents;
import melonslise.locks.init.LocksItemTags;
import melonslise.locks.graphics.LocksRendering;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.concurrent.locks.Lock;

public class LocksRenderEvents {
    public static void register() {
        BlockEntityRenderEvent.POST.register((blockEntityRenderer, blockEntity, light, f, poseStack, multiBufferSource) -> {
            var locked = blockEntity.getComponent(LocksComponents.LOCKED);
            var lock = locked.getLock();
            var dir = locked.getDirection();
            if (!lock.isEmpty()) {
                if (blockEntity instanceof ChestBlockEntity cbe) {
                    LocksRendering.renderLockOnChest(cbe, locked, light, poseStack, multiBufferSource);
                    return;
                }
                LocksRendering.renderLockGeneric(locked, light, dir, poseStack, multiBufferSource);
            }
        });
        HudRenderCallback.EVENT.register((guiGraphics, v) -> {
            if (!Locks.CONFIG.overlay()) return;
            var mc = Minecraft.getInstance();
            var level = mc.level;
            var player = mc.player;
            var mainitem = player.getMainHandItem();
            if (!(
                mainitem.is(LocksItemTags.KEYS) ||
                mainitem.is(LocksItemTags.LOCK_PICKS)
            ))
                return;
            var hit = mc.hitResult;
            if (hit == null)
                return;
            if (hit.getType() != HitResult.Type.BLOCK)
                return;
            var blockhit = (BlockHitResult)hit;
            var blockpos = blockhit.getBlockPos();
            var locked = AbstractLocked.getFrom(player.level(), blockpos);
            if (locked == null)
                return;
            var item = locked.getLock();
            if (item.isEmpty())
                return;
            var w = guiGraphics.guiWidth();
            var h = guiGraphics.guiHeight();
            var x = w/2;
            var y = h/2;
            guiGraphics.renderTooltip(mc.font, item, x, y);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(worldRenderContext -> {
            if (!Locks.CONFIG.nonBlockEntityLocking()) return;
            var mc = Minecraft.getInstance();
            var cam = worldRenderContext.camera();
            var camPos = cam.getPosition();
            var stack = worldRenderContext.matrixStack();
            for (var ri : worldRenderContext.worldRenderer().renderChunksInFrustum) {
                var chunk = worldRenderContext.world().getChunk(ri.chunk.getOrigin());
                if (chunk instanceof EmptyLevelChunk)
                    continue;
                var chunkLocked = chunk.getComponent(LocksComponents.CHUNK_LOCKED);
                var lockeds = chunkLocked.getAll();
                lockeds.forEach((blockPos, abstractLocked) -> {
                    //var matrix = stack.last().pose();
                    stack.pushPose();
                    //Locks.LOGGER.debug(stack.\);
                    //stack.mulPoseMatrix(matrix.invert());

                    stack.translate(-camPos.x, -camPos.y, -camPos.z);
                    stack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    LocksRendering.renderLockGeneric(abstractLocked, worldRenderContext.world().getLightEmission(blockPos), abstractLocked.getDirection(), worldRenderContext.world().getBlockState(blockPos).getShape(chunk, blockPos).bounds(), stack, worldRenderContext.consumers());
                    stack.popPose();
                });
            }
        });
    }
}
