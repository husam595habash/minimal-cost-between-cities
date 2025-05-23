package com.example.algo1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DPTablePage {
    private final BorderPane root;
    private final GridPane tableGrid;
    private final Button backButton;
    private final VBox centerBox;
    private final Label title;
    private final VBox tableContainer;
    private String[] cities;
    private int[][] dp;

    public DPTablePage(String[] cities, int[][] dp) {
        this.cities = cities;
        this.dp = dp;
        root = new BorderPane();

        // Background image
        Image backgroundImage = new Image(
                getClass().getResource("/com/example/algo1/bg.png").toExternalForm()
        );
        BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
        );
        root.setBackground(new Background(bgImage));

        // Title
        title = new Label("Dynamic Programming Table");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);

        // Table setup
        tableGrid = new GridPane();
        tableGrid.setHgap(10);
        tableGrid.setVgap(10);

        setTable(); // Fill table with data

        // VBox for table
        tableContainer = new VBox(tableGrid);
        tableContainer.setAlignment(Pos.CENTER);

        // CenterBox
        centerBox = new VBox(20, title, tableContainer);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(50));
        centerBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35); -fx-background-radius: 25;");
        centerBox.setMaxWidth(800);
        centerBox.setMaxHeight(900);

        root.setCenter(centerBox);

        // Back Button
        backButton = new Button("Back");
        backButton.getStyleClass().add("bottom-button");

        HBox rightBox = new HBox(backButton);
        rightBox.setAlignment(Pos.CENTER_LEFT);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(rightBox);
        bottomPane.setPadding(new Insets(20, 30, 20, 30));

        root.setBottom(bottomPane);
    }



    private Label createCell(String text, boolean isHeader) {
        Label label = new Label(text);
        label.getStyleClass().add("dp-cell");
        if (isHeader) {
            label.getStyleClass().add("dp-header-cell");
        }
        return label;
    }

    public void setTable() {
        tableGrid.getChildren().clear();

        // Headers
        for (int col = 0; col < cities.length; col++) {
            tableGrid.add(createCell(cities[col], true), col + 1, 0);
        }

        for (int row = 0; row < cities.length; row++) {
            tableGrid.add(createCell(cities[row], true), 0, row + 1);

            for (int col = 0; col < cities.length; col++) {
                String value ;
                if (dp[row][col] == Integer.MAX_VALUE || row == col)
                    value = "";
                else
                    value = String.valueOf(dp[row][col]);
                tableGrid.add(createCell(value, false), col + 1, row + 1);
            }
        }
    }

    public BorderPane getView() {
        return root;
    }

    public Button getBackButton() {
        return backButton;
    }
}
