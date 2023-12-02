package pl.mazak.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class StreamSupport {
    private StreamSupport() {

    }

    public static BufferedReader readFile(String file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }
}
