package com.carbon.carteauxtresors.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.carbon.carteauxtresors.TreasureHuntApplication.LOGGER;

@Repository
public class TreasureHuntRepository {
    /**
     * Reads a file
     * @param filename name of the file to read
     * @param filepath filepath of the file to read
     * @return List of String from the file
     * @throws IOException if an I/O error occurs opening the file
     */
    public List<String> readInputFile(String filename, String filepath) throws IOException {
        LOGGER.info(String.format("Reading input file [%s]", filename));
        Path path = Paths.get(filepath + File.separator + filename);
        Stream<String> lines = Files.lines(path);
        List<String> data = lines.collect(Collectors.toList());
        lines.close();

        LOGGER.info(String.format("Read successful for file [%s]", filename));
        return data;
    }

    /**
     * Writes a file to the user directory
     * @param filename name of the file to write. A prefix "output-" will be added to the file name.
     * @param fileContent content of the file
     * @throws IOException if an I/O error occurs
     */
    public void writeOutputFile(String filename, String fileContent) throws IOException {

        String outputFileName = Paths.get("output-" + filename).toString();

        LOGGER.info(String.format("Writing output file [%s]", outputFileName));

        FileOutputStream fos = new FileOutputStream(outputFileName);
        byte[] fileContentBytes = fileContent.getBytes();
        fos.write(fileContentBytes);
        LOGGER.info(String.format("Write successful for output file [%s]", outputFileName));
    }
}
