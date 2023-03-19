package com.carbon.carteauxtresors.service;

import com.carbon.carteauxtresors.entity.*;
import com.carbon.carteauxtresors.repository.TreasureHuntRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasureHuntServiceTest {

    @Mock
    private TreasureMapService treasureMapService;
    @Mock
    private AdventurerService adventurerService;
    @Mock
    private TreasureHuntRepository treasureHuntRepository;
    private TreasureHuntService treasureHuntService;

    @BeforeEach
    void setup() {
        treasureHuntService = new TreasureHuntService(treasureMapService, adventurerService, treasureHuntRepository);
    }

    @Test
    void startTreasureHuntTest() throws Exception {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        Map<Position, CellType> treasureMapHashMap = new HashMap<>();
        treasureMapHashMap.put(new Position(1, 0 ), new Mountain(new  Position(1, 0 )));
        treasureMapHashMap.put(new Position(2, 1 ), new Mountain(new  Position(2, 1 )));
        treasureMapHashMap.put(new Position(0, 3 ), new Treasure(new  Position(0, 3 ), 2));
        treasureMapHashMap.put(new Position(1, 3 ), new Treasure(new  Position(1, 3 ), 3));
        treasureMap.setTreasureMapHashMap(treasureMapHashMap);

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 1), Direction.SOUTH,
                new char[]{'A', 'A', 'D', 'A', 'D', 'A', 'G', 'G', 'A'}, 0));


        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(treasureMapHashMap.size()).isEqualTo(3);
        assertThat(((Treasure) treasureMapHashMap.get(new Position(1, 3 ))).getTreasureCount()).isEqualTo(2);
        assertThat(adventurerList.get(0).getPosition()).isEqualTo(new Position(0, 3));
        assertThat(adventurerList.get(0).getDirection()).isEqualTo(Direction.SOUTH);
        assertThat(adventurerList.get(0).getTreasureCount()).isEqualTo(3);
    }


    @Test
    void startTreasureHuntWithTurnSKippedTest() throws Exception {

        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        treasureMap.setTreasureMapHashMap(new HashMap<>());

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 1), Direction.SOUTH,
                new char[]{'A', 'B'}, 0));

        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(adventurerList.get(0).getPosition()).isEqualTo(new Position(1, 2));
    }

    @Test
    void startTreasureHuntBlockedByMountainTest() throws Exception {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        Map<Position, CellType> treasureMapHashMap = new HashMap<>();
        treasureMapHashMap.put(new Position(1, 2 ), new Mountain(new  Position(1, 0 )));
        treasureMap.setTreasureMapHashMap(treasureMapHashMap);

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 1), Direction.SOUTH,
                new char[]{'A'}, 0));

        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(adventurerList.get(0).getPosition()).isEqualTo(new Position(1, 1));
    }

    @Test
    void startTreasureHuntBlockedByOtherAdventurerTest() throws Exception {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        treasureMap.setTreasureMapHashMap(new HashMap<>());

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 1), Direction.SOUTH,
                new char[]{'A'}, 0));
        adventurerList.add(new Adventurer("Samantha", new Position(1, 2), Direction.SOUTH,
                new char[]{'A'}, 0));

        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(adventurerList.get(0).getName()).isEqualTo("Lara");
        assertThat(adventurerList.get(0).getPosition()).isEqualTo(new Position(1, 1));
    }

    @Test
    void startTreasureHuntMoveOutOfBoundTest() throws Exception {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        treasureMap.setTreasureMapHashMap(new HashMap<>());

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 3), Direction.SOUTH,
                new char[]{'A'}, 0));

        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(adventurerList.get(0).getPosition()).isEqualTo(new Position(1, 3));
    }

    @Test
    void startTreasureHuntTurnRightTest() throws Exception {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        treasureMap.setTreasureMapHashMap(new HashMap<>());

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 3), Direction.SOUTH,
                new char[]{'D'}, 0));

        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(adventurerList.get(0).getDirection()).isEqualTo(Direction.WEST);
    }

    @Test
    void startTreasureHuntTurnLeftTest() throws Exception {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        treasureMap.setTreasureMapHashMap(new HashMap<>());

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(1, 3), Direction.SOUTH,
                new char[]{'G'}, 0));

        when(treasureHuntRepository.readInputFile(any(), any())).thenReturn(new ArrayList<>());
        when(treasureMapService.buildTreasureMap(any())).thenReturn(treasureMap);
        when(adventurerService.buildAdventurers(any(), any())).thenReturn(adventurerList);

        treasureHuntService.startTreasureHunt("filename");

        assertThat(adventurerList.get(0).getDirection()).isEqualTo(Direction.EAST);
    }

    @Test
    void buildStringForOutputFileTest() {
        TreasureMap treasureMap = new TreasureMap();
        treasureMap.setMapSize(new Position(3, 4));
        Map<Position, CellType> treasureMapHashMap = new HashMap<>();
        treasureMapHashMap.put(new Position(1, 0), new Mountain(new Position(1, 0)));
        treasureMapHashMap.put(new Position(2, 1), new Mountain(new Position(2, 1)));
        treasureMapHashMap.put(new Position(1, 3), new Treasure(new Position(1, 3), 2));
        treasureMap.setTreasureMapHashMap(treasureMapHashMap);

        List<Adventurer> adventurerList = new ArrayList<>();
        adventurerList.add(new Adventurer("Lara", new Position(0, 3), Direction.SOUTH,
                new char[]{'A', 'A', 'D', 'A', 'D', 'A', 'G', 'G', 'A'}, 3));

        String result = treasureHuntService.buildStringForOutputFile(treasureMap, adventurerList);

        String expectedResult = new StringJoiner(System.lineSeparator())
                .add("C - 3 - 4")
                .add("M - 1 - 0")
                .add("M - 2 - 1")
                .add("T - 1 - 3 - 2")
                .add("A - Lara - 0 - 3 - S - 3").toString();

        assertThat(result).isEqualTo(expectedResult);
    }
}