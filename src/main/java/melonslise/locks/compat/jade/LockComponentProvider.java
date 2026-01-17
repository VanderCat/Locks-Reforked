package melonslise.locks.compat.jade;

import melonslise.locks.Locks;
import melonslise.locks.components.Locked;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.LinkedList;
import java.util.List;

public enum LockComponentProvider implements IBlockComponentProvider {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        var be = blockAccessor.getBlockEntity();
        if (be == null)
            return;
        var locked = Locked.getFrom(be);
        var item = locked.getLock();
        if (item.isEmpty())
            return;
        IElementHelper elements = IElementHelper.get();
        IElement icon = elements.item(item, 0.5f).size(new Vec2(10, 10)).translate(new Vec2(0, -1));
        icon.message(null);
        tooltip.add(icon);
        if (locked.isOpen())
            tooltip.append(Component.translatable(Locks.ID+".jade.unlocked"));
        else
            tooltip.append(Component.translatable(Locks.ID+".jade.locked"));
        var player = Minecraft.getInstance().player;
        if (!pluginConfig.get(LocksWailaPlugin.SHOW_INFO))
            return;
        if (!pluginConfig.get(LocksWailaPlugin.ALWAYS_SHOW_INFO) && !player.isShiftKeyDown())
            return;

        ITooltip box = elements.tooltip();

        box.add(Component.empty().append(item.getHoverName()).withStyle(item.getRarity().color));
        {
            var list = new LinkedList<Component>();
            item.getItem().appendHoverText(item, be.getLevel(), list, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
            for (var el : list) {
                box.add(el);
            }
        }
        var listTag = item.getEnchantmentTags();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundTag))
                    .ifPresent((enchantment) ->
                            box.add(enchantment.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundTag))));
        }
        tooltip.add(elements.box(box, BoxStyle.DEFAULT));
    }

    @Override
    public ResourceLocation getUid() {
        return LocksWailaPlugin.LOCK;
    }
}
