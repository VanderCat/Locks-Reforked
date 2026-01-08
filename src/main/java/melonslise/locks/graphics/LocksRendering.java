package melonslise.locks.graphics;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.common.init.LocksComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class LocksRendering {
    public static void renderLock(ItemStack lock, Direction direction, int light, PoseStack stack, MultiBufferSource buf) {
        var mc = Minecraft.getInstance();
        stack.pushPose();
        stack.translate(0.5f, 0.35f, 0.5f);
        stack.translate( direction.getStepX()*0.5, direction.getStepY()*0.5,direction.getStepZ()*0.5);
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.mulPose(direction.getRotation()
                .rotateAxis(-(float)Math.PI/2, 1f, 0f, 0f)
                .rotateAxis((float)Math.PI, 0f, 1f, 0f));
        mc.getItemRenderer().renderStatic(lock, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buf, mc.level, 0);
        stack.popPose();
    }

//    public static void renderLockOnChest(ChestBlockEntity chest, int light, ItemStack lock, PoseStack stack, MultiBufferSource buf) {
//
//        renderLock(lock, light, stack, buf);
//
//    }
}
