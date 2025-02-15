package edu.hawaii.its.api.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatternPropertyChecker {

    private static final Log logger = LogFactory.getLog(PatternPropertyChecker.class);


    /**
     * Helper - checkForPasswords
     * getPatternLocation: checks a file(s) and given file naming convention(.properties, .java, .pom)
     * for a pattern.
     *
     * @param fileExtension  The file type(.java, .properties, .pom, etc).
     * @param folderLocation The folder location(/src/main/resources).
     * @return A list of strings containing locations of the found patterns.
     */
    public List<String> getPatternLocation(String folderLocation, String fileExtension) {
        final String pattern = "^.*password.*\\=(?!\\s*$).+";

        logger.info("fileLocations;  fileExtension: " + fileExtension);
        logger.info("fileLocations; folderLocation: " + folderLocation);
        logger.info("fileLocations;        pattern: " + pattern);

        List<String> patternLocation = new ArrayList<>();

        try {
            File dir = new File(folderLocation);

            File[] fileResources = dir.listFiles((dir1, name) -> name.endsWith(fileExtension));
            if (fileResources != null) {
                Pattern pat = Pattern.compile(pattern);
                Matcher matcher;

                for (File fr : fileResources) {
                    logger.info("fileLocations; scan file: " + fr);
                    int lineId = 0;
                    List<Integer> lineNumbers = new ArrayList<>();
                    // The try...when closes the scanner after exiting the try
                    try (Scanner fileScanner = new Scanner(fr)) {
                        while (fileScanner.hasNextLine()) {
                            String line = fileScanner.nextLine();
                            lineId++;

                            matcher = pat.matcher(line);

                            if (matcher.find()) {
                                lineNumbers.add(lineId);
                            }
                        }
                    }
                    if (!lineNumbers.isEmpty()) {
                        for (int li : lineNumbers) {
                            patternLocation.add(fr + " on line: " + li);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
        }
        return patternLocation.stream().sorted().collect(Collectors.toList());
    }
}