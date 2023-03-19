package com.carbon.carteauxtresors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TreasureHuntApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(TreasureHuntApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(TreasureHuntApplication.class, args);
	}

}
