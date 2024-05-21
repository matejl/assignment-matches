package com.matejlenarcic.matches.parser;

import com.matejlenarcic.matches.processor.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Parser {
    private final Processor processor;

    public Parser(Processor processor) {
        this.processor = processor;
    }

    public void parseAndProcess(InputStream inputStream) throws IOException, InterruptedException {
        parseAndProcess(inputStream, true);
    }

    public void parseAndProcess(InputStream inputStream, boolean skipHeader)
        throws IOException, InterruptedException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            // the first line can be a header and needs to be skipped
            if (skipHeader) {
                br.readLine();
            }
            String line;
            while ((line = br.readLine()) != null) {
                processor.addLine(line.split("\\|"));
            }
        }
        processor.process();
    }
}
