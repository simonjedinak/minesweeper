package com.example.minesweeper;

/**
 * Trieda pre dlazdicu s mínou - dedí od Dlazdica
 * Demonštruje dedičnosť a polymorfizmus
 */
public class Mina extends Dlazdica {

    /**
     * Konštruktor pre vytvorenie míny
     * @param riadok pozícia v riadku
     * @param stlpec pozícia v stĺpci
     * @param minovepole referencia na herné pole
     */
    public Mina(int riadok, int stlpec, MinovePole minovepole) {
        super(riadok, stlpec, minovepole);  // Volanie konštruktora rodiča
    }

    /**
     * Polymorfná implementácia vykreslenia míny
     * Prekrýva abstraktnú metódu z rodičovskej triedy
     */
    @Override
    public void vykresli() {
        switch (stav) {
            case ZATVORENE:
                setText("");
                setStyle("-fx-background-color: lightgray; -fx-font-weight: bold;");
                break;
            case OTVORENE:
                setText("💣");  // Emoji míny
                setStyle("-fx-background-color: red; -fx-font-weight: bold;");
                break;
            case OZNACENE:
                setText("🚩");  // Emoji vlajočky
                setStyle("-fx-background-color: yellow; -fx-font-weight: bold;");
                break;
        }
    }

    /**
     * Polymorfná implementácia - mína nemá susedné míny
     * @return -1 ako indikátor, že je to mína
     */
    @Override
    public int getPocetSusednychMin() {
        return -1; // Mína nepočíta susedné míny
    }
}
