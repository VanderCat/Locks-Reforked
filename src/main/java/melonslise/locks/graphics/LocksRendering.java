package melonslise.locks.graphics;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.util.LocksClientUtil;
import melonslise.locks.common.components.Locked;
import melonslise.locks.common.init.LocksComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.joml.Quaternionf;

public class LocksRendering {
    public static void renderLock(Locked locked, Direction direction, int light, PoseStack stack, MultiBufferSource buf) {
        var mc = Minecraft.getInstance();
        var item = locked.getLock();
        stack.pushPose();
        stack.translate(0.5f, 0.35f, 0.5f);
        stack.translate( direction.getStepX()*0.5, direction.getStepY()*0.5,direction.getStepZ()*0.5);
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.mulPose(direction.getRotation()
                .rotateAxis(-(float)Math.PI/2, 1f, 0f, 0f)
                .rotateAxis((float)Math.PI, 0f, 1f, 0f));
        stack.translate(0d, 0.25d, 0d);
        stack.mulPose(new Quaternionf().rotateZ(Mth.sin(LocksClientUtil.cubicBezier1d(1f, 1f, LocksClientUtil.lerp(locked.maxSwingTicks - locked.oldSwingTicks, locked.maxSwingTicks - locked.swingTicks, 0) / locked.maxSwingTicks) * locked.maxSwingTicks / 5f * 3.14f) * 0.4f));
        stack.translate(0d, -0.25d, 0d);
        mc.getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buf, mc.level, 0);
        stack.popPose();
    }

//    public static void renderLockOnChest(ChestBlockEntity chest, int light, ItemStack lock, PoseStack stack, MultiBufferSource buf) {
//
//        renderLock(lock, light, stack, buf);
//
//    }
}
