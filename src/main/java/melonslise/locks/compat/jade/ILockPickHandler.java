package melonslise.locks.compat.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface ILockPickHandler {
    ItemStack test(Level level, BlockPos pos);

    List<ItemStack> getLockPicks();

    String getName();
}
