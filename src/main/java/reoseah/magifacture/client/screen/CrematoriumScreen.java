package reoseah.magifacture.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reoseah.magifacture.screen.CrematoriumScreenHandler;

@Environment(EnvType.CLIENT)
public class CrematoriumScreen extends MagifactureScreen<CrematoriumScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("magifacture", "textures/gui/crematorium.png");

    public CrematoriumScreen(CrematoriumScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.addTankDisplay(handler.getTank(), 116, 18, 16, 50);
    }

    @Override
    protected Identifier getTextureId() {
        return TEXTURE;
    }

    @Override
    protected int getTankOverlayU() {
        return 208;
    }
}
