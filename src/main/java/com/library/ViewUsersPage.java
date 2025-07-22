package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class ViewUsersPage extends VBox {

    private TableView<User> tableView;
    private ObservableList<User> userList;
    private LibraryService service;

    public ViewUsersPage(Stage stage, User admin) {
        setSpacing(10);
        setPadding(new Insets(15));

        Label title = new Label("Manage Users");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        service = new LibraryService();
        userList = FXCollections.observableArrayList(service.getAllUsers().values());

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(userList);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> {
            boolean isAdmin = data.getValue().isAdmin();
            return new javafx.beans.property.SimpleStringProperty(isAdmin ? "Admin" : "User");
        });

        tableView.getColumns().addAll(usernameCol, nameCol, contactCol, roleCol);

        Button editBtn = new Button("Edit Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button backBtn = new Button("Back");

        editBtn.setOnAction(e -> editUser());
        deleteBtn.setOnAction(e -> deleteUser());
        backBtn.setOnAction(e -> {
            // AdminDashboard dashboard = new AdminDashboard(stage, admin);
            // stage.setScene(new Scene(dashboard, 600, 400));
            stage.getScene().setRoot(new AdminDashboard(stage, admin));
        });

        HBox controls = new HBox(10, editBtn, deleteBtn, backBtn);
        getChildren().addAll(title, tableView, controls);
    }

    private void editUser() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a user to edit.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit User");

        TextField nameField = new TextField(selected.getName());
        TextField contactField = new TextField(selected.getContact());
        CheckBox isAdminCheck = new CheckBox("Is Admin");
        isAdminCheck.setSelected(selected.isAdmin());

        VBox content = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Contact:"), contactField,
                isAdminCheck);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                selected.setName(nameField.getText().trim());
                selected.setContact(contactField.getText().trim());
                selected.setAdmin(isAdminCheck.isSelected());

                service.getAllUsers().put(selected.getUsername(), selected);
                service.saveUsers();
                refreshTable();
            }
        });
    }

    private void deleteUser() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a user to delete.");
            return;
        }

        if (selected.isAdmin()) {
            showAlert("You cannot delete another admin.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete user " + selected.getUsername() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                service.getAllUsers().remove(selected.getUsername());
                service.saveUsers();
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        userList.setAll(service.getAllUsers().values());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
