package org.qme.world.res;

import org.qme.world.TileType;

/**
 * An abstract class for resources
 * @author jakeroggenbuck
 * @since 0.3.0
 */
public class AbstractResource {

    /**
     * Which type it is
     */
    public ResourceType type;

    public AbstractResource(ResourceType type) {
        this.type = type;
    }

    /**
     * Spawn chance is a whole number, 0-100
     * @return
     */
    public static int getSpawnChance(ResourceType type, TileType tileType) {
        return switch (type) {
            case COAL ->
                    switch (tileType) {
                        case MOUNTAIN -> 50;
                        case HIGH_MOUNTAIN -> 20;
                        default -> 0;
                    };
            case FRUIT ->
                    switch (tileType) {
                        case FOREST -> 20;
                        default -> 0;
                    };
            case GOLD_ORE ->
                    switch (tileType) {
                        case MOUNTAIN -> 10;
                        case HIGH_MOUNTAIN -> 20;
                        default -> 0;
                    };
            case GRAPE ->
                    switch (tileType) {
                        case FERTILE_PLAINS -> 10;
                        default -> 0;
                    };
            case GRASS ->
                    switch (tileType) {
                        case PLAINS -> 70;
                        default -> 0;
                    };
            case LEAF ->
                    switch (tileType) {
                        case FOREST -> 80;
                        default -> 0;
                    };
            case SALT ->
                    switch (tileType) {
                        case OCEAN -> 200;
                        default -> 0;
                    };
            case SEA_WEED ->
                    switch (tileType) {
                        case OCEAN -> 30;
                        case SEA -> 25;
                        default -> 0;
                    };
            case TALL_GRASS ->
                    switch (tileType) {
                        case FERTILE_PLAINS -> 50;
                        default -> 0;
                    };
        };
    }

    public ResourceType getType() {
        return type;
    }

    public String getTexturePath() {
        return "items/" + (type.name().toLowerCase()) + ".png";
    }
}
