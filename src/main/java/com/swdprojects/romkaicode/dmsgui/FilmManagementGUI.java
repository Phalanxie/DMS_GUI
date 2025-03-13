package com.swdprojects.romkaicode.dmsgui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/*
    Roberto Ruiz,CEN3024C, 3/12/2025
    Software Development I
    FilmManagementGUI: this class controls the GUI functionality of the entire application functionality.
* */
public class FilmManagementGUI extends Application
{
    //TableView and column variables to display Film rows
    private TableView<Film> filmTable;
    private TableColumn<Film, Integer> idColumn;
    private TableColumn<Film, String> nameColumn;
    private TableColumn<Film, String> descColumn;
    private TableColumn<Film, Float> priceColumn;
    private TableColumn<Film, String> releaseColumn;
    private TableColumn<Film, Integer> runtimeColumn;

    private FilmController filmController = new FilmController();
    private Label runtimeLabel;


    /*
     * start: JavaFX start function, shows a main menu to the user via the JavaFX GUI
     * args: Stage object
     * return: none
     * */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Film Management System");

        //setup table for TableView
        initTable();

        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(15));
        rootPane.setStyle("-fx-background-color: #F4F4F4");

        rootPane.setCenter(filmTable);

        //button setup and styling for each, including padding, color, etc.
        Button addButton = createStyledButton("Add Film");
        Button deleteButton = createStyledButton("Delete Film");
        deleteButton.setStyle("-fx-background-color: #FF4444; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        Button updateButton = createStyledButton("Update Film");
        updateButton.setStyle("-fx-background-color: #F2C57C; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        Button runtimeButton = createStyledButton("Show Total Runtime");
        runtimeButton.setStyle("-fx-background-color: #5E5E5E; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        Button addFromFileButton = createStyledButton("Add Films from File");
        Button showFilmsButton = createStyledButton("Show All Films");
        showFilmsButton.setStyle("-fx-background-color: #7FB685; -fx-text-fill: white;" +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px;" +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        runtimeLabel = new Label("Total Runtime: 0 minutes");
        runtimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        runtimeLabel.setTextFill(Color.DARKBLUE);

        //setup button actions for launching windows/functionality
        addButton.setOnAction(e -> openAddFilmWindow());
        deleteButton.setOnAction(e -> openDeleteFilmWindow());
        updateButton.setOnAction(e -> openUpdateFilmWindow());
        runtimeButton.setOnAction(e -> updateRuntimeLabel());
        addFromFileButton.setOnAction(e -> loadFilmsFromFile(primaryStage));
        showFilmsButton.setOnAction(e -> updateFilmTable());

        //horizontal button layout
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(addButton, updateButton, deleteButton, showFilmsButton, addFromFileButton, runtimeButton);
        rootPane.setTop(buttonLayout);

        //lower right anchor for "Total Runtime" text label
        StackPane bottomRightPane = new StackPane();
        bottomRightPane.setPadding(new Insets(10));
        bottomRightPane.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightPane.getChildren().add(runtimeLabel);
        rootPane.setBottom(bottomRightPane);

        //display main window
        Scene scene = new Scene(rootPane, 1000, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     * initTable: using TableView and TableColumn vars, populate TableView to display all Film objects data
     * args: none
     * return: none
     * */
    private void initTable()
    {
        filmTable = new TableView<>();

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        releaseColumn = new TableColumn<>("Release Date");
        releaseColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));

        runtimeColumn = new TableColumn<>("Runtime");
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));

        filmTable.getColumns().addAll(idColumn,nameColumn,descColumn,priceColumn,releaseColumn,runtimeColumn);
    }

    /*
     * updateFilmTable: updates the Film List for the TableView to be shown when 'Show All Films' is clicked
     * args: none
     * return: none
     * */
    private void updateFilmTable()
    {
        //clear the table in the main window
        filmTable.getItems().clear();
        //collect all films from List, then add them to the table for 'Show All Films' functionality
        List<Film> films = filmController.getAllFilms();
        System.out.println("films: " + films);
        filmTable.getItems().addAll(films);

    }

    /*
     * openAddFilmWindow:  launches the 'Add Films' window for the user to add a film object manually
     * args: none
     * return: none
     * */
    private void openAddFilmWindow() {
        Stage stage = new Stage();
        stage.setTitle("Add Film");

        //vertical layout for buttons
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField descField = new TextField();
        TextField priceField = new TextField();
        TextField releaseField = new TextField();
        TextField runtimeField = new TextField();
        Button addButton = createStyledButton("Add");

        //add labels for input
        layout.getChildren().addAll(new Label("ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Description:"), descField,
                new Label("Price:"), priceField,
                new Label("Release Date:"), releaseField,
                new Label("Runtime:"), runtimeField, addButton);

        /*when button is clicked, pass information to variables to be added to FilmController's addFilmManually
        and close the window
        */
        addButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String desc = descField.getText();
            float price = Float.parseFloat(priceField.getText());
            int release = Integer.parseInt(releaseField.getText());
            int runtime = Integer.parseInt(runtimeField.getText());
            filmController.addFilmManually(id, name, desc, price, release, runtime);
            stage.close();
        });

        stage.setScene(new Scene(layout, 300, 450));
        stage.show();
    }


    /*
     * loadFilmsFromFile: launches the 'Add Films from File' window for the user to add films from a text file
     * args: Stage
     * return: none
     * */
    private void loadFilmsFromFile(Stage primaryStage)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Film File");

        //Set the initial directory to User's home dir
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        //Add file filters for text file format
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        //If a file is selected, load the films
        if (selectedFile != null) {
            String filename = selectedFile.getAbsolutePath();
            boolean success = filmController.loadFilmsFromFile(filename);
            if (success) {
                System.out.println("Films loaded successfully from file: " + filename);
                updateFilmTable(); // Refresh the TableView to show the newly loaded films
            } else {
                System.out.println("Failed to load films from file: " + filename);
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    /*
     * openDeleteFilmWindow: launches the 'Delete Film' window for the user to delete a film object
     * args: none
     * return: none
     * */
    private void openDeleteFilmWindow() {
        Stage stage = new Stage();
        stage.setTitle("Delete Film");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        TextField idField = new TextField();
        Button deleteButton = createStyledButton("Delete");

        layout.getChildren().addAll(new Label("Enter Film ID:"), idField, deleteButton);

        //onclick button action passes the film ID to FilmController's 'removeFilm' method
        deleteButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            filmController.removeFilm(id);
            stage.close();
        });

        stage.setScene(new Scene(layout, 250, 150));
        stage.show();
    }

    /*
     * openUpdateFilmWindow: launches the 'Update Film' window for the user to update a film object
     * args: none
     * return: none
     * */
    private void openUpdateFilmWindow()
    {
        //make the window stage, layoutbox, and alignment form for fields
        Stage stage = new Stage();
        stage.setTitle("Update Film");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField descField = new TextField();
        TextField priceField = new TextField();
        TextField releaseField = new TextField();
        TextField runtimeField = new TextField();
        Button updateButton = createStyledButton("Update");

        //add labels to textfields
        layout.getChildren().addAll(new Label("ID:"), idField,
                new Label("New Name:"), nameField,
                new Label("New Description:"), descField,
                new Label("New Price:"), priceField,
                new Label("New Release Date:"), releaseField,
                new Label("New Runtime:"), runtimeField, updateButton);

        //when button is clicked, check input for empty values or passing input to vars
        updateButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText().isEmpty() ? null : nameField.getText();
            String desc = descField.getText().isEmpty() ? null : descField.getText();
            Float price = priceField.getText().isEmpty() ? null : Float.parseFloat(priceField.getText());
            Integer release = releaseField.getText().isEmpty() ? null : Integer.parseInt(releaseField.getText());
            Integer runtime = runtimeField.getText().isEmpty() ? null : Integer.parseInt(runtimeField.getText());

            //run through filmController validation
            filmController.updateFilm(id, name, desc, price, release, runtime);
            stage.close();
        });

        stage.setScene(new Scene(layout, 300, 450));
        stage.show();
    }

    /*
     * updateRuntimeLabel: updates the total runtime of all movies in the program, displayed in a Text label
     * args: none
     * return: none
     * */
    private void updateRuntimeLabel() {
        runtimeLabel.setText("Total Runtime: " + filmController.getAllFilmRuntime() + " minutes");
    }

    /*
     * createStyledButton: makes and sets the style of button with light blue color, default font size, padding, and other options; for configuration purposes
     * args: String input for button title
     * return: String
     * */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-pref-width: 150px; -fx-padding: 10px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        return button;
    }

    /*
     * main: application launcher from Application class for JavaFX gui
     * args: none
     * return: none
     * */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}