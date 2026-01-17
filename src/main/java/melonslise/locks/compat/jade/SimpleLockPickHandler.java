//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package melonslise.locks.compat.jade;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

import melonslise.locks.components.AbstractLocked;
import melonslise.locks.item.LockItem;
import melonslise.locks.item.LockPickItem;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SimpleLockPickHandler implements ILockPickHandler {
    private final String name;
    protected final List<ItemStack> lockPicks = Lists.newArrayList();
    protected final TagKey<Block> tag;

    public SimpleLockPickHandler(String name, TagKey<Block> tag, Item... tools) {
        this.tag = tag;
        this.name = name;

        for(Item tool : tools) {
            this.lockPicks.add(tool.getDefaultInstance());
        }

    }
    @Override
    public ItemStack test(Level world, BlockPos pos) {
        var locked = AbstractLocked.getFrom(world, pos);
        if (locked == null)
            return ItemStack.EMPTY;
        var stack = locked.getLock();
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        var item = stack.getItem();
        if (!(item instanceof LockItem))
            return ItemStack.EMPTY;

        for(ItemStack tool : this.lockPicks) {
            if (!(tool.getItem() instanceof LockPickItem))
                continue;
            if (LockPickItem.canPick(tool, stack))
                return tool;
        }

        return ItemStack.EMPTY;
    }

    public List<ItemStack> getLockPicks() {
        return this.lockPicks;
    }

    public String getName() {
        return this.name;
    }
}
