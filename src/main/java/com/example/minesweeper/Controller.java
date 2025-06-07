package com.example.minesweeper;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Kontrolér trieda pre riadenie stavu hry a aktualizáciu používateľského rozhrania
 * Implementuje hlavnú hernú slučku a spracováva udalosti
 * Spĺňa požiadavku na prezentačnú vrstvu a logickú vrstvu
 */
public class Controller {
    // Zapuzdrené atribúty
    private MinovePole minoveole;    // Referencia na herné pole
    private VBox topPanel;           // Horný panel s ovládacími prvkami
    private Stage stage;             // Hlavné okno aplikácie
    private Timer timer;             // Časovač pre meranie času hry
    private int secondsElapsed;      // Počet uplynulých sekúnd
    private long startTime;          // Čas začiatku hry
    private Label timerLabel;        // Label pre zobrazenie času
    private Label movesLabel;        // Label pre zobrazenie počtu ťahov
    private Label statusLabel;       // Label pre zobrazenie stavu hry

    /**
     * Konštruktor kontroléra
     * @param minoveole herné pole
     * @param topPanel horný panel s ovládacími prvkami
     * @param stage hlavné okno aplikácie
     */
    public Controller(MinovePole minoveole, VBox topPanel, Stage stage) {
        this.minoveole = minoveole;
        this.topPanel = topPanel;
        this.stage = stage;
        this.secondsElapsed = 0;

        // Nájdenie labelov v hornom paneli
        findLabels();
    }

    /**
     * Vyhľadanie a priradenie labelov z horného panela
     * Používa lookup pre nájdenie komponentov podľa ID
     */
    private void findLabels() {
        topPanel.lookupAll(".label").forEach(node -> {
            if (node instanceof Label) {
                Label label = (Label) node;
                String id = label.getId();
                if ("timerLabel".equals(id)) {
                    timerLabel = label;
                } else if ("movesLabel".equals(id)) {
                    movesLabel = label;
                } else if ("statusLabel".equals(id)) {
                    statusLabel = label;
                }
            }
        });
    }

    /**
     * Spustenie novej hry
     * Inicializuje časovač a hlavnú hernú slučku
     */
    public void startGame() {
        startTime = System.currentTimeMillis();
        startTimer();
        startGameLoop();
    }

    /**
     * Spustenie časovača pre meranie času hry
     * Aktualizuje časový label každú sekundu
     */
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                // Aktualizácia UI musí byť na JavaFX vlákne
                Platform.runLater(() -> {
                    if (timerLabel != null) {
                        // Obmedzenie na 999 sekúnd pre zobrazenie
                        timerLabel.setText(String.format("Čas: %03d", Math.min(secondsElapsed, 999)));
                    }
                });
            }
        }, 1000, 1000); // Spustenie po 1 sekunde, opakovanie každú sekundu
    }

    /**
     * Hlavná herná slučka - spĺňa požiadavku B. Logická vrstva
     * Pravidelne kontroluje stav hry a aktualizuje UI
     */
    private void startGameLoop() {
        Timer gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Všetky UI operácie musia byť na JavaFX vlákne
                Platform.runLater(() -> {
                    updateUI();
                    // Kontrola ukončenia hry
                    if (minoveole.getStav() != StavPola.HRANIE) {
                        endGame();
                        gameTimer.cancel(); // Zastavenie hernej slučky
                    }
                });
            }
        }, 100, 100); // Kontrola každých 100ms
    }

    /**
     * Aktualizácia používateľského rozhrania
     * Zobrazuje aktuálny počet ťahov a stav hry
     */
    private void updateUI() {
        // Aktualizácia počtu ťahov
        if (movesLabel != null) {
            movesLabel.setText("Ťahy: " + minoveole.getPocetTahov());
        }

        // Aktualizácia stavu hry s farebnými indikátormi
        if (statusLabel != null) {
            switch (minoveole.getStav()) {
                case HRANIE:
                    statusLabel.setText("Hrá sa...");
                    statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
                    break;
                case VYHRANE:
                    statusLabel.setText("VÝHRA!");
                    statusLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 12px; -fx-font-weight: bold;");
                    break;
                case NEUSPECH:
                    statusLabel.setText("PREHRA!");
                    statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px; -fx-font-weight: bold;");
                    break;
            }
        }
    }

    /**
     * Ukončenie hry a spracovanie výsledkov
     * Spĺňa požiadavku C. Dátová vrstva - ukladanie do histórie
     */
    private void endGame() {
        // Zastavenie časovača
        if (timer != null) {
            timer.cancel();
        }

        // Výpočet celkového trvania hry
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000; // Prevod na sekundy

        // Uloženie hry do histórie - použitie kolekcie (požiadavka C)
        GameHistory.getInstance().pridajHru(
                minoveole.getSirka(),
                minoveole.getVyska(),
                minoveole.getPocetMin(),
                minoveole.getPocetTahov(),
                minoveole.getStav(),
                duration
        );

        // Zobrazenie dialógu s výsledkom hry
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec hry");

            if (minoveole.getStav() == StavPola.VYHRANE) {
                // Víťazný dialóg
                alert.setHeaderText("🎉 Gratulujeme! Vyhrali ste! 🎉");
                alert.setContentText(String.format(
                        "Čas: %d sekúnd\nPočet ťahov: %d\nVeľkosť poľa: %dx%d\nPočet mín: %d",
                        duration, minoveole.getPocetTahov(),
                        minoveole.getSirka(), minoveole.getVyska(), minoveole.getPocetMin()));
            } else {
                // Prehratý dialóg
                alert.setHeaderText("💥 Bohužiaľ, prehrali ste. 💥");
                alert.setContentText(String.format(
                        "Čas: %d sekúnd\nPočet ťahov: %d\nSkúste to znovu!",
                        duration, minoveole.getPocetTahov()));
            }

            alert.showAndWait();
        });
    }
}
