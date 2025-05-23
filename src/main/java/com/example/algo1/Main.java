package com.example.algo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends Application {
    private Scene homeScene;
    private File selectedFile;
    private BorderPane root = new BorderPane();
    private static String[] cities;
    private static String start;
    private static String end;
    private static int[][] dp;
    private static City[] paths;
    private int numberOfCities;
    private String optimalPath;
    private PathPage pathPage = new PathPage();
    private DPTablePage dpTablePage;
    private boolean readingSuccess ;
    private int MAX_PATHS;
    private  MinHeap allPathsWithCost;


    private void collectAllPaths(String currentCity, String pathSoFar, int costSoFar) {
        if (currentCity.equalsIgnoreCase(end)) {
            allPathsWithCost.insert(new CostEntry(pathSoFar, costSoFar));
            return;
        }

        for (City city : paths) {
            if (city.getCity().equalsIgnoreCase(currentCity)) {
                for (int i = 0; i < city.getNumberOfaccessCities(); i++) {
                    String nextCity = city.getAccessCity(i);
                    int moveCost = city.getPetrolCosts()[i] + city.getHotelCosts()[i];
                    collectAllPaths(nextCity, pathSoFar + " --> " + nextCity, costSoFar + moveCost);
                }
                break;
            }
        }
    }



    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        Label titleLabel = new Label("\uD83D\uDCC1 Load City Data File");
        titleLabel.getStyleClass().add("title-label");

        TextField filePathField = new TextField();
        filePathField.setPromptText("No file selected");
        filePathField.setEditable(false);
        filePathField.getStyleClass().add("text-field");

        Button browseButton = new Button("Browse");
        browseButton.getStyleClass().add("action-button");
        browseButton.setOnAction(e -> {
            readingSuccess = true;
            File file = showFileChooser(primaryStage);
            if (file != null) {
                selectedFile = file;
                filePathField.setText(file.getAbsolutePath());

            } else {
                showAlert("No File Selected", "Please select a valid file.", Alert.AlertType.WARNING);
            }
        });

        Scene resultScene = new Scene(pathPage.getView(), 1280, 720);
        resultScene.getStylesheets().add(getClass().getResource("/com/example/algo1/style.css").toExternalForm());

        Button startTripButton = new Button("Start Trip");
        startTripButton.getStyleClass().add("action-button");
        startTripButton.setPrefWidth(120);
        startTripButton.setOnAction(e -> {
            readData();
            if (readingSuccess) {
                MAX_PATHS = calculateNumberOfPaths();
                allPathsWithCost = new MinHeap(MAX_PATHS);
                primaryStage.setScene(resultScene);
                primaryStage.setFullScreen(true);

                dpTablePage = new DPTablePage(cities , dp);

                initializeDPTablePage(primaryStage);

                collectAllPaths(start, start, 0);
                // Set Optimal
                CostEntry best = allPathsWithCost.extractMin();
                optimalPath = best.getPath();
                pathPage.setOptimalPath(best.getPath(), best.getCost());

                // Prepare Alternatives
                String[][] alternatives = new String[3][2];
                int altCount = 0;

                while (altCount < 3 && !allPathsWithCost.isEmpty()) {
                    CostEntry next = allPathsWithCost.extractMin();
                    if (!next.getPath().equalsIgnoreCase(optimalPath)) {
                        alternatives[altCount][0] = next.getPath();
                        alternatives[altCount][1] = String.valueOf(next.getCost());
                        altCount++;
                    }
                }

                // Fill empty slots if fewer than 3 alternatives
                while (altCount < 3) {
                    alternatives[altCount][0] = "No valid path";
                    alternatives[altCount][1] = "-";
                    altCount++;
                }

                pathPage.setAlternativePaths(alternatives);
            }
        });



        HBox fileInputBox = new HBox(10, browseButton, filePathField);
        fileInputBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30, titleLabel, fileInputBox, startTripButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));

        root.setCenter(layout);

        homeScene = new Scene(root, 600, 350);
        homeScene.getStylesheets().add(getClass().getResource("/com/example/algo1/style.css").toExternalForm());

        primaryStage.setScene(homeScene);
        primaryStage.setTitle("City Trip Planner");
        primaryStage.show();

        pathPage.getBackButton().setOnAction(ev -> {
            primaryStage.setScene(homeScene);
            primaryStage.setFullScreen(false);
        });

    }

    private void initializeDPTablePage(Stage primaryStage) {
        dpTablePage = new DPTablePage(cities, dp);

        Scene dpScene = new Scene(dpTablePage.getView(), 1280, 720);
        dpScene.getStylesheets().add(getClass().getResource("/com/example/algo1/style.css").toExternalForm());

        pathPage.getViewTableButton().setOnAction(ev -> {
            primaryStage.setScene(dpScene);
            primaryStage.setFullScreen(true);
        });

        dpTablePage.getBackButton().setOnAction(ev -> {
            primaryStage.setScene(pathPage.getView().getScene());
            primaryStage.setFullScreen(true);
        });
    }


    private void readData(){
        try {
            Scanner sc = new Scanner(selectedFile);

            // Read total number of cities (first line in file)
            numberOfCities = Integer.parseInt(sc.nextLine().trim());

            // Initialize arrays: city names and paths
            cities = new String[numberOfCities];
            paths = new City[numberOfCities - 1];

            // Read start and end city names (second line)
            String[] startEnd = sc.nextLine().split(",");
            start = startEnd[0].trim();
            end = startEnd[1].trim();

            // Variables to manage city reading and stage assignment
            int cityIndex = 0; // Index for storing in arrays
            int stage = 0; // Current stage
            int k = 0;
            int currentCitiesReadInStage = 0; // Count of how many cities read in this stage

            while (sc.hasNext()) {
                String[] cityInfo = sc.nextLine().split(", ");

                // Determine number of accessible cities for this city
                int accessCitiesNo = cityInfo.length - 1;
                String cityName = cityInfo[0];
                cities[cityIndex] = cityName;

                // Prepare arrays for the destination cities and their costs
                String[] accessCities = new String[accessCitiesNo];
                int[] petrolCosts = new int[accessCitiesNo];
                int[] hotelCosts = new int[accessCitiesNo];

                // Extract destination city names and costs
                for (int i = 1; i < cityInfo.length; i++) {
                    String[] info = cityInfo[i].replace("[", "").replace("]", "").trim().split(",");
                    accessCities[i - 1] = info[0].trim();
                    petrolCosts[i - 1] = Integer.parseInt(info[1].trim());
                    hotelCosts[i - 1] = Integer.parseInt(info[2].trim());
                }
                City newCity = new City(cityName, accessCitiesNo, accessCities, hotelCosts, petrolCosts , stage);
                paths[cityIndex] = newCity;
                cityIndex++;

                if (currentCitiesReadInStage == k){
                    ++stage;
                    k = accessCitiesNo;
                    currentCitiesReadInStage=0;
                }

                currentCitiesReadInStage++;

            }

            // Set the final city in the city name array
            cities[cityIndex] = paths[cityIndex - 1].getAccessCity(0);

            // Validate that each connection is only to the next stage
            for (City fromCity : paths) {
                int fromStage = fromCity.getStage();

                for (int a = 0; a < fromCity.getNumberOfaccessCities(); a++) {
                    String toCity = fromCity.getAccessCity(a);
                    int toIndex = findCityIndex(toCity);

                    if (toIndex == -1 || toIndex >= paths.length) continue;

                    int toStage = paths[toIndex].getStage();

                    if (toStage != fromStage + 1) {
                        showAlert("Stage Violation", "Invalid stage transition.", Alert.AlertType.ERROR);
                        readingSuccess = false;
                        return;
                    }
                }
            }

            dp = new int[numberOfCities][numberOfCities];
            fillDBTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Error" , "Invalid file format" , Alert.AlertType.ERROR);
            readingSuccess = false;
        }

    }

    private void fillDBTable(){
        // initialize dp by filling it with maxInt;
        for (int i = 0; i < dp.length; i++)
            for (int j = 0; j < dp[i].length; j++)
                dp[i][j] = Integer.MAX_VALUE;


        // fill the dp table with values as read from the file
        for (int i = 0; i < paths.length; i++) {
            City fromCity = paths[i];

            for (int j = 0; j < fromCity.getNumberOfaccessCities(); j++) {
                String toCity = fromCity.getAccessCity(j);
                int toIndex = findCityIndex(toCity);

                if (toIndex == -1) continue;

                int totalCost = fromCity.getTotalCosts()[j];

                dp[i][toIndex] = totalCost;
            }
        }

        // fill the rest of dp table by calculating the minimum paths costs between cities
        for (int i = 0; i < dp.length; i++) { // source
            for (int j = 0; j < dp[i].length ; j++) { // destination
                for (int k = 0; k <= j ; k++) { // intermediate city
                    if (dp[i][k] == Integer.MAX_VALUE || dp[k][j] == Integer.MAX_VALUE) continue;

                    int newCost = dp[i][k] + dp[k][j];

                    if (newCost < dp[i][j]) {
                        dp[i][j] = newCost;
                    }
                }
            }
        }
    }

    // find the index of a given city in the cities array
    private int findCityIndex(String city) {
        for (int i = 0; i < cities.length; i++) {
            if (cities[i].equalsIgnoreCase(city))
                return i;
        }
        return -1;
    }


    private int calculateNumberOfPaths() {
        // First, find the maximum stage number
        int maxStage = 0;
        for (City city : paths) {
            if (city.getStage() > maxStage) {
                maxStage = city.getStage();
            }
        }

        // Count how many cities are in each stage
        int[] stageCounts = new int[maxStage + 1];
        for (City city : paths) {
            stageCounts[city.getStage()]++;
        }

        int totalPaths = 1;
        for (int count : stageCounts) {
            totalPaths *= count;
        }

        return totalPaths;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private File showFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.setInitialDirectory(new File("C:\\Users\\husam\\OneDrive\\Documents"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showOpenDialog(stage);
    }



}
