package reoseah.magifacture.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import reoseah.magifacture.Magifacture;

public class AlembicBlockEntity extends MagifactureBlockEntity {
    public AlembicBlockEntity(BlockPos pos, BlockState state) {
        super(Magifacture.BlockEntityTypes.ALEMBIC, pos, state);
    }

    @Override
    protected DefaultedList<ItemStack> createSlotsList() {
        return DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }
}
