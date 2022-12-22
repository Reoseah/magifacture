package reoseah.magifacture.screen;

import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.slot.Slot;
import reoseah.magifacture.Magifacture;
import reoseah.magifacture.block.entity.CrematoriumBlockEntity;
import reoseah.magifacture.screen.slot.MagifactureSlot;

public class CrematoriumScreenHandler extends MagifactureScreenHandler {
    protected final SingleFluidStorage tank;

    private CrematoriumScreenHandler(int syncId, Inventory inventory, SingleFluidStorage tank, PlayerInventory playerInv) {
        super(Magifacture.ScreenHandlerTypes.CREMATORIUM, syncId, inventory);

        this.addSlot(new Slot(this.inventory, 0, 18, 17));
        this.addSlot(new Slot(this.inventory, 1, 18, 53));
        this.addSlot(new MagifactureSlot(this.inventory, 2, 78, 35).insertable(false));
        this.addSlot(new Slot(this.inventory, 3, 143, 17));
        this.addSlot(new MagifactureSlot(this.inventory, 4, 143, 53).insertable(false));

        this.addPlayerSlots(playerInv);

        this.addTank(this.tank = tank);
    }

    public CrematoriumScreenHandler(int syncId, CrematoriumBlockEntity be, PlayerInventory playerInv) {
        this(syncId, be, be.getTank(), playerInv);
    }

    public CrematoriumScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, new SimpleInventory(5), SingleFluidStorage.withFixedCapacity(4000 * 81, () -> {
        }), playerInv);
    }

    public SingleFluidStorage getTank() {
        return this.tank;
    }
}
