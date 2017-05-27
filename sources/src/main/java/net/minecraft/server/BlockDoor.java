package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockDoor extends Block {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
    public static final BlockStateEnum<BlockDoor.EnumDoorHinge> HINGE = BlockStateEnum.of("hinge", BlockDoor.EnumDoorHinge.class);
    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
    public static final BlockStateEnum<BlockDoor.EnumDoorHalf> HALF = BlockStateEnum.of("half", BlockDoor.EnumDoorHalf.class);
    protected static final AxisAlignedBB f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB g = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB B = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB C = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

    protected BlockDoor(Material material) {
        super(material);
        this.y(this.blockStateList.getBlockData().set(BlockDoor.FACING, EnumDirection.NORTH).set(BlockDoor.OPEN, Boolean.valueOf(false)).set(BlockDoor.HINGE, BlockDoor.EnumDoorHinge.LEFT).set(BlockDoor.POWERED, Boolean.valueOf(false)).set(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER));
    }

    @Override
    public AxisAlignedBB b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        iblockdata = iblockdata.b(iblockaccess, blockposition);
        EnumDirection enumdirection = iblockdata.get(BlockDoor.FACING);
        boolean flag = !iblockdata.get(BlockDoor.OPEN).booleanValue();
        boolean flag1 = iblockdata.get(BlockDoor.HINGE) == BlockDoor.EnumDoorHinge.RIGHT;

        switch (enumdirection) {
        case EAST:
        default:
            return flag ? BlockDoor.C : (flag1 ? BlockDoor.g : BlockDoor.f);

        case SOUTH:
            return flag ? BlockDoor.f : (flag1 ? BlockDoor.C : BlockDoor.B);

        case WEST:
            return flag ? BlockDoor.B : (flag1 ? BlockDoor.f : BlockDoor.g);

        case NORTH:
            return flag ? BlockDoor.g : (flag1 ? BlockDoor.B : BlockDoor.C);
        }
    }

    @Override
    public String getName() {
        return LocaleI18n.get((this.a() + ".name").replaceAll("tile", "item"));
    }

    @Override
    public boolean b(IBlockData iblockdata) {
        return false;
    }

    @Override
    public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return g(c(iblockaccess, blockposition));
    }

    @Override
    public boolean c(IBlockData iblockdata) {
        return false;
    }

    private int e() {
        return this.material == Material.ORE ? 1011 : 1012;
    }

    private int g() {
        return this.material == Material.ORE ? 1005 : 1006;
    }

    @Override
    public MaterialMapColor r(IBlockData iblockdata) {
        return iblockdata.getBlock() == Blocks.IRON_DOOR ? MaterialMapColor.h : (iblockdata.getBlock() == Blocks.WOODEN_DOOR ? BlockWood.EnumLogVariant.OAK.c() : (iblockdata.getBlock() == Blocks.SPRUCE_DOOR ? BlockWood.EnumLogVariant.SPRUCE.c() : (iblockdata.getBlock() == Blocks.BIRCH_DOOR ? BlockWood.EnumLogVariant.BIRCH.c() : (iblockdata.getBlock() == Blocks.JUNGLE_DOOR ? BlockWood.EnumLogVariant.JUNGLE.c() : (iblockdata.getBlock() == Blocks.ACACIA_DOOR ? BlockWood.EnumLogVariant.ACACIA.c() : (iblockdata.getBlock() == Blocks.DARK_OAK_DOOR ? BlockWood.EnumLogVariant.DARK_OAK.c() : super.r(iblockdata)))))));
    }

    @Override
    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        if (this.material == Material.ORE) {
            return false;
        } else {
            BlockPosition blockposition1 = iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER ? blockposition : blockposition.down();
            IBlockData iblockdata1 = blockposition.equals(blockposition1) ? iblockdata : world.getType(blockposition1);

            if (iblockdata1.getBlock() != this) {
                return false;
            } else {
                iblockdata = iblockdata1.a((IBlockState) BlockDoor.OPEN);
                world.setTypeAndData(blockposition1, iblockdata, 10);
                world.b(blockposition1, blockposition);
                world.a(entityhuman, iblockdata.get(BlockDoor.OPEN).booleanValue() ? this.g() : this.e(), blockposition, 0);
                return true;
            }
        }
    }

    public void setDoor(World world, BlockPosition blockposition, boolean flag) {
        IBlockData iblockdata = world.getType(blockposition);

        if (iblockdata.getBlock() == this) {
            BlockPosition blockposition1 = iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER ? blockposition : blockposition.down();
            IBlockData iblockdata1 = blockposition == blockposition1 ? iblockdata : world.getType(blockposition1);

            if (iblockdata1.getBlock() == this && iblockdata1.get(BlockDoor.OPEN).booleanValue() != flag) {
                world.setTypeAndData(blockposition1, iblockdata1.set(BlockDoor.OPEN, Boolean.valueOf(flag)), 10);
                world.b(blockposition1, blockposition);
                world.a((EntityHuman) null, flag ? this.g() : this.e(), blockposition, 0);
            }

        }
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        if (iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
            BlockPosition blockposition2 = blockposition.down();
            IBlockData iblockdata1 = world.getType(blockposition2);

            if (iblockdata1.getBlock() != this) {
                world.setAir(blockposition);
            } else if (block != this) {
                iblockdata1.doPhysics(world, blockposition2, block, blockposition1);
            }
        } else {
            boolean flag = false;
            BlockPosition blockposition3 = blockposition.up();
            IBlockData iblockdata2 = world.getType(blockposition3);

            if (iblockdata2.getBlock() != this) {
                world.setAir(blockposition);
                flag = true;
            }

            if (!world.getType(blockposition.down()).r()) {
                world.setAir(blockposition);
                flag = true;
                if (iblockdata2.getBlock() == this) {
                    world.setAir(blockposition3);
                }
            }

            if (flag) {
                this.b(world, blockposition, iblockdata, 0);
            } else {

                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bukkitBlock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                org.bukkit.block.Block blockTop = bworld.getBlockAt(blockposition3.getX(), blockposition3.getY(), blockposition3.getZ());

                int power = bukkitBlock.getBlockPower();
                int powerTop = blockTop.getBlockPower();
                if (powerTop > power) power = powerTop;
                int oldPower = iblockdata2.get(BlockDoor.POWERED) ? 15 : 0;

                if (oldPower == 0 ^ power == 0) {
                    BlockRedstoneEvent eventRedstone = BlockRedstoneEvent.requestMutable(bukkitBlock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);

                    boolean flag1 = eventRedstone.getNewCurrent() > 0;
                    // CraftBukkit end
                    world.setTypeAndData(blockposition3, iblockdata2.set(BlockDoor.POWERED, Boolean.valueOf(flag1)), 2);
                    if (flag1 != iblockdata.get(BlockDoor.OPEN).booleanValue()) {
                        world.setTypeAndData(blockposition, iblockdata.set(BlockDoor.OPEN, Boolean.valueOf(flag1)), 2);
                        world.b(blockposition, blockposition);
                        world.a((EntityHuman) null, flag1 ? this.g() : this.e(), blockposition, 0);
                    }
                }
            }
        }

    }

    @Override
    public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.a : this.h();
    }

    @Override
    public boolean canPlace(World world, BlockPosition blockposition) {
        return blockposition.getY() >= 255 ? false : world.getType(blockposition.down()).r() && super.canPlace(world, blockposition) && super.canPlace(world, blockposition.up());
    }

    @Override
    public EnumPistonReaction h(IBlockData iblockdata) {
        return EnumPistonReaction.DESTROY;
    }

    public static int c(IBlockAccess iblockaccess, BlockPosition blockposition) {
        IBlockData iblockdata = iblockaccess.getType(blockposition);
        int i = iblockdata.getBlock().toLegacyData(iblockdata);
        boolean flag = i(i);
        IBlockData iblockdata1 = iblockaccess.getType(blockposition.down());
        int j = iblockdata1.getBlock().toLegacyData(iblockdata1);
        int k = flag ? j : i;
        IBlockData iblockdata2 = iblockaccess.getType(blockposition.up());
        int l = iblockdata2.getBlock().toLegacyData(iblockdata2);
        int i1 = flag ? i : l;
        boolean flag1 = (i1 & 1) != 0;
        boolean flag2 = (i1 & 2) != 0;

        return e(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
    }

    @Override
    public ItemStack a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return new ItemStack(this.h());
    }

    private Item h() {
        return this == Blocks.IRON_DOOR ? Items.IRON_DOOR : (this == Blocks.SPRUCE_DOOR ? Items.SPRUCE_DOOR : (this == Blocks.BIRCH_DOOR ? Items.BIRCH_DOOR : (this == Blocks.JUNGLE_DOOR ? Items.JUNGLE_DOOR : (this == Blocks.ACACIA_DOOR ? Items.ACACIA_DOOR : (this == Blocks.DARK_OAK_DOOR ? Items.DARK_OAK_DOOR : Items.WOODEN_DOOR)))));
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        BlockPosition blockposition1 = blockposition.down();
        BlockPosition blockposition2 = blockposition.up();

        if (entityhuman.abilities.canInstantlyBuild && iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER && world.getType(blockposition1).getBlock() == this) {
            world.setAir(blockposition1);
        }

        if (iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER && world.getType(blockposition2).getBlock() == this) {
            if (entityhuman.abilities.canInstantlyBuild) {
                world.setAir(blockposition);
            }

            world.setAir(blockposition2);
        }

    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        IBlockData iblockdata1;

        if (iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
            iblockdata1 = iblockaccess.getType(blockposition.up());
            if (iblockdata1.getBlock() == this) {
                iblockdata = iblockdata.set(BlockDoor.HINGE, iblockdata1.get(BlockDoor.HINGE)).set(BlockDoor.POWERED, iblockdata1.get(BlockDoor.POWERED));
            }
        } else {
            iblockdata1 = iblockaccess.getType(blockposition.down());
            if (iblockdata1.getBlock() == this) {
                iblockdata = iblockdata.set(BlockDoor.FACING, iblockdata1.get(BlockDoor.FACING)).set(BlockDoor.OPEN, iblockdata1.get(BlockDoor.OPEN));
            }
        }

        return iblockdata;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return iblockdata.get(BlockDoor.HALF) != BlockDoor.EnumDoorHalf.LOWER ? iblockdata : iblockdata.set(BlockDoor.FACING, enumblockrotation.a(iblockdata.get(BlockDoor.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return enumblockmirror == EnumBlockMirror.NONE ? iblockdata : iblockdata.a(enumblockmirror.a(iblockdata.get(BlockDoor.FACING))).a((IBlockState) BlockDoor.HINGE);
    }

    @Override
    public IBlockData fromLegacyData(int i) {
        return (i & 8) > 0 ? this.getBlockData().set(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).set(BlockDoor.HINGE, (i & 1) > 0 ? BlockDoor.EnumDoorHinge.RIGHT : BlockDoor.EnumDoorHinge.LEFT).set(BlockDoor.POWERED, Boolean.valueOf((i & 2) > 0)) : this.getBlockData().set(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).set(BlockDoor.FACING, EnumDirection.fromType2(i & 3).f()).set(BlockDoor.OPEN, Boolean.valueOf((i & 4) > 0));
    }

    @Override
    public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i;

        if (iblockdata.get(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER) {
            i = b0 | 8;
            if (iblockdata.get(BlockDoor.HINGE) == BlockDoor.EnumDoorHinge.RIGHT) {
                i |= 1;
            }

            if (iblockdata.get(BlockDoor.POWERED).booleanValue()) {
                i |= 2;
            }
        } else {
            i = b0 | iblockdata.get(BlockDoor.FACING).e().get2DRotationValue();
            if (iblockdata.get(BlockDoor.OPEN).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    protected static int e(int i) {
        return i & 7;
    }

    public static boolean d(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return g(c(iblockaccess, blockposition));
    }

    public static EnumDirection f(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return f(c(iblockaccess, blockposition));
    }

    public static EnumDirection f(int i) {
        return EnumDirection.fromType2(i & 3).f();
    }

    protected static boolean g(int i) {
        return (i & 4) != 0;
    }

    protected static boolean i(int i) {
        return (i & 8) != 0;
    }

    @Override
    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockDoor.HALF, BlockDoor.FACING, BlockDoor.OPEN, BlockDoor.HINGE, BlockDoor.POWERED});
    }

    public static enum EnumDoorHinge implements INamable {

        LEFT, RIGHT;

        private EnumDoorHinge() {}

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == BlockDoor.EnumDoorHinge.LEFT ? "left" : "right";
        }
    }

    public static enum EnumDoorHalf implements INamable {

        UPPER, LOWER;

        private EnumDoorHalf() {}

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == BlockDoor.EnumDoorHalf.UPPER ? "upper" : "lower";
        }
    }
}
