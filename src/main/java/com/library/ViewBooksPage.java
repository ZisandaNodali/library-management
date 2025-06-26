package com.library;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class ViewBooksPage extends BorderPane {

    public ViewBooksPage(Stage stage, User admin) {
        setPadding(new Insets(20));

        Label title = new Label("All Books in Library");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Book> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Book, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Book, String> libraryCol = new TableColumn<>("Library");
        libraryCol.setCellValueFactory(new PropertyValueFactory<>("library"));

        TableColumn<Book, String> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().isAvailable() ? "Yes" : "No"));

        tableView.getColumns().addAll(titleCol, authorCol, genreCol, yearCol, libraryCol, availableCol);

        LibraryService service = new LibraryService();
        tableView.setItems(FXCollections.observableArrayList(service.getAllBooks().values()));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            AdminDashboard dashboard = new AdminDashboard(stage, admin);
            stage.setScene(new Scene(dashboard, 600, 400));
        });

        VBox topBox = new VBox(10, title);
        topBox.setPadding(new Insets(10));

        setTop(topBox);
        setCenter(tableView);
        setBottom(new VBox(10, backButton));
    }
}
