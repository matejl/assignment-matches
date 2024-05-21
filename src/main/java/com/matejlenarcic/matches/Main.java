package com.matejlenarcic.matches;

import com.matejlenarcic.matches.inserter.Inserter;
import com.matejlenarcic.matches.parser.Parser;
import com.matejlenarcic.matches.processor.Processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Inserter inserter = new Inserter();
        Processor processor = new Processor(inserter);
        Parser parser = new Parser(processor);

        String sourcePath = getSourcePath(args);
        try {
            parser.parseAndProcess(Files.newInputStream(Paths.get(sourcePath)));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Done");

        System.out.printf("Min date insert = %s, max date insert = %s",
            inserter.getMinDateInsert(), inserter.getMaxDateInsert());
    }

    private static String getSourcePath(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Please provide input file name as a first argument");
        }
        return args[0];
    }
}