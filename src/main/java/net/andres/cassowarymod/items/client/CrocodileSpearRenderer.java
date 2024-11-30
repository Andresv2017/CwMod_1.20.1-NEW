package net.andres.cassowarymod.items.client;

import net.andres.cassowarymod.items.custom.CrocodileSpear;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CrocodileSpearRenderer extends GeoItemRenderer<CrocodileSpear> {
    public CrocodileSpearRenderer() {
        super(new CrocodileSpearModel());
    }
}
