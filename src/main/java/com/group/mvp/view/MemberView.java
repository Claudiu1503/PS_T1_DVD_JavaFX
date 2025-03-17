package com.group.mvp.view;

import com.group.mvp.model.Member;
import com.group.mvp.presenter.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.* ;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MemberView implements IMemberView {
    private MemberPresenter presenter;
    private VBox view;

    public MemberView() {
        memberTableView = new TableView<>();
        setupTable();

        view = new VBox(10, memberTableView);
        presenter = new MemberPresenter(this);
    }
    private void setupTable(){
        // Enable editing the table
        memberTableView.setEditable(true);

        // Sets delete and add entry event + filter options
        setContextMenuOptions();

        // Create table columns
        TableColumn<Member, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setEditable(false); // ID should not be editable

        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        setEditable(nameColumn); // Set update entry event for each column

        TableColumn<Member, String> birthdateColumn = new TableColumn<>("Birthdate");
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        setEditable(birthdateColumn);

        TableColumn<Member, String> baseTypeColumn = new TableColumn<>("Base Type");
        baseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("baseType"));
        setEditable(baseTypeColumn);

        TableColumn<Member, String> imageColumn = new TableColumn<>("Image");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        setEditable(imageColumn);

        // Add columns to table
        memberTableView.getColumns().addAll(idColumn, nameColumn, birthdateColumn, baseTypeColumn, imageColumn);

    }
    private TableView<Member> memberTableView;
    // Sets context menu with different options
    private void setContextMenuOptions(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem addItem = new MenuItem("Add");
        MenuItem viewImageItem = new MenuItem("View Image");

        Menu filterMenu = new Menu("Filter By");
        MenuItem directorItem = new MenuItem("directors");
        MenuItem producerItem = new MenuItem("producers");
        MenuItem writerItem = new MenuItem("writers");
        MenuItem actorItem = new MenuItem("actors");

        filterMenu.getItems().addAll(directorItem, producerItem, writerItem, actorItem);

        directorItem.setOnAction(event -> presenter.loadMembers("DIRECTOR"));
        producerItem.setOnAction(event -> presenter.loadMembers("PRODUCER"));
        writerItem.setOnAction(event -> presenter.loadMembers("WRITER"));
        actorItem.setOnAction(event -> presenter.loadMembers("ACTOR"));

        deleteItem.setOnAction(event -> {
            Member selectedMember = getSelectedItem();
            if (selectedMember != null) {
                presenter.deleteMember(selectedMember);
                memberTableView.getItems().remove(selectedMember);
            }
        });

        addItem.setOnAction(event -> {
            Member newMember = new Member();
            presenter.addMember(newMember);
            memberTableView.getSelectionModel().select(newMember);
        });

        viewImageItem.setOnAction(event -> {
            Member selectedMember = getSelectedItem();
            if (selectedMember != null) {
                showImagePopup(selectedMember.getImage());
            }
        });

        contextMenu.getItems().add(deleteItem);
        contextMenu.getItems().add(addItem);
        contextMenu.getItems().add(viewImageItem);
        contextMenu.getItems().add(filterMenu);

        // Note: context menu shows up by right-clicking
        memberTableView.setContextMenu(contextMenu);
    }

    // Sets Edit entry event
    @Override
    public void setEditable(TableColumn<Member, String> column) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            Member member = event.getRowValue();
            String newValue = event.getNewValue();

            switch (column.getText()){
                case "Name":
                    member.setName(newValue);
                    break;
                case "Birthdate":
                    try {
                        member.setBirthDate(newValue);
                    }catch(IllegalArgumentException e){
                        showError(e.getMessage());
                    }
                    break;
                case "Base Type":
                    member.setBaseType(newValue);
                    break;
                case "Image":
                    member.setImage(newValue);
                    break;
            }
            presenter.updateMember(getSelectedItem());
            memberTableView.refresh();
        });
    }

    @Override
    public void setItemList(List<Member> Members) {
        memberTableView.setItems(FXCollections.observableArrayList(Members));
        memberTableView.refresh();
    }
    @Override
    public void showImagePopup(String path) {
        Stage imageStage = new Stage();
        imageStage.setTitle("Image Preview");

        ImageView imageView = new ImageView();
        try {
            //Convert Windows path to JavaFX-compatible URI
            String imageUrl = "file:///" + path.replace("\\", "/");
           // System.out.println("Converted URL: " + imageUrl);

            Image image = new Image(imageUrl, true);
            imageView.setImage(image);
            imageView.setFitWidth(250);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            showError("Failed to load image.");
            return;
        }

        VBox layout = new VBox(imageView);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 250);
        imageStage.setScene(scene);
        imageStage.show();
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);

        alert.showAndWait();
    }

    @Override
    public Member getSelectedItem() {
        return memberTableView.getSelectionModel().getSelectedItem();
    }


    @Override
    public VBox getView() {
        return view;
    }
}
