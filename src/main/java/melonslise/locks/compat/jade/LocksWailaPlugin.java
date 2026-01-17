package melonslise.locks.compat.jade;

import melonslise.locks.Locks;
import melonslise.locks.components.AbstractLocked;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class LocksWailaPlugin implements IWailaPlugin {
    public static ResourceLocation LOCK = new ResourceLocation(Locks.ID, "lock");
    public static ResourceLocation SHOW_INFO = new ResourceLocation(Locks.ID, "lock.show_info");
    public static ResourceLocation ALWAYS_SHOW_INFO = new ResourceLocation(Locks.ID, "lock.always_show_info");
    public static ResourceLocation LOCK_PICKING_TOOL = new ResourceLocation(Locks.ID, "lock_picking_tool");
    public static ResourceLocation LOCK_PICKING_TOOL_CREATIVE = new ResourceLocation(Locks.ID, "lock_picking_tool.creative");
    @Override
    public void register(IWailaCommonRegistration registration) {
        //TODO register data providers
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(LockPickingToolProvider.INSTANCE, Block.class);
        registration.addConfig(LOCK_PICKING_TOOL_CREATIVE, false);
        registration.registerBlockComponent(LockComponentProvider.INSTANCE, Block.class);
        registration.addConfig(SHOW_INFO, true);
        registration.addConfig(ALWAYS_SHOW_INFO, false);

        registration.markAsClientFeature(LOCK_PICKING_TOOL);
        registration.markAsClientFeature(LOCK_PICKING_TOOL_CREATIVE);
        registration.markAsClientFeature(LOCK);
        registration.markAsClientFeature(SHOW_INFO);
        registration.markAsClientFeature(ALWAYS_SHOW_INFO);
    }
}
