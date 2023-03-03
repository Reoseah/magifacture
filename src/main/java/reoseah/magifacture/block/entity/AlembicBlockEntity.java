package reoseah.magifacture.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
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
import reoseah.magifacture.block.AlembicBlock;
import reoseah.magifacture.fluid.ExperienceFluid;
import reoseah.magifacture.screen.AlembicScreenHandler;
import reoseah.magifacture.util.FluidUtils;

public class AlembicBlockEntity extends MagifactureBlockEntity implements SidedInventory {
    public static final BlockEntityType<AlembicBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(AlembicBlockEntity::new, AlembicBlock.INSTANCE).build();
    protected final SingleFluidStorage tank = SingleFluidStorage.withFixedCapacity(4000 * 81, this::markDirty);

    public AlembicBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);

        // FIXME testing code
        tank.variant = FluidVariant.of(ExperienceFluid.EXPERIENCE);
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
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.tank.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.tank.writeNbt(nbt);
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

    public static void tickServer(@SuppressWarnings("unused") World world, @SuppressWarnings("unused") BlockPos pos, @SuppressWarnings("unused") BlockState state, AlembicBlockEntity be) {
        FluidUtils.tryFillItem(be.tank, be, EMPTY_SLOT, FILLED_SLOT);
    }

    public SingleFluidStorage getTank() {
        return this.tank;
    }
}
