package com.carbon.carteauxtresors.controller;

import com.carbon.carteauxtresors.service.TreasureHuntService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TreasureHuntControllerTest {

    @Mock
    TreasureHuntService treasureHuntService;

    private TreasureHuntController treasureHuntController;

    @BeforeEach
    void setup() {
        treasureHuntController = new TreasureHuntController(treasureHuntService);
    }

    @Test
    void launchTreasureHuntSuccessfulTest() {
        assertThat(treasureHuntController.launchTreasureHunt("filename").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void launchTreasureHuntExceptionTest() throws Exception {
        doThrow(Exception.class).when(treasureHuntService).startTreasureHunt(any());
        assertThatThrownBy(() -> treasureHuntController.launchTreasureHunt("filename"))
                .isInstanceOf(ResponseStatusException.class) ;
    }
}