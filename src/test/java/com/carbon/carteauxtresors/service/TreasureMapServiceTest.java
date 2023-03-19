package com.carbon.carteauxtresors.service;

import com.carbon.carteauxtresors.entity.Mountain;
import com.carbon.carteauxtresors.entity.Position;
import com.carbon.carteauxtresors.entity.Treasure;
import com.carbon.carteauxtresors.entity.TreasureMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class TreasureMapServiceTest {

    private TreasureMapService treasureMapService;

    @BeforeEach
    void setup() {
        treasureMapService = new TreasureMapService();
    }

    @Test
    void buildTreasureMapTest() {
        List<String> inputFile = new ArrayList<>();
        inputFile.add("C - 3 - 4");
        inputFile.add("M - 1 - 1");
        inputFile.add("M - 6 - 6");
        inputFile.add("T - 2 - 1 - 5");
        inputFile.add("T - 7 - 6 - 1");

        TreasureMap resultTreasureMap = treasureMapService.buildTreasureMap(inputFile);

        assertThat(resultTreasureMap.getMapSize()).isEqualTo(new Position(3, 4));
        assertThat(resultTreasureMap.getTreasureMapHashMap().size()).isEqualTo(2);
        assertThat(resultTreasureMap.getTreasureMapHashMap().get(new Position(1, 1))).isInstanceOf(Mountain.class);
        assertThat(resultTreasureMap.getTreasureMapHashMap().get(new Position(2, 1))).isInstanceOf(Treasure.class);
        assertThat(((Treasure) resultTreasureMap.getTreasureMapHashMap().get(new Position(2, 1))).getTreasureCount())
                .isEqualTo(5);
    }

    @Test
    void buildTreasureMapTreasureOverwriteMountainTest() {
        List<String> inputFile = new ArrayList<>();
        inputFile.add("C - 3 - 4");
        inputFile.add("M - 1 - 1");
        inputFile.add("T - 1 - 1 - 5");

        TreasureMap resultTreasureMap = treasureMapService.buildTreasureMap(inputFile);

        assertThat(resultTreasureMap.getTreasureMapHashMap().size()).isEqualTo(1);
        assertThat(resultTreasureMap.getTreasureMapHashMap().get(new Position(1, 1))).isInstanceOf(Treasure.class);
    }

    @Test
    void buildTreasureMapLineNotFoundTest() {
        List<String> inputFile = new ArrayList<>();
        inputFile.add("A - 3 - 4");

        assertThatThrownBy(() -> treasureMapService.buildTreasureMap(inputFile))
                .isInstanceOf(NoSuchElementException.class) ;
    }

    @Test
    void buildTreasureMapParseSizeFailTest() {
        List<String> inputFile = new ArrayList<>();
        inputFile.add("C - A - 4");

        assertThatThrownBy(() -> treasureMapService.buildTreasureMap(inputFile))
                .isInstanceOf(NumberFormatException.class) ;
    }
}