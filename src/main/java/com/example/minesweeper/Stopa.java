package com.example.minesweeper;

/**
 * Regular tile class (not a mine) - inherits from Dlazdica
 */
public class Stopa extends Dlazdica {
    private int pocetSusednychMin;

    public Stopa(int riadok, int stlpec, MinovePole minovepole) {
        super(riadok, stlpec, minovepole);
        this.pocetSusednychMin = 0;
    }

    @Override
    public void vykresli() {
        switch (stav) {
            case ZATVORENE:
                setText("");
                setStyle("-fx-background-color: lightgray; -fx-font-weight: bold;");
                break;
            case OTVORENE:
                if (pocetSusednychMin > 0) {
                    setText(String.valueOf(pocetSusednychMin));
                    setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-text-fill: " + getColorForNumber(pocetSusednychMin) + ";");
                } else {
                    setText("");
                    setStyle("-fx-background-color: white; -fx-font-weight: bold;");
                }
                break;
            case OZNACENE:
                setText("🚩");
                setStyle("-fx-background-color: yellow; -fx-font-weight: bold;");
                break;
        }
    }

    private String getColorForNumber(int number) {
        switch (number) {
            case 1: return "blue";
            case 2: return "green";
            case 3: return "red";
            case 4: return "purple";
            case 5: return "maroon";
            case 6: return "turquoise";
            case 7: return "black";
            case 8: return "gray";
            default: return "black";
        }
    }

    @Override
    public int getPocetSusednychMin() {
        return pocetSusednychMin;
    }

    public void setPocetSusednychMin(int pocet) {
        this.pocetSusednychMin = pocet;
    }
}