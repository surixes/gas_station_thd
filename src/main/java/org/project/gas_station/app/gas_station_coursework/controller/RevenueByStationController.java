package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import org.hibernate.Session;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class RevenueByStationController {

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button showRevenueButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView<RevenueRow> revenueTable;
    @FXML
    private TableColumn<RevenueRow, String> stationColumn;
    @FXML
    private TableColumn<RevenueRow, String> addressColumn;
    @FXML
    private TableColumn<RevenueRow, String> revenueColumn;

    @FXML
    public void initialize() {
        stationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStationName()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        revenueColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getRevenue())));
    }

    @FXML
    private void handleShowRevenue() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Ошибка выбора дат");
            alert.setContentText("Пожалуйста, выберите корректный период.");
            alert.showAndWait();
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                    SELECT s.station.firm.name, s.station.address, SUM(s.quantity * pd.price)
                    FROM Sale s
                    JOIN s.fuel f
                    JOIN PriceDynamics pd ON pd.fuel = f AND s.saleDate BETWEEN pd.startDate AND pd.endDate
                    WHERE s.saleDate BETWEEN :start AND :end
                    GROUP BY s.station.firm.name, s.station.address
                    ORDER BY SUM(s.quantity * pd.price) DESC
                    """;

            List<Object[]> results = session.createQuery(hql, Object[].class)
                    .setParameter("start", startDate)
                    .setParameter("end", endDate)
                    .getResultList();

            ObservableList<RevenueRow> revenueData = FXCollections.observableArrayList();
            for (Object[] row : results) {
                revenueData.add(new RevenueRow(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).doubleValue()));
            }

            revenueTable.setItems(revenueData);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ошибка загрузки данных");
            alert.setContentText("Не удалось получить данные из базы.");
            alert.showAndWait();
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

    /**
     * Вспомогательный класс для представления строки таблицы.
     */
    public static class RevenueRow {
        private final String stationName;
        private final String address;
        private final double revenue;

        public RevenueRow(String stationName, String address, double revenue) {
            this.stationName = stationName;
            this.address = address;
            this.revenue = revenue;
        }

        public String getStationName() {
            return stationName;
        }

        public String getAddress() {
            return address;
        }

        public double getRevenue() {
            return revenue;
        }
    }
}
