package com.matejlenarcic.matches.processor;

import com.matejlenarcic.matches.DB;
import com.matejlenarcic.matches.inserter.Inserter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Processor {
    private final Map<String, MatchProcessor> matchWorkers = new HashMap<>();

    private final Inserter inserter;

    private static final String WORKER_PREFIX = "worker-";

    public Processor(Inserter inserter) {
        this.inserter = inserter;
    }

    public void addLine(String[] parts) {
        MatchProcessor matchProcessor;

        String key = WORKER_PREFIX + parts[0];
        if (!matchWorkers.containsKey(key)) {
            matchProcessor = new MatchProcessor();
            matchWorkers.put(key, matchProcessor);
        } else {
            matchProcessor = matchWorkers.get(key);
        }
        matchProcessor.addLine(parts);
    }

    public void process() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (Map.Entry<String, MatchProcessor> entry : matchWorkers.entrySet()) {
            threads.add(Thread.ofVirtual()
                .name(entry.getKey())
                .start(entry.getValue()));
        }

        try (Connection connection = DB.connect()) {
            for (Thread thread : threads) {
                thread.join();
                List<Match> matches = matchWorkers.get(thread.getName()).getMatches();
                inserter.insertAll(Objects.requireNonNull(connection), matches);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to connect to DB", e);
        }
    }
}
