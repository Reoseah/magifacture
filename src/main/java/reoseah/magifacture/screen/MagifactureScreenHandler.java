package reoseah.magifacture.screen;

import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import reoseah.magifacture.screen.slot.NbtCarryingSlot;

public abstract class MagifactureScreenHandler extends ScreenHandler {
    protected final Inventory inventory;
    private int playerFirstSlotIdx = -1;

    protected MagifactureScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, Inventory inventory) {
        super(type, syncId);
        this.inventory = inventory;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    protected Slot addSlot(Slot slot) {
        if (slot.inventory instanceof PlayerInventory && this.playerFirstSlotIdx == -1) {
            this.playerFirstSlotIdx = this.slots.size();
        }
        return super.addSlot(slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY; // TODO make basic implementation using this#playerFirstSlotIdx
    }

    protected void addPlayerSlots(PlayerInventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInv, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInv, column, 8 + column * 18, 142));
        }
    }

    protected void addTank(SingleFluidStorage tank) {
        // TODO: change this to take some NbtSerializable interface
        this.addSlot(new NbtCarryingSlot(() -> {
            NbtCompound nbt = new NbtCompound();
            tank.writeNbt(nbt);
            return nbt;
        }, tank::readNbt));
    }

    public boolean isInventorySlot(Slot slot) {
        return slot.inventory == this.inventory;
    }
}
