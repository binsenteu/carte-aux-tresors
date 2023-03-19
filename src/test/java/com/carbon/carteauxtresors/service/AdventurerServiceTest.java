package com.carbon.carteauxtresors.service;

import com.carbon.carteauxtresors.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AdventurerServiceTest {

    private AdventurerService adventurerService;

    @BeforeEach
    void setup() {
        adventurerService = new AdventurerService();
    }

    @Test
    void buildAdventurersNoAdventurerTest() {
        List<String> inputFile = Arrays.asList(
                "C - 3 - 4",
                "A - Lara - A - 1 - S - AADADAGGA",
                "A - Lara - 1 - 1 - K - AADADAGGA");
        assertThat(adventurerService.buildAdventurers(inputFile, new HashMap<>())).isEmpty();
    }

    @Test
    void buildAdventurersTest() {
        List<String> inputFile = List.of("A - Lara - 1 - 1 - S - AADADAGGA");

        List<Adventurer> resultAdventurerList = adventurerService.buildAdventurers(inputFile, new HashMap<>());

        assertThat(resultAdventurerList.size()).isEqualTo(1);

        Adventurer adventurer = resultAdventurerList.get(0);

        assertThat(adventurer.getName()).isEqualTo("Lara");
        assertThat(adventurer.getPosition()).isEqualTo(new Position(1, 1));
        assertThat(adventurer.getDirection()).isEqualTo(Direction.SOUTH);
        assertThat(adventurer.getMovementSequence().length).isEqualTo(9);
    }

    @Test
    void buildAdventurersPositionOccupiedByMountainTest() {
        List<String> inputFile = List.of("A - Lara - 1 - 1 - S - AADADAGGA");
        Map<Position, CellType> treasureMapHashMap = new HashMap<>();
        treasureMapHashMap.put(new Position(1, 1), new Mountain(new Position(1, 1)));

        List<Adventurer> resultAdventurerList = adventurerService.buildAdventurers(inputFile, treasureMapHashMap);

        assertThat(resultAdventurerList).isEmpty();
    }

    @Test
    void buildAdventurersPositionOccupiedByTreasureTest() {
        List<String> inputFile = List.of("A - Lara - 1 - 1 - S - AADADAGGA");
        Map<Position, CellType> treasureMapHashMap = new HashMap<>();
        treasureMapHashMap.put(new Position(1, 1), new Treasure(new Position(1, 1), 3));

        List<Adventurer> resultAdventurerList = adventurerService.buildAdventurers(inputFile, treasureMapHashMap);

        assertThat(resultAdventurerList.size()).isEqualTo(1);
        assertThat(resultAdventurerList.get(0).getTreasureCount()).isEqualTo(0);
        assertThat(((Treasure) treasureMapHashMap.get(new Position(1, 1))).getTreasureCount()).isEqualTo(3);
    }
}