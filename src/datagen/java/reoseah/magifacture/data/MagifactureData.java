package reoseah.magifacture.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import reoseah.magifacture.Magifacture;

public class MagifactureData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.createPack().addProvider(ModelProvider::new);
    }

    public static class ModelProvider extends FabricModelProvider {
        public ModelProvider(FabricDataOutput generator) {
            super(generator);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator gen) {
            gen.registerSimpleCubeAll(Magifacture.Blocks.EXPERIENCE);
        }

        @Override
        public void generateItemModels(ItemModelGenerator gen) {
            gen.register(Magifacture.Items.EXPERIENCE_BUCKET, Models.GENERATED);
        }
    }
}


