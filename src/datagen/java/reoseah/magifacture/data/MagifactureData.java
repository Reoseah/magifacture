package reoseah.magifacture.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MagifactureData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.createPack().addProvider(MagifactureModelProvider::new);
        generator.createPack().addProvider(MagifactureBlockLootTableProvider::new);
    }
}


