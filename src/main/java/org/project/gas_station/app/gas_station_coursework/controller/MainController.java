package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class MainController {

    @FXML
    private Label statusLabel;

    @FXML
    private void onPreparedQueries(ActionEvent e) {
        loadScene(e, "/org/project/gas_station/app/gas_station_coursework/app/PreparedQueries.fxml",
                "Готовые запросы");
    }

    @FXML
    private void onCustomQuery(ActionEvent e) {
        loadScene(e, "/org/project/gas_station/app/gas_station_coursework/app/CustomQuery.fxml", "Свой запрос");
    }

    @FXML
    private void onExit(ActionEvent e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }

    private void loadScene(ActionEvent e, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Не удалось загрузить страницу").showAndWait();
        }
    }
}
