package melonslise.locks.graphics;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.util.LocksClientUtil;
import melonslise.locks.components.Locked;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.joml.Quaternionf;

public class LocksRendering {
    public static void renderLock(Locked locked, int light, PoseStack stack, MultiBufferSource buf) {
        var mc = Minecraft.getInstance();
        var item = locked.getLock();
        stack.pushPose();
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.translate(0d, 0.25d, 0d);
        stack.mulPose(new Quaternionf().rotateZ(Mth.sin(LocksClientUtil.cubicBezier1d(1f, 1f, LocksClientUtil.lerp(locked.maxSwingTicks - locked.oldSwingTicks, locked.maxSwingTicks - locked.swingTicks, 0) / locked.maxSwingTicks) * locked.maxSwingTicks / 5f * 3.14f) * 0.4f));
        stack.translate(0d, -0.25d, 0d);
        mc.getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buf, mc.level, 0);
        stack.popPose();
    }

    public static void renderLockGeneric(Locked locked, int light, Direction dir, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.35f, 0.5f);
        poseStack.translate( dir.getStepX()*0.5, dir.getStepY()*0.5,dir.getStepZ()*0.5);
        poseStack.mulPose(dir.getRotation()
                .rotateAxis(-(float)Math.PI/2, 1f, 0f, 0f)
                .rotateAxis((float)Math.PI, 0f, 1f, 0f));

        renderLock(locked, light, poseStack, multiBufferSource);
        poseStack.popPose();
    }

    public static void renderLockOnChest(ChestBlockEntity chest, Locked locked, int light, PoseStack stack, MultiBufferSource buf) {
        stack.pushPose();
        var bs = chest.getBlockState();
        var dir = ChestBlock.getConnectedDirection(bs);
        var bt = ChestBlock.getBlockType(bs);
        stack.translate(0.5f, 0.35f, 0.5f);
        //stack.translate( dir.getStepZ()*0.5, 0f,dir.getStepX()*0.5);
        var rot = dir.getRotation()
                .rotateAxis(-(float)Math.PI/2, 1f, 0f, 0f);
        switch (bt) {
            case SINGLE -> stack.mulPose(rot.rotateAxis((float)Math.PI/2, 0f, 1f, 0f));
            case FIRST -> {
                stack.mulPose(rot.rotateAxis((float)Math.PI/2, 0f, 1f, 0f));
                stack.translate(-0.5f, 0f, 0f);
            }
            case SECOND -> {
                stack.mulPose(rot.rotateAxis((float)-Math.PI/2, 0f, 1f, 0f));
                stack.translate(0.5f, 0f, 0f);
            }
        }

        stack.translate( 0f, 0f,-0.5f);
        renderLock(locked, light, stack, buf);
        stack.popPose();
    }
}
