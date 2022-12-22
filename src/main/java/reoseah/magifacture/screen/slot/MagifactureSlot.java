package reoseah.magifacture.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class MagifactureSlot extends Slot {
    protected boolean canInsert = true;

    public MagifactureSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.canInsert;
    }

    public MagifactureSlot insertable(boolean canInsert) {
        this.canInsert = canInsert;
        return this;
    }
}
