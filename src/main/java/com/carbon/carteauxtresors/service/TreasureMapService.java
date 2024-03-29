package com.carbon.carteauxtresors.service;

import com.carbon.carteauxtresors.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.carbon.carteauxtresors.TreasureHuntApplication.LOGGER;

@Service
@RequiredArgsConstructor
public class TreasureMapService {
    /**
     * Main method used to buil the treasure map
     * @param inputFile  input file with the treasure hunt data
     * @return treasure map containing mountains and treasures
     * @throws NoSuchElementException if data is not found in the input file
     * @throws IllegalArgumentException if data in the input file is not as expected
     */
    public TreasureMap buildTreasureMap(List<String> inputFile) throws NoSuchElementException, IllegalArgumentException {
        TreasureMap treasureMap = new TreasureMap();

        //Build treasure map size
        treasureMap.setMapSize(buildTreasureMapSize(inputFile));
        LOGGER.info(String.format("Map created with a size of %s", treasureMap.getMapSize()));

        //Build mountains and treasures
        treasureMap.setTreasureMapHashMap(placeMountainsAndTreasures(inputFile, treasureMap.getMapSize()));

        return treasureMap;
    }

    /**
     * Places mountains and treasures on the treasure map
     * @param inputFile input file with the treasure hunt data
     * @param mapSize Position object defining the size of the map
     * @return Hashmap containing mountains and treasures
     */
    private Map<Position, CellType> placeMountainsAndTreasures(List<String> inputFile, Position mapSize) {

        //Build and place mountains
        List<String> mountainLines = inputFile.stream().filter(line -> line.startsWith("M")).toList();
        Map<Position, CellType> treasureMapHashMap = new HashMap<>(buildMountainsHashMap(mountainLines, mapSize));
        List<String> treasureLines = inputFile.stream().filter(line -> line.startsWith("T")).toList();

        //Build and place treasures
        Map<Position, CellType> treasuresHashMap = buildTreasuresHashMap(treasureLines, mapSize);
        for (Position treasurePositon : treasuresHashMap.keySet()) {
            if (treasureMapHashMap.get(treasurePositon) != null)
                LOGGER.warn(String.format("Conflict at position %s. Treasure will overwrite Mountain", treasurePositon.toString()));
        }
        treasureMapHashMap.putAll(treasuresHashMap);

        return treasureMapHashMap;
    }

    /**
     * Builds the treasures from the input file data
     * @param treasureLines lines from the input file containing the treasures' information
     * @param mapSize size of the map
     * @return Map with the position of the treasure as key, and the treasure itself as value
     */
    private Map<Position, CellType> buildTreasuresHashMap(List<String> treasureLines, Position mapSize) {
        Map<Position, CellType> treasureMap = new HashMap<>();
        for (String treasureLine : treasureLines) {
            String[] lineSplit = treasureLine.split(" - ");
            Position treasurePosition = new Position(lineSplit[1], lineSplit[2]);
            if (!treasurePosition.isPositionOutOfBounds(mapSize)) {
                Treasure treasure = new Treasure(treasurePosition, Integer.parseInt(lineSplit[3]));
                treasureMap.put(treasurePosition, treasure);
            } else {
                LOGGER.warn(String.format("Treasure at [%s] is not placed on the map as it is out of bounds",
                        treasurePosition));
            }
        }
        LOGGER.info(String.format("%s treasures built from input data", treasureMap.size()));
        return treasureMap;
    }

    /**
     * Builds the mountains from the input file data
     * @param mountainLines lines from the input file containing the moutains' information
     * @param mapSize size of the map
     * @return Map with the position of the mountain as key, and the treasure itself as value
     */
    private Map<Position, CellType> buildMountainsHashMap(List<String> mountainLines, Position mapSize) {
        Map<Position, CellType> mountainMap = new HashMap<>();
        for (String mountainLine : mountainLines) {
            String[] lineSplit = mountainLine.split(" - ");
            Position mountainPosition = new Position(lineSplit[1], lineSplit[2]);
            if (!mountainPosition.isPositionOutOfBounds(mapSize)) {
                Mountain mountain = new Mountain(mountainPosition);
                mountainMap.put(mountainPosition, mountain);
            } else {
                LOGGER.warn(String.format("Mountain at [%s] is not placed on the map as it is out of bounds",
                        mountainPosition));
            }
        }
        LOGGER.info(String.format("%s mountains build from input data", mountainMap.size()));
        return mountainMap;
    }

    /**
     * Builds the size of the treasure map
     * @param inputFile input file with the treasure hunt data
     * @return Position object describing the size of the map
     * @throws RuntimeException if data in the input file is not as expected
     */
    private Position buildTreasureMapSize(List<String> inputFile) throws RuntimeException {
        String mapLine = getMapLineFromInputFile(inputFile);
        String[] lineSplit = mapLine.split(" - ");
        return new Position(lineSplit[1], lineSplit[2]);
    }

    /**
     * Extract the line regarding the map in the input file
     * @param inputFile input file with the treasure hunt data
     * @return line regarding the map in the input file
     * @throws NoSuchElementException if data is not found in the input file
     */
    private String getMapLineFromInputFile(List<String> inputFile) throws NoSuchElementException {
        Optional<String> mapSizeOptional = inputFile.stream().filter(line -> line.startsWith("C")).findFirst();
        if (mapSizeOptional.isPresent()) {
            return mapSizeOptional.get();
        } else {
            throw new NoSuchElementException("Unable to create map as there is no line starting with 'C' in input file");
        }
    }
}
