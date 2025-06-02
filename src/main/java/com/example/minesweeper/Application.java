package com.example.minesweeper;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Míny - Minesweeper");

        // Create main menu scene
        Scene mainScene = createMainMenuScene(primaryStage);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Scene createMainMenuScene(Stage stage) {
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label titleLabel = new Label("MÍNY");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Subtitle
        Label subtitleLabel = new Label("Vyberte obtiažnosť:");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        // Difficulty buttons
        Button beginnerBtn = new Button("Začiatočník (9x9, 10 mín)");
        Button intermediateBtn = new Button("Pokročilý (16x16, 40 mín)");
        Button expertBtn = new Button("Expert (30x16, 99 mín)");
        Button customBtn = new Button("Vlastné nastavenie");

        // Style buttons
        String buttonStyle = "-fx-font-size: 12px; -fx-pref-width: 200px; -fx-pref-height: 35px;";
        beginnerBtn.setStyle(buttonStyle + "-fx-background-color: #27ae60; -fx-text-fill: white;");
        intermediateBtn.setStyle(buttonStyle + "-fx-background-color: #f39c12; -fx-text-fill: white;");
        expertBtn.setStyle(buttonStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white;");
        customBtn.setStyle(buttonStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white;");

        // Custom settings (initially hidden)
        VBox customBox = createCustomSettingsBox(stage);
        customBox.setVisible(false);
        customBox.setManaged(false);

        // History and exit buttons
        Button historyBtn = new Button("História hier");
        Button exitBtn = new Button("Koniec");

        historyBtn.setStyle("-fx-font-size: 11px; -fx-pref-width: 120px;");
        exitBtn.setStyle("-fx-font-size: 11px; -fx-pref-width: 120px;");

        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(historyBtn, exitBtn);

        // Event handlers
        beginnerBtn.setOnAction(e -> startGame(stage, 9, 9, 10));
        intermediateBtn.setOnAction(e -> startGame(stage, 16, 16, 40));
        expertBtn.setOnAction(e -> startGame(stage, 30, 16, 99));

        customBtn.setOnAction(e -> {
            customBox.setVisible(!customBox.isVisible());
            customBox.setManaged(!customBox.isManaged());
        });

        historyBtn.setOnAction(e -> showHistory());
        exitBtn.setOnAction(e -> Platform.exit());

        mainLayout.getChildren().addAll(
                titleLabel, subtitleLabel, beginnerBtn, intermediateBtn,
                expertBtn, customBtn, customBox, bottomButtons
        );

        return new Scene(mainLayout, 350, 450);
    }

    private VBox createCustomSettingsBox(Stage stage) {
        VBox customBox = new VBox(10);
        customBox.setAlignment(Pos.CENTER);
        customBox.setPadding(new Insets(10));
        customBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-background-color: white;");

        Label customLabel = new Label("Vlastné nastavenie:");
        customLabel.setStyle("-fx-font-weight: bold;");

        // Input fields
        HBox widthBox = new HBox(5);
        widthBox.setAlignment(Pos.CENTER);
        Label widthLabel = new Label("Šírka:");
        TextField widthField = new TextField("10");
        widthField.setPrefWidth(60);
        widthBox.getChildren().addAll(widthLabel, widthField);

        HBox heightBox = new HBox(5);
        heightBox.setAlignment(Pos.CENTER);
        Label heightLabel = new Label("Výška:");
        TextField heightField = new TextField("10");
        heightField.setPrefWidth(60);
        heightBox.getChildren().addAll(heightLabel, heightField);

        HBox minesBox = new HBox(5);
        minesBox.setAlignment(Pos.CENTER);
        Label minesLabel = new Label("Míny:");
        TextField minesField = new TextField("15");
        minesField.setPrefWidth(60);
        minesBox.getChildren().addAll(minesLabel, minesField);

        Button startCustomBtn = new Button("Začať hru");
        startCustomBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        startCustomBtn.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int mines = Integer.parseInt(minesField.getText());

                if (width < 5 || width > 50 || height < 5 || height > 30 ||
                        mines < 1 || mines >= width * height) {
                    throw new NumberFormatException();
                }

                startGame(stage, width, height, mines);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText("Neplatné hodnoty");
                alert.setContentText("Prosím zadajte platné čísla:\n" +
                        "Šírka: 5-50\nVýška: 5-30\nMíny: 1 až (šírka×výška-1)");
                alert.showAndWait();
            }
        });

        customBox.getChildren().addAll(
                customLabel, widthBox, heightBox, minesBox, startCustomBtn
        );

        return customBox;
    }

    private void startGame(Stage stage, int width, int height, int mines) {
        Scene gameScene = createGameScene(stage, width, height, mines);
        stage.setScene(gameScene);

        // Adjust window size based on game field
        int windowWidth = Math.max(400, width * 30 + 100);
        int windowHeight = Math.max(300, height * 30 + 150);
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
    }

    private Scene createGameScene(Stage stage, int width, int height, int mines) {
        BorderPane gameLayout = new BorderPane();
        gameLayout.setStyle("-fx-background-color: #ecf0f1;");

        // Top panel with game info and controls
        VBox topPanel = createTopPanel(stage);
        gameLayout.setTop(topPanel);

        // Game field
        MinovePole minovepole = new MinovePole(width, height, mines);
        minovepole.setAlignment(Pos.CENTER);
        minovepole.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(minovepole);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #ecf0f1;");

        gameLayout.setCenter(scrollPane);

        // Start game timer and monitoring
        Controller gameController = new Controller(minovepole, topPanel, stage);
        gameController.startGame();

        return new Scene(gameLayout);
    }

    private VBox createTopPanel(Stage stage) {
        VBox topPanel = new VBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-background-color: #34495e;");

        // Title and controls row
        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setSpacing(20);

        Label titleLabel = new Label("MÍNY");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button newGameBtn = new Button("Nová hra");
        Button exitBtn = new Button("Koniec hry");

        newGameBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        exitBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        newGameBtn.setOnAction(e -> {
            Scene mainScene = createMainMenuScene(stage);
            stage.setScene(mainScene);
            stage.setWidth(350);
            stage.setHeight(450);
        });

        exitBtn.setOnAction(e -> Platform.exit());

        titleRow.getChildren().addAll(titleLabel, spacer, newGameBtn, exitBtn);

        // Game status row
        HBox statusRow = new HBox(30);
        statusRow.setAlignment(Pos.CENTER);

        Label timerLabel = new Label("Čas: 000");
        Label movesLabel = new Label("Ťahy: 0");
        Label statusLabel = new Label("Hrá sa...");

        String labelStyle = "-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;";
        timerLabel.setStyle(labelStyle);
        movesLabel.setStyle(labelStyle);
        statusLabel.setStyle(labelStyle);

        // Store references for GameController
        timerLabel.setId("timerLabel");
        movesLabel.setId("movesLabel");
        statusLabel.setId("statusLabel");

        statusRow.getChildren().addAll(timerLabel, movesLabel, statusLabel);

        topPanel.getChildren().addAll(titleRow, statusRow);

        return topPanel;
    }

    private void showHistory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("História hier");
        alert.setHeaderText("Posledných 10 hier:");

        StringBuilder content = new StringBuilder();
        var historia = GameHistory.getInstance().getHistoria();

        if (historia.isEmpty()) {
            content.append("Žiadne hry v histórii.");
        } else {
            int start = Math.max(0, historia.size() - 10);
            for (int i = start; i < historia.size(); i++) {
                content.append(historia.get(i).toString()).append("\n");
            }
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}