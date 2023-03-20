package com.carbon.carteauxtresors.service;

import com.carbon.carteauxtresors.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.carbon.carteauxtresors.TreasureHuntApplication.LOGGER;

@Service
@RequiredArgsConstructor
public class AdventurerService {

    /**
     * Main methode to build adventurers from the input file data
     * @param inputFile input file with the treasure hunt data
     * @param treasureMapHashMap treasure map hashmap including mountains and treasures positions
     * @return list of adventurers
     */
    public List<Adventurer> buildAdventurers(List<String> inputFile, Map<Position, CellType> treasureMapHashMap) {
        List<Adventurer> adventurerList = new ArrayList<>();

        List<String> adventurerLines = inputFile.stream().filter(line -> line.startsWith("A")).toList();

        for (String adventurerLine : adventurerLines) {
            try {
                adventurerList.add(buildAdventurer(adventurerLine, treasureMapHashMap));
            } catch (Exception e) {
                LOGGER.warn(String.format("Unable to create adventurer with input : %s. %s", adventurerLine, e.getMessage())); //TODO
            }
        }
        LOGGER.info(String.format("%s adventurers created", adventurerList.size()));
        return adventurerList;
    }

    /**
     * Build individual adventurer
     * @param adventurerLine single adventurer line from the input file data
     * @param treasureMapHashMap treasure map hashmap including mountains and treasures positions
     * @return Individual adventurer
     */
    private Adventurer buildAdventurer(String adventurerLine, Map<Position, CellType> treasureMapHashMap) {
        Adventurer adventurer = new Adventurer();

        String[] lineSplit = adventurerLine.split(" - ");

        adventurer.setName(lineSplit[1]);
        Position adventurerPosition = new Position(lineSplit[2], lineSplit[3]);
        if (treasureMapHashMap.get(adventurerPosition) != null && treasureMapHashMap.get(adventurerPosition) instanceof Mountain) {
            throw new RuntimeException(String.format(
                    "Map at position %s is a mountain. Adventurer %s will not be placed on the map",
                    adventurerPosition, adventurer.getName()));
        }
        adventurer.setPosition(adventurerPosition);
        adventurer.setDirection(Direction.getDirectionFromString(lineSplit[4]));
        adventurer.setMovementSequence(lineSplit[5].toCharArray());

        return adventurer;
    }
}
