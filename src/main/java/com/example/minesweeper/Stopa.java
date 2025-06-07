package com.example.minesweeper;

/**
 * Trieda pre obyčajnú dlazdicu (nie mína) - dedí od Dlazdica
 * Demonštruje dedičnosť a polymorfizmus
 */
public class Stopa extends Dlazdica {
    private int pocetSusednychMin;  // Zapuzdrený atribút

    /**
     * Konštruktor pre vytvorenie obyčajnej dlazdice
     * @param riadok pozícia v riadku
     * @param stlpec pozícia v stĺpci
     * @param minovepole referencia na herné pole
     */
    public Stopa(int riadok, int stlpec, MinovePole minovepole) {
        super(riadok, stlpec, minovepole);  // Volanie konštruktora rodiča
        this.pocetSusednychMin = 0;
    }

    /**
     * Polymorfná implementácia vykreslenia obyčajnej dlazdice
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
                if (pocetSusednychMin > 0) {
                    setText(String.valueOf(pocetSusednychMin));
                    setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-text-fill: " +
                            getColorForNumber(pocetSusednychMin) + ";");
                } else {
                    setText("");  // Prázdna dlazdica
                    setStyle("-fx-background-color: white; -fx-font-weight: bold;");
                }
                break;
            case OZNACENE:
                setText("🚩");
                setStyle("-fx-background-color: yellow; -fx-font-weight: bold;");
                break;
        }
    }

    /**
     * Pomocná metóda pre určenie farby čísla podľa počtu susedných mín
     * @param number počet susedných mín
     * @return farba ako string
     */
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

    /**
     * Polymorfná implementácia - vracia počet susedných mín
     * @return počet susedných mín
     */
    @Override
    public int getPocetSusednychMin() {
        return pocetSusednychMin;
    }

    /**
     * Setter pre počet susedných mín - zapuzdrenie
     * @param pocet nový počet susedných mín
     */
    public void setPocetSusednychMin(int pocet) {
        this.pocetSusednychMin = pocet;
    }
}
