package com.carbon.carteauxtresors.service;

import com.carbon.carteauxtresors.entity.*;
import com.carbon.carteauxtresors.repository.TreasureHuntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

import static com.carbon.carteauxtresors.TreasureHuntApplication.LOGGER;

@Service
@RequiredArgsConstructor
public class TreasureHuntService {
    private static final String ELEMENT_JOINER = " - ";

    private final TreasureMapService treasureMapService;

    private final AdventurerService adventurerService;

    private final TreasureHuntRepository treasureHuntRepository;

    public void startTreasureHunt(String filename) throws Exception {

        LOGGER.info("[STEP][1/5] Read input file");
        List<String> inputFile = treasureHuntRepository.readInputFile(filename, System.getProperty("user.dir"));

        LOGGER.info("[STEP][2/5] Build treasure map with mountains and treasures");
        TreasureMap treasureMap = treasureMapService.buildTreasureMap(inputFile);

        LOGGER.info("[STEP][3/5] Build adventurers list");
        List<Adventurer> adventurerList = adventurerService.buildAdventurers(inputFile, treasureMap.getTreasureMapHashMap());

        LOGGER.info("[STEP][4/5] Treasure hunt");
        hunt(treasureMap, adventurerList);

        String outputString = buildStringForOutputFile(treasureMap, adventurerList);

        LOGGER.info("[STEP][5/5] Write output file");
        treasureHuntRepository.writeOutputFile(filename, outputString);
    }

    private void hunt(TreasureMap treasureMap, List<Adventurer> adventurerList) {
        int maxTurns = adventurerList.stream().map(adventurer -> adventurer.getMovementSequence().length)
                .max(Integer::compare).orElse(0);

        for (int currentTurn = 0 ; currentTurn < maxTurns ; currentTurn++) {
            for (Adventurer adventurer : adventurerList) {
                if (adventurer.getMovementSequence().length > currentTurn) {
                    try {
                        makeMove(adventurer, currentTurn, treasureMap, adventurerList);
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                    }
                }
            }
        }
    }

    private void makeMove(Adventurer adventurer, int currentTurn, TreasureMap treasureMap, List<Adventurer> adventurerList)
            throws IllegalArgumentException {
        switch (adventurer.getMovementSequence()[currentTurn]) {
            case 'A' -> {
                Position targetPosition = findTargetPosition(adventurer);
                if (isValidTargetPosition(targetPosition, treasureMap, adventurerList)) {
                    adventurer.setPosition(findTargetPosition(adventurer));

                    // Collect treasure if present
                    CellType targetCell = treasureMap.getTreasureMapHashMap().get(adventurer.getPosition());
                    if (targetCell instanceof Treasure && ((Treasure) targetCell).getTreasureCount() > 0) {
                        adventurer.setTreasureCount(adventurer.getTreasureCount() + 1);
                        if (((Treasure) targetCell).getTreasureCount() == 1) {
                            treasureMap.getTreasureMapHashMap().remove(adventurer.getPosition());
                        } else {
                            ((Treasure) targetCell).setTreasureCount(((Treasure) targetCell).getTreasureCount() - 1);
                        }

                    }
                }
            }
            case 'D' -> adventurer.setDirection(adventurer.getDirection().turnRight());
            case 'G' -> adventurer.setDirection(adventurer.getDirection().turnLeft());
            default ->
                    throw new IllegalArgumentException(String.format(
                            "Turn skipped for adventurer %s. Unknown move in adventurer's movement sequence : %s",
                            adventurer.getName(),
                            adventurer.getMovementSequence()[currentTurn]));
        }
    }

    private Position findTargetPosition(Adventurer adventurer) {
        return new Position (adventurer.getPosition().getX() + adventurer.getDirection().getXShift(),
                adventurer.getPosition().getY() + adventurer.getDirection().getYShift());
    }

    private boolean isValidTargetPosition(Position targetPosition , TreasureMap treasureMap, List<Adventurer> adventurerList) {
        if (targetPosition.isPositionOutOfBounds(treasureMap.getMapSize()) ||
                treasureMap.getTreasureMapHashMap().get(targetPosition) instanceof Mountain) return false;

        List<Position> adventurersPositionList = adventurerList.stream().map(Adventurer::getPosition).toList();
        return !adventurersPositionList.contains(targetPosition);
    }

    public String buildStringForOutputFile(TreasureMap treasureMap, List<Adventurer> adventurerList) {
        StringJoiner lineJoiner = new StringJoiner(System.lineSeparator());

        lineJoiner.add(new StringJoiner(ELEMENT_JOINER).add("C")
                .add(treasureMap.getMapSize().getX().toString())
                .add(treasureMap.getMapSize().getY().toString()).toString());

        List<Mountain> mountainList = treasureMap.getTreasureMapHashMap().values().stream()
                .filter(cellType -> cellType instanceof Mountain).map(Mountain.class::cast).toList();

        for (Mountain mountain : mountainList) {
            lineJoiner.add(new StringJoiner(ELEMENT_JOINER).add("M")
                    .add(mountain.getPosition().getX().toString())
                    .add(mountain.getPosition().getY().toString()).toString());
        }

        List<Treasure> treasureList = treasureMap.getTreasureMapHashMap().values().stream()
                .filter(cellType -> cellType instanceof Treasure).map(Treasure.class::cast).toList();

        for (Treasure treasure : treasureList) {
            lineJoiner.add(new StringJoiner(ELEMENT_JOINER).add("T")
                    .add(treasure.getPosition().getX().toString())
                    .add(treasure.getPosition().getY().toString())
                    .add(String.valueOf(treasure.getTreasureCount())).toString());
        }

        for (Adventurer adventurer : adventurerList) {
            lineJoiner.add(new StringJoiner(ELEMENT_JOINER).add("A")
                    .add(adventurer.getName())
                    .add(adventurer.getPosition().getX().toString())
                    .add(adventurer.getPosition().getY().toString())
                    .add(adventurer.getDirection().getLabel())
                    .add(String.valueOf(adventurer.getTreasureCount())).toString());
        }

        return lineJoiner.toString();
    }
}
