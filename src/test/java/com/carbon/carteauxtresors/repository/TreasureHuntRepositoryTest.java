package com.carbon.carteauxtresors.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TreasureHuntRepositoryTest {

    private TreasureHuntRepository treasureHuntRepository;

    @BeforeEach
    void setup() {
        treasureHuntRepository = new TreasureHuntRepository();
    }

    @Test
    void readInputFileTest() throws IOException {
        List<String> result = treasureHuntRepository.readInputFile("test.txt", "src/test/resources");

        assertThat(result.size()).isEqualTo(6);
    }

    @Test
    void readInputFileNotFoundTest() {
        assertThatThrownBy(() ->
                treasureHuntRepository.readInputFile("testNotFound.txt", "src/test/resources"))
                .isInstanceOf(NoSuchFileException.class);
    }
}