package org.qme.world.res;

import org.qme.world.TileType;

public class ResourceCoal extends AbstractResource {
    ResourceCoal() {
        super();
    }

    @Override
    public int getSpawnChance(TileType type) {
        if (type == TileType.HIGH_MOUNTAIN) {
            return 20;
        } else if (type == TileType.MOUNTAIN) {
            return 50;
        } else {
            return 0;
        }
    }

    @Override
    public ResourceType getType() {
        return ResourceType.Coal;
    }

    @Override
    public String getTexturePath() {
        return "Coal.png";
    }
}