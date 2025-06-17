package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.project.gas_station.app.gas_station_coursework.model.Firm;
import org.project.gas_station.app.gas_station_coursework.model.Fuel;
import org.project.gas_station.app.gas_station_coursework.model.PriceDynamics;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CurrentFuelPricesController {

    @FXML
    private ComboBox<Firm> firmComboBox;
    @FXML
    private ComboBox<Fuel> fuelTypeCombo;
    @FXML
    private TableView<PriceRow> pricesTable;
    @FXML
    private TableColumn<PriceRow, String> firmColumn;
    @FXML
    private TableColumn<PriceRow, String> fuelTypeColumn;
    @FXML
    private TableColumn<PriceRow, String> priceColumn;
    @FXML
    private TableColumn<PriceRow, String> startDateColumn;
    @FXML
    private TableColumn<PriceRow, String> endDateColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        firmColumn.setCellValueFactory(new PropertyValueFactory<>("firmName"));
        fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        loadFirms();
        loadFuelTypes();
    }

    private void loadFirms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Firm> firms = session.createQuery("FROM Firm f WHERE f.isOwner = true", Firm.class).getResultList();
            firmComboBox.setItems(FXCollections.observableArrayList(firms));
        }
    }

    private void loadFuelTypes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Fuel> fuels = session.createQuery("FROM Fuel", Fuel.class).getResultList();
            fuelTypeCombo.setItems(FXCollections.observableArrayList(fuels));
        }
    }

    @FXML
    private void handleRefresh() {
        Firm selectedFirm = firmComboBox.getValue();
        Fuel selectedFuel = fuelTypeCombo.getValue();

        if (selectedFirm == null || selectedFuel == null) {
            showAlert("Ошибка выбора", "Пожалуйста, выберите фирму и тип топлива.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM PriceDynamics pd " +
                    "WHERE pd.firm.id = :firmId " +
                    "AND pd.fuel.id = :fuelId " +
                    "AND (pd.endDate IS NULL OR pd.endDate >= CURRENT_DATE) " +
                    "AND pd.startDate <= CURRENT_DATE " +
                    "ORDER BY pd.startDate DESC";

            List<PriceDynamics> prices = session.createQuery(hql, PriceDynamics.class)
                    .setParameter("firmId", selectedFirm.getId())
                    .setParameter("fuelId", selectedFuel.getId())
                    .setMaxResults(1)
                    .getResultList();

            ObservableList<PriceRow> tableData = FXCollections.observableArrayList();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (!prices.isEmpty()) {
                PriceDynamics pd = prices.get(0);
                String endDate = (pd.getEndDate() != null) ? pd.getEndDate().format(fmt) : "Бессрочно";

                tableData.add(new PriceRow(
                        selectedFirm.getName(),
                        selectedFuel.getType(),
                        String.format("%.2f", pd.getPrice()),
                        pd.getStartDate().format(fmt),
                        endDate));
            }

            pricesTable.setItems(tableData);

            if (tableData.isEmpty()) {
                showAlert("Результат", "Не найдено актуальных цен для выбранных критериев.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось обновить данные: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() throws IOException {
        Scene scene = backButton.getScene();
        if (scene != null) {
            Parent prevView = FXMLLoader.load(getClass().getResource(
                    "/org/project/gas_station/app/gas_station_coursework/app/PreparedQueries.fxml"));
            scene.setRoot(prevView);
        }
    }

    public static class PriceRow {
        private final SimpleStringProperty firmName;
        private final SimpleStringProperty fuelType;
        private final SimpleStringProperty price;
        private final SimpleStringProperty startDate;
        private final SimpleStringProperty endDate;

        public PriceRow(String firmName, String fuelType, String price, String startDate, String endDate) {
            this.firmName = new SimpleStringProperty(firmName);
            this.fuelType = new SimpleStringProperty(fuelType);
            this.price = new SimpleStringProperty(price);
            this.startDate = new SimpleStringProperty(startDate);
            this.endDate = new SimpleStringProperty(endDate);
        }

        public String getFirmName() {
            return firmName.get();
        }

        public String getFuelType() {
            return fuelType.get();
        }

        public String getPrice() {
            return price.get();
        }

        public String getStartDate() {
            return startDate.get();
        }

        public String getEndDate() {
            return endDate.get();
        }
    }
}