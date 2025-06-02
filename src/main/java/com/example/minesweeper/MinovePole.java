package com.example.minesweeper;

import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private List<Integer> pozicieMín;

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

        // Generate mine positions
        generujMiny();

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
        vypocitajSusedneMiný();
    }

    private void generujMiny() {
        pozicieMín = new ArrayList<>();
        for (int i = 0; i < sirka * vyska; i++) {
            pozicieMín.add(i);
        }
        Collections.shuffle(pozicieMín);
        pozicieMín = pozicieMín.subList(0, pocetMin);
    }

    private boolean jeMina(int riadok, int stlpec) {
        int pozicia = riadok * sirka + stlpec;
        return pozicieMín.contains(pozicia);
    }

    private void vypocitajSusedneMiný() {
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
