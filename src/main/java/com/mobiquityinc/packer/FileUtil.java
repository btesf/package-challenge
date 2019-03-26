package com.mobiquityinc.packer;

import com.mobiquityinc.packer.exception.APIException;

import java.io.*;
import java.util.stream.Collectors;

public interface FileUtil {

    /**
     * Reads file content and return content as String
     *
     * @return
     */
    static String getTextFromFile(String path) throws APIException {

        File file = new File(path);
        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(file));

        } catch (FileNotFoundException e) {

            throw new APIException("File '" + path + "' not found.");
        }

        //Read lines from file and join them by system specific line separator
        String lines = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        return lines;
    }
}
