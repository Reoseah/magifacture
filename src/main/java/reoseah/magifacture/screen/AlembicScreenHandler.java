package reoseah.magifacture.screen;

import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import reoseah.magifacture.Magifacture;
import reoseah.magifacture.block.entity.AlembicBlockEntity;

public class AlembicScreenHandler extends MagifactureScreenHandler {
    protected final SingleFluidStorage tank;

    private AlembicScreenHandler(int syncId, Inventory inventory, SingleFluidStorage tank, PlayerInventory playerInv) {
        super(Magifacture.ScreenHandlerTypes.ALEMBIC, syncId, inventory);
        this.addSlot(new Slot(this.inventory, 0, 98, 18));
        this.addSlot(new Slot(this.inventory, 1, 98, 54) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        this.addPlayerSlots(playerInv);

        this.addTank(this.tank = tank);
    }

    public AlembicScreenHandler(int syncId, AlembicBlockEntity be, PlayerInventory playerInv) {
        this(syncId, be, be.getTank(), playerInv);
    }

    public AlembicScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, new SimpleInventory(2), SingleFluidStorage.withFixedCapacity(4000 * 81, () -> {
        }), playerInv);
    }

    public SingleFluidStorage getTank() {
        return this.tank;
    }
}
