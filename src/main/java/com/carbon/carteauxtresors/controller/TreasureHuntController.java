package com.carbon.carteauxtresors.controller;

import com.carbon.carteauxtresors.service.TreasureHuntService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class TreasureHuntController {

    private final TreasureHuntService treasureHuntService;

    @PostMapping("/start")
    public ResponseEntity<String> launchTreasureHunt(@RequestBody String filename) throws ResponseStatusException {
        try {
            treasureHuntService.startTreasureHunt(filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
