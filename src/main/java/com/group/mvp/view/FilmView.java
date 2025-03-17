package com.group.mvp.view;

import com.group.mvp.model.Cast;
import com.group.mvp.model.Film;
import com.group.mvp.model.FilmImage;
import com.group.mvp.presenter.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.* ;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;

public class FilmView implements IFilmView {
    private FilmPresenter presenter;
    private TableView<Film> filmTableView;
    private VBox view;

    public FilmView() {
        filmTableView = new TableView<>();
        setupTable();

        view = new VBox(10, filmTableView);
        presenter = new FilmPresenter(this);
    }
    private void setupTable() {
        // Enable editing the table
        filmTableView.setEditable(true);

        // Sets delete and add entry event + filter options
        setContextMenuOptions();

        // Create table columns
        TableColumn<Film, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setEditable(false); // ID should not be editable

        TableColumn<Film, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        setEditable(titleColumn); // Set update entry event for each column

        TableColumn<Film, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        setEditableInteger(yearColumn);

        TableColumn<Film, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        setEditable(typeColumn);

        TableColumn<Film, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        setEditable(categoryColumn);

        TableColumn<Film, String> directorNameColumn = new TableColumn<>("Director");
        directorNameColumn.setCellValueFactory(new PropertyValueFactory<>("directorName"));

        TableColumn<Film, String> writerNameColumn = new TableColumn<>("Writer");
        writerNameColumn.setCellValueFactory(new PropertyValueFactory<>("writerName"));

        TableColumn<Film, String> producerNameColumn = new TableColumn<>("Producer");
        producerNameColumn.setCellValueFactory(new PropertyValueFactory<>("producerName"));

        TableColumn<Film, String> actorNamesColumn = new TableColumn<>("Actors");
        actorNamesColumn.setCellValueFactory(new PropertyValueFactory<>("actorNames"));

        // Add columns to table
        filmTableView.getColumns().addAll(idColumn, titleColumn, yearColumn, typeColumn, categoryColumn, directorNameColumn, writerNameColumn, producerNameColumn, actorNamesColumn);
    }



    // Sets context menu with different options
    private void setContextMenuOptions(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem addItem = new MenuItem("Add");
        MenuItem viewImagesItem = new MenuItem("View Images");
        MenuItem viewRolesItem = new MenuItem("View Roles");
        MenuItem exportCsvItem = new MenuItem("Export .csv");
        MenuItem exportDocxItem = new MenuItem("Export .docx");

        exportCsvItem.setOnAction(event -> presenter.exportToCsv(filmTableView));
        exportDocxItem.setOnAction(event -> presenter.exportToDocx(filmTableView));

        Menu filterMenu = new Menu("Filter By");

        Menu typeMenu = new Menu("type");
        MenuItem artisticTypeMenuItem = new MenuItem("artistic");
        MenuItem seriesTypeMenuItem = new MenuItem("series");
        artisticTypeMenuItem.setOnAction(event -> presenter.loadFilms("ARTISTIC"));
        seriesTypeMenuItem.setOnAction(event -> presenter.loadFilms("SERIES"));
        typeMenu.getItems().addAll(artisticTypeMenuItem, seriesTypeMenuItem);

        Menu categoryMenu = new Menu("category");
        List<String> categories = List.of("ACTION", "WESTERN", "HISTORICAL", "SCIFI", "HORROR", "DRAMA", "ADVENTURE", "ROMANCE", "COMEDY", "THRILLER");
        for (String category : categories) {
            MenuItem categoryMenuItem = new MenuItem(category.toLowerCase());
            categoryMenuItem.setOnAction(event -> presenter.loadFilmsByCategory(category));
            categoryMenu.getItems().add(categoryMenuItem);
        }

        MenuItem yearItem = new MenuItem("year");
        yearItem.setOnAction(event -> enterYearPopupWindow());
        MenuItem actorItem = new MenuItem("actor");
        actorItem.setOnAction(event -> enterActorPopupWindow());

        filterMenu.getItems().addAll(typeMenu, categoryMenu, yearItem, actorItem);

        // Set the action for the delete menu item
        deleteItem.setOnAction(event -> {
            Film selectedFilm = filmTableView.getSelectionModel().getSelectedItem();
            if (selectedFilm != null) {
                presenter.deleteFilm(selectedFilm); // Call the presenter to delete the Film
                filmTableView.getItems().remove(selectedFilm); // Remove from the table
            }
        });
        // Set the action for the add menu item
        addItem.setOnAction(event -> {
            Film newFilm = new Film();
            presenter.addFilm(newFilm);
            setContextMenuOptions();
            filmTableView.getSelectionModel().select(newFilm);
        });
        // Set the action to view images based on the selected entry
        viewImagesItem.setOnAction(event -> {
            Film selectedFilm = filmTableView.getSelectionModel().getSelectedItem();
            if (selectedFilm != null) {
                showImagesPopup(selectedFilm);
            }
        });
        // Set the action to view list of roles based on the selected entry
        viewRolesItem.setOnAction(event -> {
            Film selectedFilm = filmTableView.getSelectionModel().getSelectedItem();
            if (selectedFilm != null) {
                showRolesTable(selectedFilm);
            }
        });

        contextMenu.getItems().addAll(deleteItem, addItem, viewImagesItem, viewRolesItem, exportCsvItem, exportDocxItem, filterMenu);
        // Show the context menu when right-clicking on a row in the table
        filmTableView.setContextMenu(contextMenu);
    }

    // Sets Edit entry event
    private void setEditable(TableColumn<Film, String> column) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            Film Film = event.getRowValue();
            String newValue = event.getNewValue();

            switch (column.getText()){
                case "Title":
                    Film.setTitle(newValue);
                    break;
                case "Type":
                    Film.setType(newValue);
                    break;
                case "Category":
                    Film.setCategory(newValue);
                    break;
            }
            presenter.updateFilm(getSelectedItem());
            filmTableView.refresh();
        });
    }

    private void setEditableInteger(TableColumn<Film, Integer> column) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column.setOnEditCommit(event -> {
            Film film = event.getRowValue();
            Integer newValue = event.getNewValue();

            switch (column.getText()) {
                case "Year":
                    film.setYear(newValue);
                    break;
                case "Director ID":
                    film.setDirectorId(newValue);
                    break;
                case "Writer ID":
                    film.setWriterId(newValue);
                    break;
                case "Producer ID":
                    film.setProducerId(newValue);
                    break;
            }
            presenter.updateFilm(getSelectedItem());
            filmTableView.refresh();
        });
    }

    @Override
    public void setItemList(List<Film> Films) {
        filmTableView.setItems(FXCollections.observableArrayList(Films));
        filmTableView.refresh();
    }

    private void enterYearPopupWindow(){
        Stage yearStage = new Stage();
        yearStage.setTitle("Enter Year");

        // Create a VBox for the layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        // Create a Label
        Label label = new Label("Enter Year:");

        // Create a TextField to input the year
        TextField yearField = new TextField();

        // Create a Button to submit
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String input = yearField.getText();
            if (input != null && !input.isEmpty()) {
                try {
                    int year = Integer.parseInt(input);  // Check if it's a valid integer
                    presenter.loadFilmsByYear(year);  // Call the presenter to fetch films
                    yearStage.close();  // Close the popup
                } catch (NumberFormatException ex) {
                    showError("Please enter a valid year.");
                }
            } else {
                showError("Year cannot be empty.");
            }
        });

        // Add the label, text field, and button to the VBox
        vbox.getChildren().addAll(label, yearField, submitButton);

        // Set the Scene and show the stage
        Scene scene = new Scene(vbox, 300, 150);
        yearStage.setScene(scene);
        yearStage.show();
    }

    private void enterActorPopupWindow(){
        Stage actorStage = new Stage();
        actorStage.setTitle("Enter Actor Name");

        // Create a VBox for the layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        // Create a Label
        Label label = new Label("Enter Actor Name:");

        // Create a TextField to input the actor name
        TextField actorField = new TextField();
        actorField.setPromptText("e.g., Brad Pitt");

        // Create a Button to submit
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String input = actorField.getText();
            if (input != null && !input.isEmpty()) {
                presenter.loadFilmsByActor(input);  // Call the presenter to fetch films by actor
                actorStage.close();  // Close the popup
            } else {
                showError("Actor name cannot be empty.");
            }
        });

        // Add the label, text field, and button to the VBox
        vbox.getChildren().addAll(label, actorField, submitButton);

        // Set the Scene and show the stage
        Scene scene = new Scene(vbox, 300, 150);
        actorStage.setScene(scene);
        actorStage.show();
    }
    @Override
    public void showImagesPopup(Film film) {
        List<FilmImage> paths = new ArrayList<>(film.getImages());
        while (paths.size() < 3) {
            paths.add(new FilmImage(0, film.getId(), ""));
        }

        Stage imageStage = new Stage();
        imageStage.setTitle("Images Preview");

        HBox mainContainer = new HBox(20);
        mainContainer.setPadding(new Insets(10));

        VBox inputContainer = new VBox(10);
        inputContainer.setAlignment(Pos.TOP_LEFT);

        VBox imageContainer = new VBox(10);
        imageContainer.setAlignment(Pos.TOP_RIGHT);

        List<TextField> urlFields = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            HBox entry = new HBox(10);
            entry.setAlignment(Pos.CENTER_LEFT);

            String path = paths.get(i).getUrl();
            String imageUrl = "file:///" + path.replace("\\", "/");

            // Image View
            ImageView imageView = new ImageView();
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);

            try {
                Image image = new Image(imageUrl, true);
                imageView.setImage(image);
            } catch (Exception e) {
                imageView.setImage(null);
            }

            // Editable text field
            TextField urlField = new TextField(path);
            urlField.setPrefWidth(250);
            urlFields.add(urlField);

            entry.getChildren().add(urlField);
            inputContainer.getChildren().add(entry);
            imageContainer.getChildren().add(imageView);
        }

        // Save button
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(event -> {
            for (int i = 0; i < 3; i++) {
                paths.get(i).setUrl(urlFields.get(i).getText());
            }
            film.setImages(paths);
            presenter.updateFilm(film);
            imageStage.close();
        });

        inputContainer.getChildren().add(saveButton);

        mainContainer.getChildren().addAll(inputContainer, imageContainer);

        Scene scene = new Scene(mainContainer, 400, 600);
        imageStage.setScene(scene);
        imageStage.show();
    }
    @Override
    public void showRolesTable(Film film) {
        List<Cast> roles = film.getCast();
        Stage roleStage = new Stage();
        roleStage.setTitle("Film Roles");

        TableView<Cast> rolesTableView = new TableView<>();
        rolesTableView.setEditable(true);

        // Column for Actor ID (Editable)
        TableColumn<Cast, Integer> actorIdColumn = new TableColumn<>("Actor ID");
        actorIdColumn.setCellValueFactory(new PropertyValueFactory<>("idActor"));
        actorIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        actorIdColumn.setOnEditCommit(event -> {
            Cast cast = event.getRowValue();
            cast.setIdActor(event.getNewValue());
        });

        // Column for Role (Editable)
        TableColumn<Cast, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        roleColumn.setOnEditCommit(event -> {
            Cast cast = event.getRowValue();
            cast.setRole(event.getNewValue());
        });

        rolesTableView.getColumns().addAll(actorIdColumn, roleColumn);
        rolesTableView.getItems().addAll(roles);

        // Add button
        Button addButton = new Button("Add Role");
        addButton.setOnAction(event -> {
            Cast newCast = new Cast(); // Default values
            rolesTableView.getItems().add(newCast);
            film.getCast().add(newCast);
        });

        // Delete button
        Button deleteButton = new Button("Delete Role");
        deleteButton.setOnAction(event -> {
            Cast selected = rolesTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                rolesTableView.getItems().remove(selected);
                film.getCast().remove(selected);
            }
        });

        // Save button
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(event -> {
            film.setCast(new ArrayList<>(rolesTableView.getItems())); // Update Film's cast list
            presenter.updateFilm(film); // Trigger update
            roleStage.close();
        });

        HBox buttonContainer = new HBox(10, addButton, deleteButton, saveButton);
        buttonContainer.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, rolesTableView, buttonContainer);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 400);
        roleStage.setScene(scene);
        roleStage.show();
    }

    @Override
    public void showError(String message) {
        // Create an alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);

        // Show the alert
        alert.showAndWait();
    }
    @Override
    public void showConfirmation(String message) {
        // Create an alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Confirmation message");
        alert.setContentText(message);

        // Show the alert
        alert.showAndWait();
    }

    @Override
    public Film getSelectedItem() {
        return filmTableView.getSelectionModel().getSelectedItem();
    }
    @Override
    public VBox getView() {
        return view;
    }

}
