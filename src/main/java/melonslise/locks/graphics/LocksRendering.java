package melonslise.locks.graphics;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.util.LocksClientUtil;
import melonslise.locks.components.AbstractLocked;
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
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionf;

public class LocksRendering {
    public static void renderLock(AbstractLocked locked, int light, PoseStack stack, MultiBufferSource buf) {
        var mc = Minecraft.getInstance();
        var item = locked.getLock();
        var ft = mc.getFrameTimer();
        stack.pushPose();
        stack.scale(0.5f, 0.5f, 0.5f);
        stack.translate(0d, 0.25d, 0d);
        if (locked.maxSwingTicks != 0 && locked.swingTicks != 0) {
            var animValue = AbstractLocked.swingAnim(org.joml.Math.max(locked.maxSwingTicks - mc.player.tickCount - mc.getFrameTime(), 0f) / (float)locked.swingTicks);
            stack.mulPose(new Quaternionf().rotateZ(Mth.HALF_PI*animValue/3));
        }
        stack.translate(0d, -0.25d, 0d);
        mc.getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buf, mc.level, 0);
        stack.popPose();
    }

    public static void renderLockGeneric(AbstractLocked locked, int light, Direction dir, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        renderLockGeneric(locked, light, dir, new AABB(0f, 0f, 0f, 1f, 1f, 1f), poseStack, multiBufferSource);
    }

    public static void renderLockGeneric(AbstractLocked locked, int light, Direction dir, AABB aabb, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        poseStack.pushPose();
        //poseStack.translate(0.5f, 0.35f, 0.5f);

        poseStack.translate(aabb.getXsize()/2, aabb.getYsize()/2,aabb.getZsize()/2);
        poseStack.translate( dir.getStepX()*0.5, dir.getStepY()*0.5,dir.getStepZ()*0.5);
        poseStack.mulPose(dir.getRotation()
                .rotateAxis(-(float)Math.PI/2, 1f, 0f, 0f)
                .rotateAxis((float)Math.PI, 0f, 1f, 0f));

        renderLock(locked, light, poseStack, multiBufferSource);
        poseStack.popPose();
    }

    public static void renderLockOnChest(ChestBlockEntity chest, AbstractLocked locked, int light, PoseStack stack, MultiBufferSource buf) {
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
