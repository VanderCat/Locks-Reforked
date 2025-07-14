package melonslise.locks.common.util;
//
//import melonslise.locks.Locks;
//import melonslise.locks.common.init.LocksComponents;
//import melonslise.locks.common.item.LockItem;
////import melonslise.locks.common.item.SmartLockItem;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.culling.Frustum;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.AttachFace;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//import java.io.Serializable;
//import java.util.*;
//
///*
//	A lock lolololololololololol.
// */
//public class Lockable extends Observable implements Observer, Serializable {
//	public static class State implements Serializable {
//		public static final AABB
//				VERT_Z_BB = new AABB(-2d / 16d, -3d / 16d, 2d / 16d, 2d / 16d, 3d / 16d, 2d / 16d),
//				VERT_X_BB = LocksUtil.rotateY(VERT_Z_BB),
//				HOR_Z_BB = LocksUtil.rotateX(VERT_Z_BB),
//				HOR_X_BB = LocksUtil.rotateY(HOR_Z_BB);
//
//		public static AABB getBounds(Transform tr) {
//            if (tr.face == AttachFace.WALL) {
//                if (tr.dir.getAxis() == Direction.Axis.Z)
//					return VERT_Z_BB;
//                return VERT_X_BB;
//            }
//            if (tr.dir.getAxis() == Direction.Axis.Z)
//				return HOR_Z_BB;
//            return HOR_X_BB;
//        }
//
//		public final Vec3 pos;
//		public final Transform tr;
//		public final AABB bb;
//
//		public State(Vec3 pos, Transform tr) {
//			this(pos, tr, getBounds(tr).move(pos));
//		}
//
//		public State(Vec3 pos, Transform tr, AABB bb) {
//			this.pos = pos;
//			this.tr = tr;
//			this.bb = bb;
//		}
//
//		@Environment(EnvType.CLIENT)
//		public boolean inView(Frustum ch) {
//			AABB aabb = new AABB(this.bb.minX, this.bb.minY, this.bb.minZ, this.bb.maxX, this.bb.maxY, this.bb.maxZ);
//			return ch.isVisible(aabb);
//		}
//
//		@Environment(EnvType.CLIENT)
//		public boolean inRange(Vec3 pos) {
//			Minecraft mc = Minecraft.getInstance();
//			double dist = this.pos.distanceToSqr(pos);
//			double max = mc.options.renderDistance().get() * 8;
//			return dist < max * max;
//		}
//	}
//
//	public Cuboid6i boundingBox;
//	//public Lock lock;
//	public Transform transform;
//	public ItemStack stack;
//	public int id;
//
//	public int
//			oldSwingTicks,
//			swingTicks,
//			maxSwingTicks;
//
//	public Map<List<BlockState>, State> cache = new HashMap<>(6);
//
////	public Lockable(Cuboid6i boundingBox, Lock lock, Transform transform, ItemStack stack, Level world)
////	{
//////		this(boundingBox, lock, transform, stack, LocksComponents.LOCKABLE_HANDLER.get(world).nextId());
////	}
//
//	/**
//	 Creates a lock
//
//	 @param boundingBox 	 - Location of the lock.
//	 @param lock - You wont believe me...
//	 @param transform	 - Enumerator that dictates direction lock is facing
//	 @param stack - In hand item
//	 @param id - Lock combination id.
//	 */
//	public Lockable(Cuboid6i boundingBox, Lock lock, Transform transform, ItemStack stack, int id) {
//		this.boundingBox = boundingBox;
//		this.lock = lock;
//		this.transform = transform;
//		this.stack = stack;
//		this.id = id;
//
//		this.checkIntegrity(this);
//		lock.addObserver(this);
//	}
//
//	public Lockable() {
//	}
//
//	public static final String
//			KEY_BB = "Bb",
//			KEY_LOCK = "Lock",
//			KEY_TRANSFORM = "Transform",
//			KEY_STACK = "Stack",
//			KEY_ID = "Id";
//
//	public static Lockable fromNbt(CompoundTag nbt) {
//		return new Lockable(Cuboid6i.fromNbt(nbt.getCompound(KEY_BB)), Lock.fromNbt(nbt.getCompound(KEY_LOCK)), Transform.values()[(int) nbt.getByte(KEY_TRANSFORM)], ItemStack.of(nbt.getCompound(KEY_STACK)), nbt.getInt(KEY_ID));
//	}
//
//	/**
//	 BIGGGG
//	 Shit
//	 */
//	public static CompoundTag toNbt(Lockable lkb) {
//		CompoundTag nbt = new CompoundTag();
//		nbt.put(KEY_BB, Cuboid6i.toNbt(lkb.boundingBox));
//		nbt.put(KEY_LOCK, Lock.toNbt(lkb.lock));
//
//
//		if (lkb.transform == null) {
//			Locks.LOGGER.warn("Transform is null for Lockable?");
//			lkb.transform = Transform.NORTH_UP;
//		}
//		nbt.putByte(KEY_TRANSFORM, (byte) (lkb.transform != null ? lkb.transform.ordinal() : Transform.NORTH_MID.ordinal()));
//		//nbt.putByte(KEY_TRANSFORM, (byte) lkb.tr.ordinal());
//		nbt.put(KEY_STACK, lkb.stack.save(new CompoundTag()));
//		nbt.putInt(KEY_ID, lkb.id);
//		return nbt;
//	}
//
////	/**
////	 * Returns true if the lock is a smart lock
////	 *
////	 * @return - Returns true if the lock is a smart lock
////	 */
////	public boolean isSmart()
////	{
////		return this.stack.getItem() instanceof SmartLockItem;
////	}
//
//	public static int idFromNbt(CompoundTag nbt) {
//		return nbt.getInt(KEY_ID);
//	}
//
//
//	/**
//	 Reads the data back from the network buffer into this object.
//	 */
//	public static Lockable fromBuf(FriendlyByteBuf buf) {
//		return new Lockable(Cuboid6i.fromBuf(buf), Lock.fromBuf(buf), buf.readEnum(Transform.class), buf.readItem(), buf.readInt());
//	}
//
//	/**
//	 Sends this object’s data over the network (like from server to client). Minecraft uses PacketByteBuf for custom packet data.
//
//	 @param buf - big boy buffer
//	 @param lkb - the lock
//	 */
//	public static void toBuf(FriendlyByteBuf buf, Lockable lkb) {
//		lkb = checkIntegrity(lkb);
//		Cuboid6i.toBuf(buf, lkb.boundingBox);
//		Lock.toBuf(buf, lkb.lock);
//
//		buf.writeEnum(lkb.transform != null ? lkb.transform : Transform.NORTH_MID);
//		//buf.writeEnum(lkb.tr);
//		buf.writeItem(lkb.stack);
//		buf.writeInt(lkb.id);
//	}
//
//	/**
//	 * Checks if the values of the lock a NOT null. If null, then it sets a default value.
//	 *
//	 * @param lkb - The lock lol.
//	 * @return
//	 */
//	private static Lockable checkIntegrity(Lockable lkb) {
//		if (lkb.transform == null) {
//			Locks.LOGGER.warn("Transform was null for Lockable");
//            Locks.LOGGER.warn("BB (Block location is {}", lkb.boundingBox);
//            Locks.LOGGER.warn("Id is {}", lkb.id);
//            Locks.LOGGER.warn("Stack (Type of lock and amount) is {}", lkb.stack);
//			lkb.transform = Transform.NORTH_UP;
//
//			//Locks.LOGGER.warn("\n\n\n --------------------------");
//		}
//
//		return lkb;
//	}
//
//	@Override
//	public void update(Observable lock, Object data) {
//		this.setChanged();
//		this.notifyObservers();
//		LockItem.setOpen(this.stack, !this.lock.locked);
//	}
//
//	public void tick() {
//		this.oldSwingTicks = this.swingTicks;
//		if(this.swingTicks > 0)
//			--this.swingTicks;
//	}
//
//	public void swing(int ticks) {
//		this.swingTicks = this.oldSwingTicks = this.maxSwingTicks = ticks;
//	}
//
//	// FIXME use array instead of list
//	public State getLockState(Level world) {
//		List<BlockState> states = new ArrayList<>(this.boundingBox.volume());
//		for(BlockPos pos : this.boundingBox.getContainedPos())
//		{
//			if(!world.hasChunkAt(pos))
//				return null;
//			states.add(world.getBlockState(pos));
//		}
//		State state = this.cache.get(states);
//		if(state != null)
//			return state;
//		ArrayList<AABB> boxes = new ArrayList<>(4);
//		for(BlockPos pos : this.boundingBox.getContainedPos())
//		{
//			VoxelShape shape = world.getBlockState(pos).getShape(world, pos);
//			if(shape.isEmpty())
//				continue;
//			AABB bb = shape.bounds();
//			bb = bb.move(pos);
//			AABB union = bb;
//			Iterator<AABB> it = boxes.iterator();
//			while(it.hasNext())
//			{
//				AABB bb1 = it.next();
//				if(LocksUtil.intersectsInclusive(union, bb1))
//				{
//					union = union.minmax(bb1);
//					it.remove();
//				}
//			}
//			boxes.add(union);
//		}
//		if(boxes.isEmpty())
//			return null;
//		Direction side = this.transform.getCuboidFace();
//		Vec3 center = this.boundingBox.sideCenter(side);
//		Vec3 point = center;
//		double min = -1d;
//		for(AABB box : boxes)
//			for(Direction side1 : Direction.values())
//			{
//				Vec3 point1 = LocksUtil.sideCenter(box, side1).add(Vec3.atLowerCornerOf(side1.getNormal()).scale(0.05d));
//				double dist = center.distanceToSqr(point1);
//				if(min != -1d && dist >= min)
//					continue;
//				point = point1;
//				min = dist;
//				side = side1;
//			}
//		state = new State(point, Transform.fromDirection(side, this.transform.dir));
//		this.cache.put(states, state);
//		return state;
//	}
//}