package com.example.minesweeper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    public VBox mainVBox;
    public VBox customVBox;

    public TextField input_y;
    public TextField input_x;
    public TextField input_mines;

    @FXML
    public javafx.scene.control.Label timerLabel;
    private int secondsElapsed;



    public void swtichToGameView(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/minesweeper/gameView.fxml"));
            Parent gameView = loader.load();

            // Get the controller for the game view
            Controller gameController = loader.getController();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene gameScene = new Scene(gameView);

            stage.setScene(gameScene);
            stage.show();

            gameController.startTimer();
        } catch (IOException e) {
            System.out.println("Error loading game view: " + e.getMessage());
        }
    }


    public void generateField(int x, int y, int mines) {
        System.out.println("Generating field with dimensions: " + x + "x" + y + " and " + mines + " mines.");
    }

    public void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                Platform.runLater(() -> {
                    timerLabel.setText(String.format("%03d", Math.min(secondsElapsed, 999)));
                });
            }
        }, 1000, 1000);
    }



    public void handleBeginner(ActionEvent actionEvent) {
        swtichToGameView(actionEvent);
        generateField(9, 9, 10);
    }
    public void handleIntermediate(ActionEvent actionEvent) {
        swtichToGameView(actionEvent);
        generateField(16, 16, 40);
    }
    public void handleExpert(ActionEvent actionEvent) {
        swtichToGameView(actionEvent);
        generateField(30, 16, 99);
    }
    public void handleCustom(ActionEvent actionEvent) {
        customVBox.setVisible(!customVBox.isVisible());
        customVBox.setManaged(customVBox.isVisible());
        mainVBox.setVisible(!mainVBox.isVisible());
        mainVBox.setManaged(mainVBox.isVisible());
    }




    public void handleCustomStart(ActionEvent actionEvent) {
        try {
            int x = Integer.parseInt(input_x.getText());
            int y = Integer.parseInt(input_y.getText());
            int mines = Integer.parseInt(input_mines.getText());
            if (x < 1 || y < 1 || mines < 1 || mines > x * y) {
                throw new NumberFormatException();
            }
            swtichToGameView(actionEvent);
            generateField(x, y, mines);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for width and height.");
            alert.showAndWait();
        }
    }
}