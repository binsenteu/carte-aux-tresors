package com.carbon.carteauxtresors.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Treasure extends CellType {
    private int treasureCount;

    public Treasure(Position position, int treasureCount) {
        super(position);
        this.treasureCount = treasureCount;
    }
}
