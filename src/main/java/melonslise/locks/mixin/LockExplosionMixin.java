package melonslise.locks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import melonslise.locks.components.AbstractLocked;
import melonslise.locks.enchantment.SturdyEnchantment;
import melonslise.locks.init.LocksEnchantments;
import melonslise.locks.item.LockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ExplosionDamageCalculator.class)
public class LockExplosionMixin {
    @Inject(at = @At(value = "RETURN"), method = "getBlockExplosionResistance", cancellable = true)
    private void modifyChestResistance(
            Explosion explosion,
            BlockGetter blockGetter,
            BlockPos blockPos,
            BlockState blockState,
            FluidState fluidState,
            CallbackInfoReturnable<Optional<Float>> cir) {
        var locked = AbstractLocked.getFrom(blockGetter, blockPos);
        if (locked == null) {
            cir.cancel();
            return;
        }
        if (locked.getLock().isEmpty()) {
            cir.cancel();
            return;
        }
        var ret = cir.getReturnValue();
        if (ret.isEmpty()) {
            cir.cancel();
            return;
        }
        var lockStack = locked.getLock();
        var resist = LockItem.getResistance(lockStack);
        var blast = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLAST_PROTECTION, lockStack);
        if (blast > 0) {
            resist *= blast + 1;
        }
        cir.setReturnValue(Optional.of(ret.get()+ resist));
    }

}
