//package com.example.minesweeper;
//
//import javafx.scene.control.Button;
//
//public class Cell {
//    private boolean hasMine;
//    private boolean isRevealed;
//    private boolean isFlagged;
//    private int neighborMines;
//    private final int x;
//    private final int y;
//    private Button button; // UI representation
//
//    public Cell(int x, int y) {
//        this.x = x;
//        this.y = y;
//        this.hasMine = false;
//        this.isRevealed = false;
//        this.isFlagged = false;
//        this.neighborMines = 0;
//        this.button = new Button();
//        button.setMinSize(30, 30);
//        button.setMaxSize(30, 30);
//    }
//
//    // Getters and setters
//    public boolean hasMine() {
//        return hasMine;
//    }
//    public void setMine(boolean mine) {
//        this.hasMine = mine;
//    }
//    public boolean isRevealed() {
//        return isRevealed;
//    }
//    public boolean isFlagged() {
//        return isFlagged;
//    }
//    public void toggleFlag() {
//        this.isFlagged = !this.isFlagged;
//        updateButton();
//    }
//    public int getNeighborMines() {
//        return neighborMines;
//    }
//    public void setNeighborMines(int count) {
//        this.neighborMines = count;
//    }
//    public int getX() {
//        return x;
//    }
//    public int getY() {
//        return y;
//    }
//    public Button getButton() {
//        return button;
//    }
//
//    public void reveal() {
//        if (!isRevealed && !isFlagged) {
//            isRevealed = true;
//            updateButton();
//        }
//    }
//
//
//    public void updateButton() {
//        if (isRevealed) {
//            if (hasMine) {
//                button.setText("💣");
//                button.setStyle("-fx-background-color: red; -fx-font-weight: bold;");
//            } else if (neighborMines > 0) {
//                button.setText(String.valueOf(neighborMines));
//                // Set colors based on number
//                String[] colors = {
//                        "",
//                        "blue",
//                        "green",
//                        "red",
//                        "darkblue",
//                        "darkred",
//                        "cyan",
//                        "black",
//                        "gray"
//                };
//                button.setStyle("-fx-text-fill: " + colors[neighborMines] +
//                        "; -fx-background-color: lightgray; -fx-font-weight: bold;");
//            } else {
//                button.setText("");
//                button.setStyle("-fx-background-color: lightgray;");
//            }
//        } else if (isFlagged) {
//            button.setText("🚩");
//            button.setStyle("-fx-font-weight: bold;");
//        } else {
//            button.setText("");
//            button.setStyle("-fx-background-color: #c0c0c0; -fx-border-color: #808080; -fx-border-width: 1;");
//        }
//    }
//}