package melonslise.locks.events;

import melonslise.locks.components.Locked;
import melonslise.locks.init.LocksComponents;
import melonslise.locks.init.LocksItemTags;
import melonslise.locks.graphics.LocksRendering;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

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
            var blockentity = level.getBlockEntity(blockpos);
            if (blockentity == null)
                return;
            var locked = Locked.getFrom(blockentity);

            var item = locked.getLock();
            if (item.isEmpty())
                return;
            var w = guiGraphics.guiWidth();
            var h = guiGraphics.guiHeight();
            var x = w/2;
            var y = h/2;
            guiGraphics.renderTooltip(mc.font, item, x, y);
        });
    }
}
