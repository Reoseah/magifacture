package reoseah.magifacture.util;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reoseah.magifacture.mixin.AccessibleBucketItem;
import reoseah.magifacture.mixin.AccessibleEmptyItemFluidStorage;

import java.util.List;

public class FluidUtils {
    public static void tryFillItem(SingleFluidStorage tank, Inventory inventory, int emptySlot, int filledSlot) {
        ItemStack emptyStack = inventory.getStack(emptySlot);
        if (emptyStack.isEmpty()) {
            return;
        }

        if (tank.amount >= FluidConstants.BUCKET && emptyStack.getItem() instanceof AccessibleBucketItem bucket && bucket.getFluid() == Fluids.EMPTY) {
            Fluid fluid = tank.variant.getFluid();
            Item filledBucket = fluid.getBucketItem();
            if ((filledBucket instanceof AccessibleBucketItem accessor && accessor.getFluid() != fluid) || filledBucket.getRecipeRemainder() != emptyStack.getItem()) {
                return;
            }
            if (moveItem(inventory, emptySlot, filledSlot, new ItemStack(filledBucket))) {
                tank.amount -= FluidConstants.BUCKET;
                return;
            }
        }
        Storage<FluidVariant> storage = FluidStorage.ITEM.find(emptyStack, ContainerItemContext.withInitial(emptyStack));
        if (storage instanceof CombinedStorage<?, ?> combined) {
            for (Storage<FluidVariant> part : ((List<Storage<FluidVariant>>) combined.parts)) {
                if (part instanceof AccessibleEmptyItemFluidStorage emptyStorage) {
                    ItemStack filledStack = emptyStorage.getEmptyToFullMapping().apply(ItemVariant.of(emptyStack)).toStack();
                    if (tank.variant.getFluid() == emptyStorage.getInsertableFluid() && tank.amount >= emptyStorage.getInsertableAmount()) {
                        if (moveItem(inventory, emptySlot, filledSlot, filledStack)) {
                            tank.amount -= emptyStorage.getInsertableAmount();
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean moveItem(Inventory inventory, int inputSlot, int outputSlot, ItemStack outputStack) {
        ItemStack currentOutputStack = inventory.getStack(outputSlot);
        if (!currentOutputStack.isEmpty() && !ItemStack.canCombine(currentOutputStack, outputStack)) {
            return false;
        }
        if (!currentOutputStack.isEmpty() && currentOutputStack.getCount() >= Math.min(currentOutputStack.getCount(), inventory.getMaxCountPerStack())) {
            return false;
        }
        inventory.getStack(inputSlot).decrement(1);
        if (currentOutputStack.isEmpty()) {
            inventory.setStack(outputSlot, outputStack);
        } else {
            currentOutputStack.increment(1);
        }
        inventory.markDirty();
        return true;
    }
}
