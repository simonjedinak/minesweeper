package com.example.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;

/**
 * Abstract base class for all tiles in the minesweeper game
 * Demonstrates inheritance and polymorphism
 */
public abstract class Dlazdica extends Button {
    protected int riadok;
    protected int stlpec;
    protected StavDlazdice stav;
    protected MinovePole minovepole;

    public Dlazdica(int riadok, int stlpec, MinovePole minovepole) {
        this.riadok = riadok;
        this.stlpec = stlpec;
        this.stav = StavDlazdice.ZATVORENE;
        this.minovepole = minovepole;

        setupButton();
        setupEventHandlers();
    }

    private void setupButton() {
        setPrefSize(30, 30);
        setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        setText("");
    }

    private void setupEventHandlers() {
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                otvorit();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                oznacit();
            }
        });
    }

    // Abstract method - polymorphism
    public abstract void vykresli();

    public void otvorit() {
        if (stav == StavDlazdice.ZATVORENE && minovepole.getStav() == StavPola.HRANIE) {

            // Spracovanie prvého kliku
            if (minovepole.isPrvyKlik()) {
                minovepole.spracujPrvyKlik(riadok, stlpec);
            }

            stav = StavDlazdice.OTVORENE;
            minovepole.pridajTah();
            vykresli();

            // Check if this is a mine
            if (this instanceof Mina) {
                minovepole.setStav(StavPola.NEUSPECH);
                minovepole.odhalVsetkyMiny();
            } else {
                // Check for win condition
                minovepole.skontrolujVyhru();
                // Auto-reveal adjacent tiles if this is an empty tile
                if (getPocetSusednychMin() == 0) {
                    minovepole.otvorSusedne(riadok, stlpec);
                }
            }
        }
    }


    public void oznacit() {
        if (minovepole.getStav() == StavPola.HRANIE) {
            if (stav == StavDlazdice.ZATVORENE) {
                stav = StavDlazdice.OZNACENE;
                setText("🚩");
                setStyle("-fx-background-color: yellow; -fx-font-weight: bold;");
            } else if (stav == StavDlazdice.OZNACENE) {
                stav = StavDlazdice.ZATVORENE;
                setText("");
                setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
            }
        }
    }

    public abstract int getPocetSusednychMin();

    // Getters and setters (encapsulation)
    public StavDlazdice getStav() { return stav; }
    public void setStav(StavDlazdice stav) {
        this.stav = stav;
        vykresli();
    }
}