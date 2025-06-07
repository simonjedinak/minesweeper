package com.example.minesweeper;

import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Hlavná trieda herného poľa obsahujúca mriežku dlaždíc
 * Implementuje logiku hry a používa kolekcie
 */
public class MinovePole extends GridPane {
    // Zapuzdrené atribúty
    private int sirka;              // Šírka herného poľa
    private int vyska;              // Výška herného poľa
    private int pocetMin;           // Počet mín na poli
    private Dlazdica[][] dlazdice;  // 2D pole dlazdíc - polymorfizmus
    private StavPola stav;          // Stav hry (enum)
    private int pocetTahov;         // Počítadlo ťahov
    private List<Integer> pozicieMin; // Kolekcia pozícií mín
    private boolean prvyKlik = true; // Flag pre prvý klik

    /**
     * Konštruktor herného poľa
     * @param sirka šírka poľa
     * @param vyska výška poľa
     * @param pocetMin počet mín
     */
    public MinovePole(int sirka, int vyska, int pocetMin) {
        this.sirka = sirka;
        this.vyska = vyska;
        this.pocetMin = pocetMin;
        this.stav = StavPola.HRANIE;
        this.pocetTahov = 0;
        generujPole();
    }

    /**
     * Generovanie herného poľa s dlaždicami
     * Používa polymorfizmus - vytvára objekty Mina a Stopa
     */
    private void generujPole() {
        dlazdice = new Dlazdica[vyska][sirka];

        // Vytvorenie dlaždíc - polymorfizmus
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                // Zatiaľ vytvárame len obyčajné dlazdice
                dlazdice[r][s] = new Stopa(r, s, this);
                add(dlazdice[r][s], s, r);
            }
        }
    }

    /**
     * Generovanie mín po prvom kliku mimo bezpečnej zóny
     * Zabezpečuje, že prvý klik nebude na mínu
     * @param bezpecnyRiadok riadok prvého kliku
     * @param bezpecnyStlpec stĺpec prvého kliku
     */
    private void generujMinyPoBezpecneZone(int bezpecnyRiadok, int bezpecnyStlpec) {
        pozicieMin = new ArrayList<>();  // Použitie kolekcie
        Random random = new Random();

        // Vytvorenie bezpečnej zóny 3x3 okolo prvého kliku
        boolean[][] bezpecnaZona = new boolean[vyska][sirka];
        for (int r = Math.max(0, bezpecnyRiadok - 1); r <= Math.min(vyska - 1, bezpecnyRiadok + 1); r++) {
            for (int s = Math.max(0, bezpecnyStlpec - 1); s <= Math.min(sirka - 1, bezpecnyStlpec + 1); s++) {
                bezpecnaZona[r][s] = true;
            }
        }

        // Umiestnenie mín mimo bezpečnej zóny
        int umiestneneMin = 0;
        while (umiestneneMin < pocetMin) {
            int r = random.nextInt(vyska);
            int s = random.nextInt(sirka);
            int pozicia = r * sirka + s;

            if (!bezpecnaZona[r][s] && !pozicieMin.contains(pozicia)) {
                pozicieMin.add(pozicia);  // Pridanie do kolekcie

                // Nahradenie Stopa mínou - polymorfizmus
                getChildren().remove(dlazdice[r][s]);
                dlazdice[r][s] = new Mina(r, s, this);
                add(dlazdice[r][s], s, r);

                umiestneneMin++;
            }
        }

        // Prepočítanie susedných mín
        vypocitajSusedneMiny();
    }

    /**
     * Spracovanie prvého kliku v hre
     * @param riadok riadok prvého kliku
     * @param stlpec stĺpec prvého kliku
     */
    public void spracujPrvyKlik(int riadok, int stlpec) {
        if (prvyKlik) {
            generujMinyPoBezpecneZone(riadok, stlpec);
            prvyKlik = false;
        }
    }

    /**
     * Kontrola, či je prvý klik
     * @return true ak je prvý klik
     */
    public boolean isPrvyKlik() {
        return prvyKlik;
    }

    /**
     * Výpočet počtu susedných mín pre každú obyčajnú dlazdicu
     */
    private void vypocitajSusedneMiny() {
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                // Polymorfizmus - kontrola typu objektu
                if (dlazdice[r][s] instanceof Stopa) {
                    int pocet = 0;
                    // Kontrola všetkých 8 susedných dlaždíc
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int ds = -1; ds <= 1; ds++) {
                            int nr = r + dr;
                            int ns = s + ds;
                            if (nr >= 0 && nr < vyska && ns >= 0 && ns < sirka &&
                                    dlazdice[nr][ns] instanceof Mina) {
                                pocet++;
                            }
                        }
                    }
                    ((Stopa) dlazdice[r][s]).setPocetSusednychMin(pocet);
                }
            }
        }
    }

    /**
     * Otvorenie susedných dlaždíc (pre prázdne dlazdice)
     * @param riadok stredový riadok
     * @param stlpec stredový stĺpec
     */
    public void otvorSusedne(int riadok, int stlpec) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int ds = -1; ds <= 1; ds++) {
                int nr = riadok + dr;
                int ns = stlpec + ds;
                if (nr >= 0 && nr < vyska && ns >= 0 && ns < sirka) {
                    if (dlazdice[nr][ns].getStav() == StavDlazdice.ZATVORENE) {
                        dlazdice[nr][ns].otvorit();  // Rekurzívne otváranie
                    }
                }
            }
        }
    }

    /**
     * Odhalenie všetkých mín po prehre
     */
    public void odhalVsetkyMiny() {
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                // Polymorfizmus - kontrola typu
                if (dlazdice[r][s] instanceof Mina) {
                    dlazdice[r][s].setStav(StavDlazdice.OTVORENE);
                }
            }
        }
    }

    /**
     * Kontrola výhernej podmienky
     * Výhra = všetky dlazdice okrem mín sú otvorené
     */
    public void skontrolujVyhru() {
        int otvoreneNeminy = 0;
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                if (!(dlazdice[r][s] instanceof Mina) &&
                        dlazdice[r][s].getStav() == StavDlazdice.OTVORENE) {
                    otvoreneNeminy++;
                }
            }
        }

        // Ak sú otvorené všetky dlazdice okrem mín = výhra
        if (otvoreneNeminy == (sirka * vyska - pocetMin)) {
            stav = StavPola.VYHRANE;
        }
    }

    /**
     * Pridanie ťahu do počítadla
     */
    public void pridajTah() {
        pocetTahov++;
    }

    // Gettery a settery pre zapuzdrenie
    public StavPola getStav() { return stav; }
    public void setStav(StavPola stav) { this.stav = stav; }
    public int getPocetTahov() { return pocetTahov; }
    public int getSirka() { return sirka; }
    public int getVyska() { return vyska; }
    public int getPocetMin() { return pocetMin; }
}
