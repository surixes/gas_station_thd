package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomQueryController {
    @FXML
    private Button backButton;
    @FXML
    private TextField queryField;
    @FXML
    private Button executeButton;
    @FXML
    private TableView<ObservableList<String>> resultTable;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("");
    }

    @FXML
    private void handleBack() throws IOException {
        Parent prevView = FXMLLoader.load(getClass().getResource(
                "/org/project/gas_station/app/gas_station_coursework/app/StartScreen.fxml"));
        Scene scene = backButton.getScene();
        scene.setRoot(prevView);
        // Повторно добавьте CSS, если нужно
        if (!scene.getStylesheets().contains("/styles/main.css")) {
            scene.getStylesheets().add("/styles/main.css");
        }
    }

    @FXML
    private void handleExecute() {
        String userQuery = queryField.getText();
        if (userQuery == null || userQuery.trim().isEmpty()) {
            statusLabel.setText("Введите запрос!");
            return;
        }
        // Примеры преобразования пользовательских запросов в SQL
        String sql = mapUserQueryToSql(userQuery.trim());
        if (sql == null) {
            statusLabel.setText("Не удалось распознать запрос. Попробуйте иначе.");
            return;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<?> query = session.createNativeQuery(sql);
            List<?> resultList = query.getResultList();
            resultTable.getColumns().clear();
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            if (!resultList.isEmpty()) {
                Object firstRow = resultList.get(0);
                int columnCount;
                if (firstRow instanceof Object[]) {
                    columnCount = ((Object[]) firstRow).length;
                } else {
                    columnCount = 1;
                }
                // Создаём колонки
                for (int i = 0; i < columnCount; i++) {
                    final int colIndex = i;
                    TableColumn<ObservableList<String>, String> col = new TableColumn<>("Колонка " + (i + 1));
                    col.setCellValueFactory(
                            param -> new javafx.beans.property.SimpleStringProperty(param.getValue().get(colIndex)));
                    resultTable.getColumns().add(col);
                }
                // Заполняем строки
                for (Object rowObj : resultList) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    if (rowObj instanceof Object[]) {
                        for (Object cell : (Object[]) rowObj) {
                            row.add(cell != null ? cell.toString() : "");
                        }
                    } else {
                        row.add(rowObj != null ? rowObj.toString() : "");
                    }
                    data.add(row);
                }
            }
            resultTable.setItems(data);
            statusLabel.setText("Найдено строк: " + data.size());
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка выполнения запроса: " + e.getMessage());
        }
    }

    // Примитивный маппер пользовательских запросов на SQL
    private String mapUserQueryToSql(String userQuery) {
        String q = userQuery.toLowerCase();
        // Если пользователь ввёл обычный SQL-запрос
        if (q.startsWith("select") || q.startsWith("show") || q.startsWith("with")) {
            return userQuery;
        }
        if (q.contains("клиент")) {
            return "SELECT id, name, phone, address FROM clients";
        }
        if (q.contains("продаж")) {
            return "SELECT id, sale_date, quantity, client_id, fuel_id, station_id FROM sales";
        }
        if (q.contains("топлив")) {
            return "SELECT id, type, unit FROM fuels";
        }
        if (q.contains("фирм")) {
            return "SELECT id, name, legal_address FROM firms";
        }
        if (q.contains("азс") || q.contains("станц")) {
            return "SELECT id, address, firm_id FROM gas_stations";
        }
        if (q.contains("цен")) {
            return "SELECT id, price, start_date, end_date, fuel_id, firm_id FROM price_dynamics";
        }
        // Можно добавить больше шаблонов
        return null;
    }
}