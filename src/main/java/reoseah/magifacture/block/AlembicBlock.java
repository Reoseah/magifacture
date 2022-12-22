package reoseah.magifacture.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reoseah.magifacture.Magifacture;
import reoseah.magifacture.block.entity.AlembicBlockEntity;

public class AlembicBlock extends MagifactureBlock {
    public static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 13, 16), createCuboidShape(4, 13, 4, 12, 16, 12));

    public AlembicBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlembicBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, Magifacture.BlockEntityTypes.ALEMBIC, AlembicBlockEntity::tickServer);
    }
}
