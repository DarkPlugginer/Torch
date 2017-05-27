package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason; // CraftBukkit

public class BlockMonsterEggs extends Block {

    public static final BlockStateEnum<BlockMonsterEggs.EnumMonsterEggVarient> VARIANT = BlockStateEnum.of("variant", BlockMonsterEggs.EnumMonsterEggVarient.class);

    public BlockMonsterEggs() {
        super(Material.CLAY);
        this.y(this.blockStateList.getBlockData().set(BlockMonsterEggs.VARIANT, BlockMonsterEggs.EnumMonsterEggVarient.STONE));
        this.c(0.0F);
        this.a(CreativeModeTab.c);
    }

    @Override
    public int a(Random random) {
        return 0;
    }

    public static boolean i(IBlockData iblockdata) {
        Block block = iblockdata.getBlock();

        return iblockdata == Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.STONE) || block == Blocks.COBBLESTONE || block == Blocks.STONEBRICK;
    }

    @Override
    protected ItemStack w(IBlockData iblockdata) {
        switch (iblockdata.get(BlockMonsterEggs.VARIANT)) {
            case COBBLESTONE:
                return new ItemStack(Blocks.COBBLESTONE);

            case STONEBRICK:
                return new ItemStack(Blocks.STONEBRICK);

            case MOSSY_STONEBRICK:
                return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.MOSSY.a());

            case CRACKED_STONEBRICK:
                return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.CRACKED.a());

            case CHISELED_STONEBRICK:
                return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.CHISELED.a());

            default:
                return new ItemStack(Blocks.STONE);
        }
    }

    @Override
    public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        if (world.getGameRules().getBoolean("doTileDrops")) {
            EntitySilverfish entitysilverfish = new EntitySilverfish(world);

            entitysilverfish.setPositionRotation(blockposition.getX() + 0.5D, blockposition.getY(), blockposition.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(entitysilverfish, SpawnReason.SILVERFISH_BLOCK); // CraftBukkit - add SpawnReason
            entitysilverfish.doSpawnEffect();
        }

    }

    @Override
    public ItemStack a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return new ItemStack(this, 1, iblockdata.getBlock().toLegacyData(iblockdata));
    }

    @Override
    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockMonsterEggs.VARIANT, BlockMonsterEggs.EnumMonsterEggVarient.a(i));
    }

    @Override
    public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockMonsterEggs.VARIANT).a();
    }

    @Override
    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockMonsterEggs.VARIANT});
    }

    public static enum EnumMonsterEggVarient implements INamable {

        STONE(0, "stone") {;
        @Override
        public IBlockData d() {
            return Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.STONE);
        }
        }, COBBLESTONE(1, "cobblestone", "cobble") {;
        @Override
        public IBlockData d() {
            return Blocks.COBBLESTONE.getBlockData();
        }
        }, STONEBRICK(2, "stone_brick", "brick") {;
        @Override
        public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.DEFAULT);
        }
        }, MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {;
        @Override
        public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.MOSSY);
        }
        }, CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {;
        @Override
        public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.CRACKED);
        }
        }, CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {;
        @Override
        public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.CHISELED);
        }
        };

        private static final BlockMonsterEggs.EnumMonsterEggVarient[] g = new BlockMonsterEggs.EnumMonsterEggVarient[values().length];
        private final int h;
        private final String i;
        private final String j;

        private EnumMonsterEggVarient(int i, String s) {
            this(i, s, s);
        }

        private EnumMonsterEggVarient(int i, String s, String s1) {
            this.h = i;
            this.i = s;
            this.j = s1;
        }

        public int a() {
            return this.h;
        }

        @Override
        public String toString() {
            return this.i;
        }

        public static BlockMonsterEggs.EnumMonsterEggVarient a(int i) {
            if (i < 0 || i >= BlockMonsterEggs.EnumMonsterEggVarient.g.length) {
                i = 0;
            }

            return BlockMonsterEggs.EnumMonsterEggVarient.g[i];
        }

        @Override
        public String getName() {
            return this.i;
        }

        public String c() {
            return this.j;
        }

        public abstract IBlockData d();

        public static BlockMonsterEggs.EnumMonsterEggVarient a(IBlockData iblockdata) {
            BlockMonsterEggs.EnumMonsterEggVarient[] ablockmonstereggs_enummonstereggvarient = values();
            int i = ablockmonstereggs_enummonstereggvarient.length;

            for (int j = 0; j < i; ++j) {
                BlockMonsterEggs.EnumMonsterEggVarient blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

                if (iblockdata == blockmonstereggs_enummonstereggvarient.d()) {
                    return blockmonstereggs_enummonstereggvarient;
                }
            }

            return BlockMonsterEggs.EnumMonsterEggVarient.STONE;
        }

        EnumMonsterEggVarient(int i, String s, Object object) {
            this(i, s);
        }

        EnumMonsterEggVarient(int i, String s, String s1, Object object) {
            this(i, s, s1);
        }

        static {
            BlockMonsterEggs.EnumMonsterEggVarient[] ablockmonstereggs_enummonstereggvarient = values();
            int i = ablockmonstereggs_enummonstereggvarient.length;

            for (int j = 0; j < i; ++j) {
                BlockMonsterEggs.EnumMonsterEggVarient blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

                BlockMonsterEggs.EnumMonsterEggVarient.g[blockmonstereggs_enummonstereggvarient.a()] = blockmonstereggs_enummonstereggvarient;
            }

        }
    }
}
