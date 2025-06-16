package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;

import org.hibernate.Session;

public class PreparedQueriesController {

    @FXML
    private Button backButton;

    @FXML
    private void handleBack() throws IOException {
        Parent prevView = FXMLLoader.load(getClass().getResource(
                "/org/project/gas_station/app/gas_station_coursework/app/StartScreen.fxml"));
        Scene scene = backButton.getScene();
        scene.setRoot(prevView);
        if (!scene.getStylesheets().contains("/styles/main.css")) {
            scene.getStylesheets().add("/styles/main.css");
        }
    }

    @FXML
    private void onQuery1(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query1FuelSales.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Продажи топлива — фильтр");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery2(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query2.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Выручка АЗС за период");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery3(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query3.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Список поставщиков топлива");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery4(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query4.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Список поставщиков топлива");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery5(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query5.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Добавление АЗС");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery6(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query6.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("АЗС без конкретного топлива");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery7(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query7.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Объём продаж по видам топлива");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery8(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query8.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Кол-во АЗС у каждой фирмы");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery9(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/Query9.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Актуальные цены");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    @FXML
    private void onQuery10(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/project/gas_station/app/gas_station_coursework/app/QueryAnonymizeUser.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Анонимизация клиента");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Не удалось загрузить страницу").showAndWait();
        }
    }

    // Реализация запуска запросов
    private void executeReportQuery(String title, String sql) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var list = session.createNativeQuery(sql).list();
            // TODO: Преобразовать результат в ObservableList и показать через
            // ResultViewController
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Ошибка:\n" + e.getMessage()).showAndWait();
        }
    }

    private void executeModifyQuery(String title, String sql) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var tx = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            tx.commit();
            new Alert(Alert.AlertType.INFORMATION, title + " выполнен").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Ошибка:\n" + e.getMessage()).showAndWait();
        }
    }
}
