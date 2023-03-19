package com.carbon.carteauxtresors.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TreasureMap {
    private Position mapSize;
    private Map<Position, CellType> treasureMapHashMap;

}
