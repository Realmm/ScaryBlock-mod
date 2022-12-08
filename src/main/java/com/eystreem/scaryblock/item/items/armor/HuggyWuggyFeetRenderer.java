package com.eystreem.scaryblock.item.items.armor;

import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class HuggyWuggyFeetRenderer extends GeoArmorRenderer<HuggyWuggyFeetItem> {
    public HuggyWuggyFeetRenderer() {
        super(new HuggyWuggyFeetModel());
        //swap left and right legs
        rightLegBone = "armorLeftLeg";
        rightBootBone = "armorLeftBoot";
        leftBootBone = "armorRightBoot";
        leftLegBone = "armorRightLeg";
    }
}
