package melonslise.locks.common.util;

import melonslise.locks.common.item.LockItem;
import melonslise.locks.common.item.LockingItem;
//import melonslise.locks.common.item.SmartLockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.Observable;
import java.util.Random;

//public class Lock extends Observable {
//	public final int id;
//	// index is the order, value is the pin number
//
//
//	public Lock(int id, int length, boolean locked) {
//		this.id = id;
//		this.rng = new Random(id);
//		this.combo = this.shuffle(length);
//		// this.lookup = this.inverse(this.combo);
//		this.locked = locked;
//	}
//
//	public static Lock from(ItemStack stack) {
//		return new Lock(LockingItem.getOrSetId(stack), LockItem.getOrSetLength(stack), !LockItem.isOpen(stack));
//	}
//
//	public static final String KEY_ID = "Id", KEY_LENGTH = "Length", KEY_LOCKED = "Locked";
//
//	public static Lock fromNbt(CompoundTag nbt) {
//		return new Lock(nbt.getInt(KEY_ID), nbt.getByte(KEY_LENGTH), nbt.getBoolean(KEY_LOCKED));
//	}
//
//	public static CompoundTag toNbt(Lock lock) {
//		CompoundTag nbt = new CompoundTag();
//		nbt.putInt(KEY_ID, lock.id);
//		nbt.putByte(KEY_LENGTH, (byte) lock.combo.length);
//		nbt.putBoolean(KEY_LOCKED, lock.locked);
//		return nbt;
//	}
//
//	public static Lock fromBuf(FriendlyByteBuf buf) {
//		return new Lock(buf.readInt(), (int) buf.readByte(), buf.readBoolean());
//	}
//
//	public static void toBuf(FriendlyByteBuf buf, Lock lock) {
//		buf.writeInt(lock.id);
//		buf.writeByte((int) lock.getLength());
//		buf.writeBoolean(lock.isOpen());
//	}
//
//
//}