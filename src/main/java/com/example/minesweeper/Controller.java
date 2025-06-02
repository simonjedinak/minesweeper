package com.example.minesweeper;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller class to manage game state and UI updates
 */
public class Controller {
    private MinovePole minoveole;
    private VBox topPanel;
    private Stage stage;
    private Timer timer;
    private int secondsElapsed;
    private long startTime;
    private Label timerLabel;
    private Label movesLabel;
    private Label statusLabel;

    public Controller(MinovePole minoveole, VBox topPanel, Stage stage) {
        this.minoveole = minoveole;
        this.topPanel = topPanel;
        this.stage = stage;
        this.secondsElapsed = 0;

        // Find labels in the top panel
        findLabels();
    }

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

    public void startGame() {
        startTime = System.currentTimeMillis();
        startTimer();
        startGameLoop();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                Platform.runLater(() -> {
                    if (timerLabel != null) {
                        timerLabel.setText(String.format("Čas: %03d", Math.min(secondsElapsed, 999)));
                    }
                });
            }
        }, 1000, 1000);
    }

    private void startGameLoop() {
        Timer gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateUI();
                    if (minoveole.getStav() != StavPola.HRANIE) {
                        endGame();
                        gameTimer.cancel();
                    }
                });
            }
        }, 100, 100);
    }

    private void updateUI() {
        if (movesLabel != null) {
            movesLabel.setText("Ťahy: " + minoveole.getPocetTahov());
        }

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

    private void endGame() {
        if (timer != null) {
            timer.cancel();
        }

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;

        // Save to history
        GameHistory.getInstance().pridajHru(
                minoveole.getSirka(),
                minoveole.getVyska(),
                minoveole.getPocetMin(),
                minoveole.getPocetTahov(),
                minoveole.getStav(),
                duration
        );

        // Show end game dialog
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec hry");

            if (minoveole.getStav() == StavPola.VYHRANE) {
                alert.setHeaderText("🎉 Gratulujeme! Vyhrali ste! 🎉");
                alert.setContentText(String.format(
                        "Čas: %d sekúnd\nPočet ťahov: %d\nVeľkosť poľa: %dx%d\nPočet mín: %d",
                        duration, minoveole.getPocetTahov(),
                        minoveole.getSirka(), minoveole.getVyska(), minoveole.getPocetMin()));
            } else {
                alert.setHeaderText("💥 Bohužiaľ, prehrali ste. 💥");
                alert.setContentText(String.format(
                        "Čas: %d sekúnd\nPočet ťahov: %d\nSkúste to znovu!",
                        duration, minoveole.getPocetTahov()));
            }

            alert.showAndWait();
        });
    }
}