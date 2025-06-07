package com.example.minesweeper;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Hlavná trieda aplikácie - prezentačná vrstva (GUI)
 * Implementuje kompletné používateľské rozhranie bez použitia FXML
 * Spĺňa všetky požiadavky zadania na horný panel a hernú mapu
 * Zabezpečuje navigáciu medzi scénami a správu stavu aplikácie
 */
public class Application extends javafx.application.Application {

    /**
     * Hlavná metóda pre spustenie JavaFX aplikácie
     * Nastavuje primárne okno a zobrazuje hlavné menu
     * @param primaryStage hlavné okno aplikácie
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Míny - Minesweeper");  // Názov aplikácie podľa požiadaviek

        // Vytvorenie hlavnej menu scény
        Scene mainScene = createMainMenuScene(primaryStage);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Vytvorenie hlavného menu s výberom obtiažnosti
     * Poskytuje rôzne úrovne obtiažnosti a vlastné nastavenia
     * @param stage referencia na hlavné okno
     * @return scéna hlavného menu
     */
    private Scene createMainMenuScene(Stage stage) {
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Názov aplikácie - požiadavka A: "Názov aplikácie: Míny"
        Label titleLabel = new Label("MÍNY");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Podnázov pre výber obtiažnosti
        Label subtitleLabel = new Label("Vyberte obtiažnosť:");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        // Tlačidlá pre rôzne úrovne obtiažnosti
        Button beginnerBtn = new Button("Začiatočník (9x9, 10 mín)");
        Button intermediateBtn = new Button("Pokročilý (16x16, 40 mín)");
        Button expertBtn = new Button("Expert (30x16, 99 mín)");
        Button customBtn = new Button("Vlastné nastavenie");

        // Štýlovanie tlačidiel pre lepšiu vizuálnu identifikáciu
        String buttonStyle = "-fx-font-size: 12px; -fx-pref-width: 200px; -fx-pref-height: 35px;";
        beginnerBtn.setStyle(buttonStyle + "-fx-background-color: #27ae60; -fx-text-fill: white;");
        intermediateBtn.setStyle(buttonStyle + "-fx-background-color: #f39c12; -fx-text-fill: white;");
        expertBtn.setStyle(buttonStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white;");
        customBtn.setStyle(buttonStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white;");

        // Vlastné nastavenia - požiadavka A: "Vstupy pre počet riadkov, stĺpcov a mín"
        VBox customBox = createCustomSettingsBox(stage);
        customBox.setVisible(false);
        customBox.setManaged(false);

        // Tlačidlá pre históriu a ukončenie
        Button historyBtn = new Button("História hier");
        Button exitBtn = new Button("Koniec");

        historyBtn.setStyle("-fx-font-size: 11px; -fx-pref-width: 120px;");
        exitBtn.setStyle("-fx-font-size: 11px; -fx-pref-width: 120px;");

        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(historyBtn, exitBtn);

        // Obsluha udalostí tlačidiel - spustenie hry s rôznymi nastaveniami
        beginnerBtn.setOnAction(e -> startGame(stage, 9, 9, 10));
        intermediateBtn.setOnAction(e -> startGame(stage, 16, 16, 40));
        expertBtn.setOnAction(e -> startGame(stage, 30, 16, 99));

        // Zobrazenie/skrytie vlastných nastavení
        customBtn.setOnAction(e -> {
            customBox.setVisible(!customBox.isVisible());
            customBox.setManaged(!customBox.isManaged());
        });

        // Zobrazenie histórie hier - použitie dátovej vrstvy
        historyBtn.setOnAction(e -> showHistory());

        // Ukončenie aplikácie
        exitBtn.setOnAction(e -> Platform.exit());

        // Zostavenie layoutu hlavného menu
        mainLayout.getChildren().addAll(
                titleLabel, subtitleLabel, beginnerBtn, intermediateBtn,
                expertBtn, customBtn, customBox, bottomButtons
        );

        return new Scene(mainLayout, 350, 450);
    }

    /**
     * Vytvorenie boxu pre vlastné nastavenia hry
     * Spĺňa požiadavku A: "Vstupy pre počet riadkov, stĺpcov a mín"
     * @param stage referencia na hlavné okno
     * @return VBox s ovládacími prvkami pre vlastné nastavenia
     */
    private VBox createCustomSettingsBox(Stage stage) {
        VBox customBox = new VBox(10);
        customBox.setAlignment(Pos.CENTER);
        customBox.setPadding(new Insets(10));
        customBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-background-color: white;");

        Label customLabel = new Label("Vlastné nastavenie:");
        customLabel.setStyle("-fx-font-weight: bold;");

        // Vstupné pole pre šírku - požiadavka A: "Vstupy pre počet stĺpcov"
        HBox widthBox = new HBox(5);
        widthBox.setAlignment(Pos.CENTER);
        Label widthLabel = new Label("Šírka:");
        TextField widthField = new TextField("10");
        widthField.setPrefWidth(60);
        widthBox.getChildren().addAll(widthLabel, widthField);

        // Vstupné pole pre výšku - požiadavka A: "Vstupy pre počet riadkov"
        HBox heightBox = new HBox(5);
        heightBox.setAlignment(Pos.CENTER);
        Label heightLabel = new Label("Výška:");
        TextField heightField = new TextField("10");
        heightField.setPrefWidth(60);
        heightBox.getChildren().addAll(heightLabel, heightField);

        // Vstupné pole pre počet mín - požiadavka A: "Vstupy pre počet mín"
        HBox minesBox = new HBox(5);
        minesBox.setAlignment(Pos.CENTER);
        Label minesLabel = new Label("Míny:");
        TextField minesField = new TextField("15");
        minesField.setPrefWidth(60);
        minesBox.getChildren().addAll(minesLabel, minesField);

        // Tlačidlo pre spustenie hry s vlastnými nastaveniami
        Button startCustomBtn = new Button("Začať hru");
        startCustomBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        // Obsluha spustenia vlastnej hry s validáciou vstupov
        startCustomBtn.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int mines = Integer.parseInt(minesField.getText());

                // Validácia rozumných hodnôt pre hernú plochu
                if (width < 5 || width > 50 || height < 5 || height > 30 ||
                        mines < 1 || mines >= width * height) {
                    throw new NumberFormatException();
                }

                startGame(stage, width, height, mines);
            } catch (NumberFormatException ex) {
                // Zobrazenie chybového dialógu pri neplatných hodnotách
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText("Neplatné hodnoty");
                alert.setContentText("Prosím zadajte platné čísla:\n" +
                        "Šírka: 5-50\nVýška: 5-30\nMíny: 1 až (šírka×výška-1)");
                alert.showAndWait();
            }
        });

        // Zostavenie layoutu vlastných nastavení
        customBox.getChildren().addAll(
                customLabel, widthBox, heightBox, minesBox, startCustomBtn
        );

        return customBox;
    }

    /**
     * Spustenie novej hry s danými parametrami
     * Prechod z hlavného menu do hernej scény
     * @param stage hlavné okno
     * @param width šírka herného poľa
     * @param height výška herného poľa
     * @param mines počet mín
     */
    private void startGame(Stage stage, int width, int height, int mines) {
        Scene gameScene = createGameScene(stage, width, height, mines);
        stage.setScene(gameScene);

        // Prispôsobenie veľkosti okna podľa herného poľa
        int windowWidth = Math.max(400, width * 30 + 100);
        int windowHeight = Math.max(300, height * 30 + 150);
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
    }

    /**
     * Vytvorenie hernej scény s kompletným GUI
     * Spĺňa požiadavky A: Prezentačná vrstva
     * @param stage hlavné okno
     * @param width šírka herného poľa
     * @param height výška herného poľa
     * @param mines počet mín
     * @return herná scéna
     */
    private Scene createGameScene(Stage stage, int width, int height, int mines) {
        BorderPane gameLayout = new BorderPane();
        gameLayout.setStyle("-fx-background-color: #ecf0f1;");

        // Horný panel - požiadavka A: "Horný panel"
        VBox topPanel = createTopPanel(stage);
        gameLayout.setTop(topPanel);

        // Herné pole - požiadavka A: "Hracia mapa: Mriežka s políčkami (klikateľné)"
        MinovePole minovepole = new MinovePole(width, height, mines);
        minovepole.setAlignment(Pos.CENTER);
        minovepole.setPadding(new Insets(10));

        // ScrollPane pre veľké herné polia
        ScrollPane scrollPane = new ScrollPane(minovepole);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #ecf0f1;");

        gameLayout.setCenter(scrollPane);

        // Spustenie herného kontroléra - požiadavka B: "Hlavná herná slučka"
        Controller gameController = new Controller(minovepole, topPanel, stage);
        gameController.startGame();

        return new Scene(gameLayout);
    }

    /**
     * Vytvorenie horného panelu s ovládacími prvkami
     * Spĺňa požiadavku A: "Horný panel" so všetkými požadovanými komponentmi
     * @param stage hlavné okno
     * @return VBox s horným panelom
     */
    private VBox createTopPanel(Stage stage) {
        VBox topPanel = new VBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-background-color: #34495e;");

        // Riadok s názvom a ovládacími prvkami
        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setSpacing(20);

        // Názov aplikácie - požiadavka A: "Názov aplikácie: Míny"
        Label titleLabel = new Label("MÍNY");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Spacer pre rozloženie prvkov
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Tlačidlo "Koniec Hry" - požiadavka A: "Tlačidlo „Koniec Hry""
        Button exitButton = new Button("Ukončiť hru");
        exitButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        // Návrat do hlavného menu - požiadavka A: "Tlačidlo na reset hry"
        exitButton.setOnAction(e -> {
            Scene mainScene = createMainMenuScene(stage);
            stage.setScene(mainScene);
            stage.setWidth(350);
            stage.setHeight(450);
        });

        titleRow.getChildren().addAll(titleLabel, spacer, exitButton);

        // Riadok so stavom hry - požiadavka A: "Zobrazenie stavu hry"
        HBox statusRow = new HBox(30);
        statusRow.setAlignment(Pos.CENTER);

        // Zobrazenie času - časť stavu hry
        Label timerLabel = new Label("Čas: 000");

        // Zobrazenie počtu ťahov - požiadavka A: "počet ťahov"
        Label movesLabel = new Label("Ťahy: 0");

        // Zobrazenie stavu - požiadavka A: "Výhra / Prehra"
        Label statusLabel = new Label("Hrá sa...");

        // Štýlovanie labelov
        String labelStyle = "-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;";
        timerLabel.setStyle(labelStyle);
        movesLabel.setStyle(labelStyle);
        statusLabel.setStyle(labelStyle);

        // Nastavenie ID pre prístup z Controller triedy
        timerLabel.setId("timerLabel");
        movesLabel.setId("movesLabel");
        statusLabel.setId("statusLabel");

        statusRow.getChildren().addAll(timerLabel, movesLabel, statusLabel);

        // Zostavenie horného panelu
        topPanel.getChildren().addAll(titleRow, statusRow);

        return topPanel;
    }

    /**
     * Zobrazenie histórie hier
     * Spĺňa požiadavku C: Dátová vrstva - zobrazenie uložených hier
     * Používa kolekcie z GameHistory triedy
     */
    private void showHistory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("História hier");
        alert.setHeaderText("Posledných 10 hier:");

        StringBuilder content = new StringBuilder();
        // Použitie kolekcie z dátovej vrstvy - požiadavka C
        var historia = GameHistory.getInstance().getHistoria();

        if (historia.isEmpty()) {
            content.append("Žiadne hry v histórii.");
        } else {
            // Zobrazenie posledných 10 hier
            int start = Math.max(0, historia.size() - 10);
            for (int i = start; i < historia.size(); i++) {
                // Zobrazenie všetkých požadovaných údajov - požiadavka C
                content.append(historia.get(i).toString()).append("\n");
            }
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    /**
     * Hlavná metóda pre spustenie aplikácie
     * @param args argumenty príkazového riadku
     */
    public static void main(String[] args) {
        launch();
    }
}
