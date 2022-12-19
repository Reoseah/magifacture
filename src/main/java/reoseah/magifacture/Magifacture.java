package reoseah.magifacture;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reoseah.magifacture.block.ExperienceBlock;
import reoseah.magifacture.fluid.ExperienceFluid;

public class Magifacture implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("magifacture");

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("magifacture:main")) //
            .icon(() -> new ItemStack(Items.EXPERIENCE_BUCKET)).build();

    @Override
    public void onInitialize() {
        Blocks.initialize();
        Items.initialize();

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(entries -> {
            entries.add(Items.EXPERIENCE_BUCKET);
        });
    }

    public static class Blocks {
        public static final Block EXPERIENCE = register("experience", new ExperienceBlock(FabricBlockSettings.of(Material.LAVA, MapColor.LIME).luminance(15).noCollision()));

        private static <T extends Block> T register(String name, T block) {
            return Registry.register(Registries.BLOCK, new Identifier("magifacture", name), block);
        }

        public static void initialize() {
        }
    }

    public static class Items {
        public static final Item EXPERIENCE_BUCKET = register("experience_bucket", new BucketItem(Fluids.EXPERIENCE, new Item.Settings()));

        private static <T extends Item> T register(String name, T item) {
            return Registry.register(Registries.ITEM, new Identifier("magifacture", name), item);
        }

        public static void initialize() {
        }
    }

    public static class Fluids {
        public static final Fluid EXPERIENCE = register("experience", new ExperienceFluid());

        private static <T extends Fluid> T register(String name, T fluid) {
            return Registry.register(Registries.FLUID, new Identifier("magifacture", name), fluid);
        }

        public static void initialize() {
        }
    }

}
