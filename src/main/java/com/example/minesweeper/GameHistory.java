package com.example.minesweeper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda pre uchovanie histórie hier pomocou kolekcií
 * Implementuje Singleton pattern pre globálny prístup
 * Spĺňa požiadavku na dátovú vrstvu s kolekciami
 */
public class GameHistory {
    private static GameHistory instance;  // Singleton inštancia
    private List<GameRecord> historia;    // Kolekcia záznamov hier

    /**
     * Súkromný konštruktor pre Singleton pattern
     */
    private GameHistory() {
        historia = new ArrayList<>();  // Inicializácia kolekcie
    }

    /**
     * Získanie jedinej inštancie triedy (Singleton)
     * @return inštancia GameHistory
     */
    public static GameHistory getInstance() {
        if (instance == null) {
            instance = new GameHistory();
        }
        return instance;
    }

    /**
     * Pridanie novej hry do histórie
     * @param sirka šírka herného poľa
     * @param vyska výška herného poľa
     * @param pocetMin počet mín
     * @param pocetTahov počet ťahov
     * @param vysledok výsledok hry (enum)
     * @param trvanie trvanie hry v sekundách
     */
    public void pridajHru(int sirka, int vyska, int pocetMin, int pocetTahov,
                          StavPola vysledok, long trvanie) {
        GameRecord record = new GameRecord(sirka, vyska, pocetMin, pocetTahov,
                vysledok, trvanie, LocalDateTime.now());
        historia.add(record);  // Pridanie do kolekcie
    }

    /**
     * Získanie kópie histórie hier
     * @return kópia zoznamu hier
     */
    public List<GameRecord> getHistoria() {
        return new ArrayList<>(historia);  // Vracia kópiu pre zapuzdrenie
    }

    /**
     * Vymazanie celej histórie
     */
    public void vymazHistoriu() {
        historia.clear();
    }

    /**
     * Vnorená trieda reprezentujúca jeden záznam hry
     * Zapuzdruje všetky údaje o hre
     */
    public static class GameRecord {
        // Zapuzdrené atribúty - final pre nemennosť
        private final int sirka;
        private final int vyska;
        private final int pocetMin;
        private final int pocetTahov;
        private final StavPola vysledok;
        private final long trvanie; // v sekundách
        private final LocalDateTime cas;

        /**
         * Konštruktor záznamu hry
         */
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

        // Gettery pre zapuzdrenie
        public int getSirka() { return sirka; }
        public int getVyska() { return vyska; }
        public int getPocetMin() { return pocetMin; }
        public int getPocetTahov() { return pocetTahov; }
        public StavPola getVysledok() { return vysledok; }
        public long getTrvanie() { return trvanie; }
        public LocalDateTime getCas() { return cas; }

        /**
         * Textová reprezentácia záznamu
         * @return formátovaný string s údajmi o hre
         */
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            return String.format("%s - %dx%d (%d mín) - %d ťahov - %s - %d sek",
                    cas.format(formatter), sirka, vyska, pocetMin,
                    pocetTahov, vysledok, trvanie);
        }
    }
}
