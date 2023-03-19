package com.carbon.carteauxtresors.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Adventurer {

    private String name;
    private Position position;
    private Direction direction;
    private char[] movementSequence;
    private int treasureCount = 0;

}
