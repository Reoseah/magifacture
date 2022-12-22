package reoseah.magifacture.block.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reoseah.magifacture.Magifacture;
import reoseah.magifacture.screen.AlembicScreenHandler;
import reoseah.magifacture.util.FluidUtils;

public class AlembicBlockEntity extends MagifactureBlockEntity implements SidedInventory {

    protected final SingleFluidStorage tank = new SingleFluidStorage() {
        @Override
        protected long getCapacity(FluidVariant variant) {
            return 4000 * 81;
        }
    };

    public AlembicBlockEntity(BlockPos pos, BlockState state) {
        super(Magifacture.BlockEntityTypes.ALEMBIC, pos, state);

        // FIXME testing code
        tank.variant = FluidVariant.of(Magifacture.Fluids.EXPERIENCE);
        tank.amount = 2000 * 81;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AlembicScreenHandler(syncId, this, playerInventory);
    }

    @Override
    protected DefaultedList<ItemStack> createSlotsList() {
        return DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.tank.readNbt(tag);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        this.tank.writeNbt(tag);
    }


    // region SidedInventory
    private static final int EMPTY_SLOT = 0;
    private static final int FILLED_SLOT = 1;
    private static final int[] TOP_SLOTS = {EMPTY_SLOT};
    private static final int[] SIDE_SLOTS = {EMPTY_SLOT, FILLED_SLOT};
    private static final int[] BOTTOM_SLOTS = {FILLED_SLOT};

    @Override
    public int[] getAvailableSlots(Direction side) {
        return switch (side) {
            case UP -> TOP_SLOTS;
            case DOWN -> BOTTOM_SLOTS;
            default -> SIDE_SLOTS;
        };
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == EMPTY_SLOT;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == FILLED_SLOT;
    }
    // endregion

    public ResourceAmount<FluidVariant> getFluidVolume() {
        return new ResourceAmount<>(this.tank.variant, this.tank.amount);
    }

    public static void tickServer(@SuppressWarnings("unused") World world, @SuppressWarnings("unused") BlockPos pos, @SuppressWarnings("unused") BlockState state, AlembicBlockEntity be) {
        FluidUtils.tryFillItem(be.tank, be, EMPTY_SLOT, FILLED_SLOT);
    }

    public SingleFluidStorage getTank() {
        return this.tank;
    }

}
