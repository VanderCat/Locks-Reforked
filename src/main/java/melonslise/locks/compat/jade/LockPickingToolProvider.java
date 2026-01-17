//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package melonslise.locks.compat.jade;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import melonslise.locks.components.AbstractLocked;
import melonslise.locks.init.LocksBlockTags;
import melonslise.locks.init.LocksItems;
import melonslise.locks.item.LockPickItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import snownee.jade.addon.harvest.SimpleToolHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IElement.Align;
import snownee.jade.impl.ui.SubTextElement;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.CommonProxy;

public enum LockPickingToolProvider implements IBlockComponentProvider, ResourceManagerReloadListener {
    INSTANCE;

    public static final Cache<ItemStack, ImmutableList<ItemStack>> resultCache = CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.MINUTES).build();
    public static final Map<String, ILockPickHandler> PICK_HANDLERS = Maps.newLinkedHashMap();
    private static final Component CHECK = Component.literal("✔");
    private static final Component X = Component.literal("✕");
    private static final Vec2 ITEM_SIZE = new Vec2(10.0F, 0.0F);

    public static ImmutableList<ItemStack> getPick(Level world, BlockPos pos) {
        ImmutableList.Builder<ItemStack> tools = ImmutableList.builder();

        for(var handler : PICK_HANDLERS.values()) {
            ItemStack tool = handler.test(world, pos);
            if (!tool.isEmpty())
                tools.add(tool);
        }

        return tools.build();
    }

    public static synchronized void registerHandler(ILockPickHandler handler) {
        PICK_HANDLERS.put(handler.getName(), handler);
    }

    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Player player = accessor.getPlayer();
        if (!config.get(LocksWailaPlugin.LOCK_PICKING_TOOL_CREATIVE) && (player.isCreative() || player.isSpectator()))
            return;

        var newLine = config.get(Identifiers.MC_HARVEST_TOOL_NEW_LINE);
        List<IElement> elements = this.getText(accessor, config);
        if (elements.isEmpty())
            return;
        elements.forEach((e) -> e.message(null));
        if (newLine) {
            tooltip.add(elements);
        } else {
            elements.forEach((e) -> e.align(Align.RIGHT));
            tooltip.append(0, elements);
        }

    }

    public List<IElement> getText(BlockAccessor accessor, IPluginConfig config) {
        var locked = AbstractLocked.getFrom(accessor.getLevel(), accessor.getPosition());
        if (locked == null)
            return List.of();
        if (locked.getLock().isEmpty())
            return List.of();

        List<ItemStack> tools = List.of();

        try {
            tools = resultCache.get(locked.getLock(), () -> getPick(accessor.getLevel(), accessor.getPosition()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (tools.isEmpty())
            return List.of();

        int offsetY = -3;
        boolean newLine = config.get(Identifiers.MC_HARVEST_TOOL_NEW_LINE);
        List<IElement> elements = Lists.newArrayList();

        for(ItemStack tool : tools) {
            elements.add(IElementHelper.get().item(tool, 0.75F).translate(new Vec2(-1.0F, (float)offsetY)).size(ITEM_SIZE).message((String)null));
        }

        if (elements.isEmpty())
            return elements;

        elements.add(0, IElementHelper.get().spacer(newLine ? -2 : 5, newLine ? 10 : 0));
        ItemStack held = accessor.getPlayer().getMainHandItem();
        if (!(held.getItem() instanceof LockPickItem))
            return elements;

        var canLockPick = LockPickItem.canPick(held, locked.getLock());

        IThemeHelper t = IThemeHelper.get();
        Component text = canLockPick ? t.success(CHECK) : t.danger(X);
        elements.add((new SubTextElement(text)).translate(new Vec2(-3.0F, (float)(7 + offsetY))));

        return elements;
    }

    public void onResourceManagerReload(ResourceManager resourceManager) {
        resultCache.invalidateAll();
    }

    public ResourceLocation getUid() {
        return LocksWailaPlugin.LOCK_PICKING_TOOL;
    }

    public int getDefaultPriority() {
        return -8000;
    }

    static {
        if (CommonProxy.isPhysicallyClient()) {
            registerHandler(new SimpleLockPickHandler("lock_pick", LocksBlockTags.LOCKABLE,
                    LocksItems.COPPER_LOCK_PICK, LocksItems.IRON_LOCK_PICK,
                    LocksItems.GOLD_LOCK_PICK, LocksItems.STEEL_LOCK_PICK, LocksItems.DIAMOND_LOCK_PICK, LocksItems.NETHERITE_LOCK_PICK));
        }

    }
}
