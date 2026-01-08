package melonslise.locks.events;

import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.graphics.LocksRendering;

public class LocksRenderEvents {
    public static void register() {
        BlockEntityRenderEvent.POST.register((blockEntityRenderer, blockEntity, light, f, poseStack, multiBufferSource) -> {
            var locked = blockEntity.getComponent(LocksComponents.LOCKED);
            var lock = locked.getLock();
            var dir = locked.getDirection();
            if (!lock.isEmpty())
                LocksRendering.renderLock(locked, dir, light, poseStack, multiBufferSource);
        });
//        HudRenderCallback.EVENT.register((guiGraphics, v) -> {
//            var mc = Minecraft.getInstance();
//            var player = mc.player;
//            player.look
//            guiGraphics.renderTooltip(mc.font, );
//        });
    }
}
