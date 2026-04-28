package melonslise.locks.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public interface CanEnchantEvent {
    Event<CanEnchantEvent> POST = EventFactory.createArrayBacked(CanEnchantEvent.class,
            (listeners) -> (e, stack) -> {
                for (CanEnchantEvent listener : listeners) {
                    var result = listener.canEnchant(e, stack);
                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            });

    InteractionResult canEnchant(Enchantment e, ItemStack stack);
}
