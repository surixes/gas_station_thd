package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.project.gas_station.app.gas_station_coursework.model.Firm;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

public class Query8Controller {
    @FXML
    private ComboBox<String> firmCombo;
    @FXML
    private TableView<FirmRow> firmsTable;
    @FXML
    private TableColumn<FirmRow, String> firmNameColumn;
    @FXML
    private TableColumn<FirmRow, String> stationCountColumn;
    @FXML
    private Button showButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Заполнение ComboBox фирмами
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> firmNames = session.createQuery("SELECT f.name FROM Firm f ORDER BY f.name", String.class)
                    .getResultList();
            firmCombo.setItems(FXCollections.observableArrayList(firmNames));
            firmCombo.getItems().add(0, "Все фирмы");
            firmCombo.getSelectionModel().selectFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Настройка колонок таблицы
        firmNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirmName()));
        stationCountColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationCount()));
    }

    @FXML
    private void handleShow() {
        String selectedFirm = firmCombo.getValue();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql;
            List<Object[]> results;
            if (selectedFirm == null || selectedFirm.equals("Все фирмы")) {
                hql = "SELECT f.name, COUNT(gs.id) FROM GasStation gs JOIN gs.firm f GROUP BY f.name ORDER BY f.name";
                results = session.createQuery(hql, Object[].class).getResultList();
            } else {
                hql = "SELECT f.name, COUNT(gs.id) FROM GasStation gs JOIN gs.firm f WHERE f.name = :firmName GROUP BY f.name";
                results = session.createQuery(hql, Object[].class)
                        .setParameter("firmName", selectedFirm)
                        .getResultList();
            }
            ObservableList<FirmRow> tableData = FXCollections.observableArrayList();
            for (Object[] row : results) {
                tableData.add(new FirmRow((String) row[0], String.valueOf(row[1])));
            }
            firmsTable.setItems(tableData);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка загрузки данных.");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Parent prevView = FXMLLoader.load(getClass().getResource(
                "/org/project/gas_station/app/gas_station_coursework/app/PreparedQueries.fxml"));
        VBox contentPane = (VBox) backButton.getScene().lookup("#contentPane");
        if (contentPane != null) {
            contentPane.getChildren().setAll(prevView);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Вспомогательный класс для строк таблицы
    public static class FirmRow {
        private final String firmName;
        private final String stationCount;

        public FirmRow(String firmName, String stationCount) {
            this.firmName = firmName;
            this.stationCount = stationCount;
        }

        public String getFirmName() {
            return firmName;
        }

        public String getStationCount() {
            return stationCount;
        }
    }
}