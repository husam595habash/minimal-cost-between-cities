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

public class PathPage {
    private final BorderPane root;

    private final Label optimalPathLabel;
    private final Label costLabel;

    private final Label altPath1Label;
    private final Label altCost1Label;

    private final Label altPath2Label;
    private final Label altCost2Label;

    private final Label altPath3Label;
    private final Label altCost3Label;

    private final VBox altBox1;
    private final VBox altBox2;
    private final VBox altBox3;

    private final Button viewTableButton;
    private final Button backButton;

    public PathPage() {
        root = new BorderPane();

        // 🖼️ Background image
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

        // 🌟 Title
        Label title = new Label("Trip Results");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(30, 0, 10, 0));

        // ✅ Optimal Path
        optimalPathLabel = createLabel("Optimal Path: ");
        costLabel = createLabel("Cost: ");

        // 🛣️ Alternative 1
        altPath1Label = createLabel("Alternative 1: ");
        altCost1Label = createLabel("Cost: ");
        altBox1 = createAltBox(altPath1Label, altCost1Label);

        // 🛣️ Alternative 2
        altPath2Label = createLabel("Alternative 2: ");
        altCost2Label = createLabel("Cost: ");
        altBox2 = createAltBox(altPath2Label, altCost2Label);

        // 🛣️ Alternative 3
        altPath3Label = createLabel("Alternative 3: ");
        altCost3Label = createLabel("Cost: ");
        altBox3 = createAltBox(altPath3Label, altCost3Label);

        VBox altPathsBox = new VBox(15, altBox1, altBox2, altBox3);
        altPathsBox.setPadding(new Insets(20, 40, 20, 40));
        altPathsBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35); -fx-background-radius: 20;");

        VBox centerBox = new VBox(20, title, optimalPathLabel, costLabel, altPathsBox);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(50));
        centerBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35); -fx-background-radius: 25;");
        centerBox.setMaxWidth(800);
        centerBox.setMaxHeight(900);

        BorderPane.setAlignment(centerBox, Pos.CENTER);
        root.setCenter(centerBox);

        // Bottom buttons
        viewTableButton = new Button("View Table");
        backButton = new Button("Back");

        styleButton(viewTableButton);
        styleButton(backButton);

        HBox leftBox = new HBox(backButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox rightBox = new HBox(viewTableButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(leftBox);
        bottomPane.setRight(rightBox);
        bottomPane.setPadding(new Insets(20, 30, 20, 30));

        root.setBottom(bottomPane);

    }

    private void styleButton(Button button) {
        button.getStyleClass().add("bottom-button");
    }



    private VBox createAltBox(Label pathLabel, Label costLabel) {
        VBox box = new VBox(5, pathLabel, costLabel);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10;");
        return box;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
        label.setTextFill(Color.WHITE);
        return label;
    }

    public BorderPane getView() {
        return root;
    }

    public void setOptimalPath(String path, int cost) {
        optimalPathLabel.setText("Optimal Path: " + path);
        costLabel.setText("Cost: " + cost);
    }

    public void setAlternativePaths(String[][] pathsCost) {

            altPath1Label.setText("Alternative 1: " + pathsCost[0][0]);
            altCost1Label.setText("Cost: " + pathsCost[0][1]);


            altPath2Label.setText("Alternative 2: " + pathsCost[1][0]);
            altCost2Label.setText("Cost: " + pathsCost[1][1]);


            altPath3Label.setText("Alternative 3: " + pathsCost[2][0]);
            altCost3Label.setText("Cost: " + pathsCost[2][1]);

    }


    // ✅ Button accessors
    public Button getViewTableButton() {
        return viewTableButton;
    }

    public Button getBackButton() {
        return backButton;
    }
}
