package reoseah.magifacture.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ExperienceBlock extends Block {
    public static final int MILLIBUCKET_PER_XP = 20;
    public static final int XP_PER_BUCKET = 1000 / MILLIBUCKET_PER_XP;

    public static final Block INSTANCE = new ExperienceBlock(FabricBlockSettings.of(Material.LAVA, MapColor.LIME).luminance(15).noCollision());

    protected ExperienceBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, 1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        world.scheduleBlockTick(pos, this, 1);
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int xp = MathHelper.floor(XP_PER_BUCKET * (0.5F + random.nextFloat() / 2));

        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        while (xp > 0) {
            int orbSize = ExperienceOrbEntity.roundToOrbSize(xp);
            world.spawnEntity(new ExperienceOrbEntity(world, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat(), orbSize));
            xp -= orbSize;
        }
    }
}
