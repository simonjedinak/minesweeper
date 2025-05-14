//package com.example.minesweeper;
//
//import javafx.application.Platform;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.Label;
//import javafx.scene.layout.Pane;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class GameBoard {
//    private final Cell[][] cells;
//    private final int width;
//    private final int height;
//    private final int totalMines;
//    private int flagsPlaced;
//    private boolean firstClick;
//    private boolean gameOver;
//    private final Pane gamePane;
//    private final Label scoreLabel;
//    private final Label timerLabel;
//    private Timer timer;
//    private int secondsElapsed;
//
//    public GameBoard(Pane gamePane, Label scoreLabel, Label timerLabel, int width, int height, int mines) {
//        this.gamePane = gamePane;
//        this.scoreLabel = scoreLabel;
//        this.timerLabel = timerLabel;
//        this.width = width;
//        this.height = height;
//        this.totalMines = Math.min(mines, width * height - 9); // Ensure we don't have too many mines
//        this.cells = new Cell[width][height];
//        this.gameOver = false;
//        this.firstClick = true;
//        this.flagsPlaced = 0;
//        this.secondsElapsed = 0;
//
//        // Clear the game pane
//        gamePane.getChildren().clear();
//
//        // Calculate cell size to fit the game pane
//        double cellSize = Math.min(
//                (gamePane.getPrefWidth() - 2) / width,
//                (gamePane.getPrefHeight() - 2) / height
//        );
//
//        // Create cells
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                cells[x][y] = new Cell(x, y);
//                final int finalX = x;
//                final int finalY = y;
//
//                // Position the button in the pane
//                cells[x][y].getButton().setLayoutX(x * cellSize);
//                cells[x][y].getButton().setLayoutY(y * cellSize);
//                cells[x][y].getButton().setPrefSize(cellSize, cellSize);
//
//                // Add event handlers
//                cells[x][y].getButton().setOnMouseClicked(event -> {
//                    if (gameOver) return;
//
//                    // Start timer on first click
//                    if (firstClick) {
//                        startTimer();
//                        firstClick = false;
//
//                        // Ensure first click is safe
//                        ensureSafeStart(finalX, finalY);
//                    }
//
//                    switch (event.getButton()) {
//                        case PRIMARY:
//                            if (!cells[finalX][finalY].isFlagged()) {
//                                handleLeftClick(finalX, finalY);
//                            }
//                            break;
//                        case SECONDARY:
//                            toggleFlag(finalX, finalY);
//                            break;
//                    }
//                });
//
//                // Add to game pane
//                gamePane.getChildren().add(cells[x][y].getButton());
//                cells[x][y].updateButton();
//            }
//        }
//
//        // Update score display
//        updateScoreDisplay();
//    }
//
//    private void ensureSafeStart(int clickX, int clickY) {
//        // Place mines after first click to ensure the first click is safe
//        Random random = new Random();
//        int minesPlaced = 0;
//
//        // Create a "safe zone" around the first click
//        boolean[][] safeZone = new boolean[width][height];
//        for (int i = Math.max(0, clickX-1); i <= Math.min(width-1, clickX+1); i++) {
//            for (int j = Math.max(0, clickY-1); j <= Math.min(height-1, clickY+1); j++) {
//                safeZone[i][j] = true;
//            }
//        }
//
//        while (minesPlaced < totalMines) {
//            int x = random.nextInt(width);
//            int y = random.nextInt(height);
//
//            if (!safeZone[x][y] && !cells[x][y].hasMine()) {
//                cells[x][y].setMine(true);
//                minesPlaced++;
//            }
//        }
//
//        // Now calculate neighbor mine counts
//        calculateNeighborMineCounts();
//    }
//
//    private void calculateNeighborMineCounts() {
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if (!cells[x][y].hasMine()) {
//                    int count = countNeighborMines(x, y);
//                    cells[x][y].setNeighborMines(count);
//                }
//            }
//        }
//    }
//
//    private int countNeighborMines(int x, int y) {
//        int count = 0;
//        for (int i = Math.max(0, x-1); i <= Math.min(width-1, x+1); i++) {
//            for (int j = Math.max(0, y-1); j <= Math.min(height-1, y+1); j++) {
//                if (i != x || j != y) { // Don't count the cell itself
//                    if (cells[i][j].hasMine()) {
//                        count++;
//                    }
//                }
//            }
//        }
//        return count;
//    }
//
//    private void toggleFlag(int x, int y) {
//        if (!cells[x][y].isRevealed()) {
//            if (cells[x][y].isFlagged()) {
//                cells[x][y].setFlagged(false);
//                flagsPlaced--;
//            } else {
//                cells[x][y].setFlagged(true);
//                flagsPlaced++;
//            }
//            cells[x][y].updateButton();
//            updateScoreDisplay();
//        }
//    }
//
//    private void updateScoreDisplay() {
//        int remainingMines = totalMines - flagsPlaced;
//        scoreLabel.setText(String.format("%03d", remainingMines));
//    }
//
//    private void startTimer() {
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                secondsElapsed++;
//                Platform.runLater(() -> {
//                    timerLabel.setText(String.format("%03d", Math.min(secondsElapsed, 999)));
//                });
//            }
//        }, 1000, 1000);
//    }
//
//    public void handleLeftClick(int x, int y) {
//        if (cells[x][y].isRevealed() || cells[x][y].isFlagged()) {
//            return;
//        }
//
//        cells[x][y].setRevealed(true);
//        cells[x][y].updateButton();
//
//        if (cells[x][y].hasMine()) {
//            gameOver = true;
//            stopTimer();
//            revealAllMines();
//            showGameOverDialog(false);
//            return;
//        }
//
//        // If clicked on an empty cell, recursively reveal neighbors
//        if (cells[x][y].getNeighborMines() == 0) {
//            revealEmptyCells(x, y);
//        }
//
//        checkForWin();
//    }
//
//    private void revealEmptyCells(int x, int y) {
//        for (int i = Math.max(0, x-1); i <= Math.min(width-1, x+1); i++) {
//            for (int j = Math.max(0, y-1); j <= Math.min(height-1, y+1); j++) {
//                if (!cells[i][j].isRevealed() && !cells[i][j].hasMine() && !cells[i][j].isFlagged()) {
//                    cells[i][j].setRevealed(true);
//                    cells[i][j].updateButton();
//
//                    if (cells[i][j].getNeighborMines() == 0) {
//                        revealEmptyCells(i, j);
//                    }
//                }
//            }
//        }
//    }
//
//    private void revealAllMines() {
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if (cells[x][y].hasMine()) {
//                    cells[x][y].setRevealed(true);
//                    cells[x][y].updateButton();
//                }
//            }
//        }
//    }
//
//    private void checkForWin() {
//        int unrevealed = 0;
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if (!cells[x][y].isRevealed() && !cells[x][y].hasMine()) {
//                    unrevealed++;
//                }
//            }
//        }
//
//        if (unrevealed == 0) {
//            gameOver = true;
//            stopTimer();
//            // Flag all mines
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    if (cells[x][y].hasMine() && !cells[x][y].isFlagged()) {
//                        cells[x][y].setFlagged(true);
//                        cells[x][y].updateButton();
//                    }
//                }
//            }
//            showGameOverDialog(true);
//        }
//    }
//
//    private void stopTimer() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    private void showGameOverDialog(boolean won) {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Game Over");
//            if (won) {
//                alert.setHeaderText("You Won!");
//                alert.setContentText("Congratulations! You found all mines in " + secondsElapsed + " seconds!");
//            } else {
//                alert.setHeaderText("You Lost!");
//                alert.setContentText("Better luck next time!");
//            }
//            alert.showAndWait();
//        });
//    }
//
//    public void restart() {
//        stopTimer();
//        // Reinitialize with same parameters
//        new GameBoard(gamePane, scoreLabel, timerLabel, width, height, totalMines);
//    }
//}
