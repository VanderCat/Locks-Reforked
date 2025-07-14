package melonslise.locks.common.init;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupTab;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import melonslise.locks.Locks;
import melonslise.locks.common.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class LocksItems implements ItemRegistryContainer {
    public static List<Item> items = new ArrayList<>();

    public static final Item
            INTEGRATED_CIRCUIT = new Item(new Item.Properties()),
            SPRING = new Item(new Item.Properties()),
            WOOD_LOCK_MECHANISM = new Item(new Item.Properties()),
            IRON_LOCK_MECHANISM = new Item(new Item.Properties()),
            STEEL_LOCK_MECHANISM = new Item(new Item.Properties()),
            KEY_BLANK = new Item(new Item.Properties()),
            WOOD_LOCK = new LockItem(5, 15, 4, new Item.Properties()),
            IRON_LOCK = new LockItem(7, 14, 12, new Item.Properties()),
            STEEL_LOCK = new LockItem(9, 12, 20, new Item.Properties()),
            GOLD_LOCK = new LockItem(6, 22, 6, new Item.Properties()),
            DIAMOND_LOCK = new LockItem(11, 10, 100, new Item.Properties()),
            NETHERITE_LOCK = new LockItem(15, 12, 100, new Item.Properties().fireResistant()),
            //SMART_NETHERITE_LOCK = new SmartLockItem(12, new Item.Properties().fireResistant()),
            KEY = new KeyItem(new Item.Properties()),
            MASTER_KEY = new MasterKeyItem(new Item.Properties()),
            KEY_RING = new KeyRingItem(2, new Item.Properties()),
            WOOD_LOCK_PICK = new LockPickItem(0.2f, new Item.Properties()),
            IRON_LOCK_PICK = new LockPickItem(0.35f, new Item.Properties()),
            STEEL_LOCK_PICK = new LockPickItem(0.7f, new Item.Properties()),
            GOLD_LOCK_PICK = new LockPickItem(0.25f, new Item.Properties()),
            DIAMOND_LOCK_PICK = new LockPickItem(0.85f, new Item.Properties()),
            NETHERITE_LOCK_PICK = new LockPickItem(0.9f, new Item.Properties().fireResistant());

    public static final OwoItemGroup GROUP = OwoItemGroup
            .builder(new ResourceLocation(Locks.ID, "items"), () -> Icon.of(LocksItems.IRON_LOCK))
            .initializer(owoItemGroup -> {
                owoItemGroup.tabs.add(new ItemGroupTab(Icon.of(ItemStack.EMPTY), Component.empty(), (context, entries) -> {
                    entries.accept(INTEGRATED_CIRCUIT);
                    entries.accept(SPRING);
                    entries.accept(WOOD_LOCK_MECHANISM);
                    entries.accept(IRON_LOCK_MECHANISM);
                    entries.accept(STEEL_LOCK_MECHANISM);
                    entries.accept(KEY_BLANK);
                    entries.accept(WOOD_LOCK);
                    entries.accept(IRON_LOCK);
                    entries.accept(STEEL_LOCK);
                    entries.accept(GOLD_LOCK);
                    entries.accept(DIAMOND_LOCK);
                    entries.accept(NETHERITE_LOCK);
                    //entries.accept(SMART_NETHERITE_LOCK);
                    entries.accept(KEY);
                    entries.accept(MASTER_KEY);
                    entries.accept(KEY_RING);
                    entries.accept(WOOD_LOCK_PICK);
                    entries.accept(IRON_LOCK_PICK);
                    entries.accept(STEEL_LOCK_PICK);
                    entries.accept(GOLD_LOCK_PICK);
                    entries.accept(DIAMOND_LOCK_PICK);
                    entries.accept(NETHERITE_LOCK_PICK);
                }, ItemGroupTab.DEFAULT_TEXTURE, true));
            })
            .build();

//    public static final CreativeModeTab TABS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Locks.ID, CreativeModeTab
//            .builder(CreativeModeTab.Row.TOP, 9)
//            .icon(() -> new ItemStack(LocksItems.IRON_LOCK))
//            .title(Component.translatable("itemGroup.locks"))
//            .displayItems((parameters, output) -> {
////                for (Enchantment enchantmentRegistryObject : LocksEnchantments.ENCHANTMENTS.getEntries()) {
////                    Enchantment enchantment = enchantmentRegistryObject;
////                    ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
////                    enchantedBook.enchant(enchantment, enchantment.getMaxLevel());
////                    output.accept(enchantedBook);
////                }
//                for (Item itemRegistryObject : items) {
//                    output.accept(itemRegistryObject);
//                }
//            })
//            .build());
}