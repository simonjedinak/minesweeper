package com.example.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;

/**
 * Abstraktná základná trieda pre všetky dlaždice v hre míny
 * Demonštruje dedičnosť a polymorfizmus
 * Zapuzdruje základné vlastnosti každej dlazdice
 */
public abstract class Dlazdica extends Button {
    // Zapuzdrené atribúty - súkromné, prístupné cez metódy
    protected int riadok;           // Pozícia dlazdice v riadku
    protected int stlpec;           // Pozícia dlazdice v stĺpci
    protected StavDlazdice stav;    // Aktuálny stav dlazdice (enum)
    protected MinovePole minovepole; // Referencia na herné pole

    /**
     * Konštruktor pre vytvorenie novej dlazdice
     * @param riadok pozícia v riadku
     * @param stlpec pozícia v stĺpci
     * @param minovepole referencia na herné pole
     */
    public Dlazdica(int riadok, int stlpec, MinovePole minovepole) {
        this.riadok = riadok;
        this.stlpec = stlpec;
        this.stav = StavDlazdice.ZATVORENE;  // Začína v zatvorenom stave
        this.minovepole = minovepole;

        setupButton();
        setupEventHandlers();
    }

    /**
     * Nastavenie základného vzhľadu tlačidla
     */
    private void setupButton() {
        setPrefSize(30, 30);
        setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        setText("");
    }

    /**
     * Nastavenie obsluhy udalostí myši
     * Ľavé tlačidlo - otvoriť dlaždicu
     * Pravé tlačidlo - označiť vlajočkou
     */
    private void setupEventHandlers() {
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                otvorit();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                oznacit();
            }
        });
    }

    /**
     * Abstraktná metóda pre vykreslenie dlazdice - polymorfizmus
     * Každý typ dlazdice implementuje vlastné vykreslenie
     */
    public abstract void vykresli();

    /**
     * Metóda pre otvorenie dlazdice
     * Spracováva logiku otvárania a kontrolu stavu hry
     */
    public void otvorit() {
        // Môžeme otvoriť len zatvorenú dlaždicu počas hrania
        if (stav == StavDlazdice.ZATVORENE && minovepole.getStav() == StavPola.HRANIE) {

            // Spracovanie prvého kliku - generovanie mín mimo bezpečnej zóny
            if (minovepole.isPrvyKlik()) {
                minovepole.spracujPrvyKlik(riadok, stlpec);
            }

            stav = StavDlazdice.OTVORENE;
            minovepole.pridajTah();  // Zvýšenie počítadla ťahov
            vykresli();              // Polymorfné volanie vykreslenia

            // Kontrola, či je to mína - polymorfizmus cez instanceof
            if (this instanceof Mina) {
                minovepole.setStav(StavPola.NEUSPECH);
                minovepole.odhalVsetkyMiny();
            } else {
                // Kontrola výhry
                minovepole.skontrolujVyhru();
                // Automatické odhalenie susedných dlaždíc ak je prázdna
                if (getPocetSusednychMin() == 0) {
                    minovepole.otvorSusedne(riadok, stlpec);
                }
            }
        }
    }

    /**
     * Metóda pre označenie/odznačenie dlazdice vlajočkou
     */
    public void oznacit() {
        if (minovepole.getStav() == StavPola.HRANIE) {
            if (stav == StavDlazdice.ZATVORENE) {
                // Označenie vlajočkou
                stav = StavDlazdice.OZNACENE;
                setText("🚩");
                setStyle("-fx-background-color: yellow; -fx-font-weight: bold;");
            } else if (stav == StavDlazdice.OZNACENE) {
                // Odznačenie vlajočky
                stav = StavDlazdice.ZATVORENE;
                setText("");
                setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
            }
        }
    }

    /**
     * Abstraktná metóda pre získanie počtu susedných mín
     * @return počet susedných mín
     */
    public abstract int getPocetSusednychMin();

    // Gettery a settery pre zapuzdrenie
    public StavDlazdice getStav() { return stav; }

    /**
     * Setter pre stav dlazdice s automatickým prekreslením
     * @param stav nový stav dlazdice
     */
    public void setStav(StavDlazdice stav) {
        this.stav = stav;
        vykresli();  // Polymorfné volanie
    }
}
