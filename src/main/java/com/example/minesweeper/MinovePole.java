package com.example.minesweeper;

import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Main game field class containing the grid of tiles
 */
public class MinovePole extends GridPane {
    private int sirka;
    private int vyska;
    private int pocetMin;
    private Dlazdica[][] dlazdice;
    private StavPola stav;
    private int pocetTahov;
    private List<Integer> pozicieMin;
    private boolean prvyKlik = true; // Nový flag pre prvý klik


    public MinovePole(int sirka, int vyska, int pocetMin) {
        this.sirka = sirka;
        this.vyska = vyska;
        this.pocetMin = pocetMin;
        this.stav = StavPola.HRANIE;
        this.pocetTahov = 0;
        generujPole();
    }

    private void generujPole() {
        dlazdice = new Dlazdica[vyska][sirka];

//        // Generate mine positions
//        generujMiny();

        // Create tiles
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                if (jeMina(r, s)) {
                    dlazdice[r][s] = new Mina(r, s, this);
                } else {
                    dlazdice[r][s] = new Stopa(r, s, this);
                }
                add(dlazdice[r][s], s, r);
            }
        }

        // Calculate adjacent mine counts
        vypocitajSusedneMiny();
    }

    private void generujMinyPoBezpecneZone(int bezpecnyRiadok, int bezpecnyStlpec) {
        pozicieMin = new ArrayList<>();
        Random random = new Random();

        // Vytvoríme bezpečnú zónu 3x3 okolo prvého kliku
        boolean[][] bezpecnaZona = new boolean[vyska][sirka];
        for (int r = Math.max(0, bezpecnyRiadok - 1); r <= Math.min(vyska - 1, bezpecnyRiadok + 1); r++) {
            for (int s = Math.max(0, bezpecnyStlpec - 1); s <= Math.min(sirka - 1, bezpecnyStlpec + 1); s++) {
                bezpecnaZona[r][s] = true;
            }
        }

        int umiestneneMin = 0;
        while (umiestneneMin < pocetMin) {
            int r = random.nextInt(vyska);
            int s = random.nextInt(sirka);
            int pozicia = r * sirka + s;

            if (!bezpecnaZona[r][s] && !pozicieMin.contains(pozicia)) {
                pozicieMin.add(pozicia);

                // Nahradíme Stopa mínou
                getChildren().remove(dlazdice[r][s]);
                dlazdice[r][s] = new Mina(r, s, this);
                add(dlazdice[r][s], s, r);

                umiestneneMin++;
            }
        }

        // Prepočítame susedné míny
        vypocitajSusedneMiny();
    }

    public void spracujPrvyKlik(int riadok, int stlpec) {
        if (prvyKlik) {
            generujMinyPoBezpecneZone(riadok, stlpec);
            prvyKlik = false;
        }
    }


    private void generujMiny() {
        pozicieMin = new ArrayList<>();
        for (int i = 0; i < sirka * vyska; i++) {
            pozicieMin.add(i);
        }
        Collections.shuffle(pozicieMin);
        pozicieMin = pozicieMin.subList(0, pocetMin);
    }

    private boolean jeMina(int riadok, int stlpec) {
        return dlazdice[riadok][stlpec] instanceof Mina;
    }

    public boolean isPrvyKlik() {
        return prvyKlik;
    }

    private void vypocitajSusedneMiny() {
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                if (dlazdice[r][s] instanceof Stopa) {
                    int pocet = 0;
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

    public void otvorSusedne(int riadok, int stlpec) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int ds = -1; ds <= 1; ds++) {
                int nr = riadok + dr;
                int ns = stlpec + ds;
                if (nr >= 0 && nr < vyska && ns >= 0 && ns < sirka) {
                    if (dlazdice[nr][ns].getStav() == StavDlazdice.ZATVORENE) {
                        dlazdice[nr][ns].otvorit();
                    }
                }
            }
        }
    }

    public void odhalVsetkyMiny() {
        for (int r = 0; r < vyska; r++) {
            for (int s = 0; s < sirka; s++) {
                if (dlazdice[r][s] instanceof Mina) {
                    dlazdice[r][s].setStav(StavDlazdice.OTVORENE);
                }
            }
        }
    }

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

        if (otvoreneNeminy == (sirka * vyska - pocetMin)) {
            stav = StavPola.VYHRANE;
        }
    }

    public void pridajTah() {
        pocetTahov++;
    }

    // Getters and setters
    public StavPola getStav() { return stav; }
    public void setStav(StavPola stav) { this.stav = stav; }
    public int getPocetTahov() { return pocetTahov; }
    public int getSirka() { return sirka; }
    public int getVyska() { return vyska; }
    public int getPocetMin() { return pocetMin; }
}
