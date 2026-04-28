package melonslise.locks.mixin;

import melonslise.locks.events.CanEnchantEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class LockEnchantmentMixin {
    @Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
    private void event(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        var result = CanEnchantEvent.POST.invoker().canEnchant((Enchantment)(Object)this, stack);
        if (result == InteractionResult.PASS)
            return;
        cir.setReturnValue(result == InteractionResult.SUCCESS);
    }
}
