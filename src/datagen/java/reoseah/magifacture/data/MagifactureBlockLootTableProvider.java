package reoseah.magifacture.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import reoseah.magifacture.Magifacture;

class MagifactureBlockLootTableProvider extends FabricBlockLootTableProvider {
    public MagifactureBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        this.addDrop(Magifacture.Blocks.CREMATORIUM);
        this.addDrop(Magifacture.Blocks.ALEMBIC);
    }
}
