package com.carbon.carteauxtresors.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Direction {
    NORTH("N", 0, -1),
    EAST("E", 1, 0),
    SOUTH("S", 0, 1),
    WEST("O", -1, 0);

    private final String label;
    private final int xShift;
    private final int yShift;

    private static final Direction[] directions = values();

    public static Direction getDirectionFromString(String direction) throws IllegalArgumentException {
        for (Direction e : Direction.values()) {
            if (e.getLabel().equals(direction)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unable to find direction from input : " + direction);
    }

    public Direction turnRight() {
        return directions[(this.ordinal() + 1) % directions.length];
    }

    /**
     * Renvoie la direction en tournant à gauche
     *
     * @return la direction en tournant à gauche
     */
    public Direction turnLeft() {
        return directions[(this.ordinal() - 1 + directions.length) % directions.length];
    }
}
