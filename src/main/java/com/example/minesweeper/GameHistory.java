package com.example.minesweeper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to store game history using collections
 */
public class GameHistory {
    private static GameHistory instance;
    private List<GameRecord> historia;

    private GameHistory() {
        historia = new ArrayList<>();
    }

    public static GameHistory getInstance() {
        if (instance == null) {
            instance = new GameHistory();
        }
        return instance;
    }

    public void pridajHru(int sirka, int vyska, int pocetMin, int pocetTahov,
                          StavPola vysledok, long trvanie) {
        GameRecord record = new GameRecord(sirka, vyska, pocetMin, pocetTahov,
                vysledok, trvanie, LocalDateTime.now());
        historia.add(record);
    }

    public List<GameRecord> getHistoria() {
        return new ArrayList<>(historia);
    }

    public void vymazHistoriu() {
        historia.clear();
    }

    /**
     * Inner class representing a single game record
     */
    public static class GameRecord {
        private final int sirka;
        private final int vyska;
        private final int pocetMin;
        private final int pocetTahov;
        private final StavPola vysledok;
        private final long trvanie; // in seconds
        private final LocalDateTime cas;

        public GameRecord(int sirka, int vyska, int pocetMin, int pocetTahov,
                          StavPola vysledok, long trvanie, LocalDateTime cas) {
            this.sirka = sirka;
            this.vyska = vyska;
            this.pocetMin = pocetMin;
            this.pocetTahov = pocetTahov;
            this.vysledok = vysledok;
            this.trvanie = trvanie;
            this.cas = cas;
        }

        // Getters
        public int getSirka() { return sirka; }
        public int getVyska() { return vyska; }
        public int getPocetMin() { return pocetMin; }
        public int getPocetTahov() { return pocetTahov; }
        public StavPola getVysledok() { return vysledok; }
        public long getTrvanie() { return trvanie; }
        public LocalDateTime getCas() { return cas; }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            return String.format("%s - %dx%d (%d mín) - %d ťahov - %s - %d sek",
                    cas.format(formatter), sirka, vyska, pocetMin,
                    pocetTahov, vysledok, trvanie);
        }
    }
}

