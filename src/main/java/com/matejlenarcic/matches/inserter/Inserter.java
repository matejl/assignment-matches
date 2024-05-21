package com.matejlenarcic.matches.inserter;

import com.matejlenarcic.matches.processor.Match;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class Inserter {
    private static final int N_FIELDS = 5;
    private Instant minDateInsert;
    private Instant dateInsert;

    public void insertAll(Connection connection, List<Match> matches) {
        StringBuilder sql = new StringBuilder("INSERT INTO match_data (match_id, market_id, outcome_id, specifiers, date_insert) VALUES ");
        for (int i = 0; i < matches.size(); i++) {
            sql.append("(");
            for (int j = 1; j <= N_FIELDS; j++) {
                sql.append("?").append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append("),");
        }
        sql.deleteCharAt(sql.length() - 1);

        int i = 1;
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (Match match : matches) {
                dateInsert = Instant.now();

                stmt.setString(i++, match.matchId());
                stmt.setLong(i++, match.marketId());
                stmt.setString(i++, match.outcomeId());
                stmt.setString(i++, match.specifiers());
                stmt.setTimestamp(i++, Timestamp.from(dateInsert));

                if (minDateInsert == null) {
                    minDateInsert = dateInsert;
                }
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error when executing SQL", e);
        }
    }

    public Instant getMinDateInsert() {
        return minDateInsert;
    }

    public Instant getMaxDateInsert() {
        return dateInsert;
    }
}
