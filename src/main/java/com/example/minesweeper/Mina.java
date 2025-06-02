package com.example.minesweeper;

/**
 * Mine tile class - inherits from Dlazdica
 */
public class Mina extends Dlazdica {

    public Mina(int riadok, int stlpec, MinovePole minovepole) {
        super(riadok, stlpec, minovepole);
    }

    @Override
    public void vykresli() {
        switch (stav) {
            case ZATVORENE:
                setText("");
                setStyle("-fx-background-color: lightgray; -fx-font-weight: bold;");
                break;
            case OTVORENE:
                setText("💣");
                setStyle("-fx-background-color: red; -fx-font-weight: bold;");
                break;
            case OZNACENE:
                setText("🚩");
                setStyle("-fx-background-color: yellow; -fx-font-weight: bold;");
                break;
        }
    }

    @Override
    public int getPocetSusednychMin() {
        return -1; // Mine doesn't count adjacent mines
    }
}
