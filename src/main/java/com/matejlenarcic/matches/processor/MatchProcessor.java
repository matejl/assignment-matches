package com.matejlenarcic.matches.processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MatchProcessor implements Runnable {
    private final List<String[]> lines;
    private volatile List<Match> matches;

    public MatchProcessor() {
        this.lines = new ArrayList<>();
    }

    @Override
    public void run() {
        matches = lines.stream()
            .map(parts -> new Match(
                stripQuotes(parts[0]),
                Long.parseLong(parts[1]),
                parts.length > 2 ? stripQuotes(parts[2]) : null,
                parts.length > 3 ? stripQuotes(parts[3]) : null))
            .sorted(Comparator.comparing(Match::marketId)
                .thenComparing(Match::outcomeId)
                .thenComparing(Match::specifiers)
            )
            .toList();
    }

    public void addLine(String[] parts) {
        lines.add(parts);
    }

    public List<Match> getMatches() {
        return matches;
    }

    private String stripQuotes(String s) {
        return s.replace("'", "");
    }
}
