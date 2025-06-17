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
import org.project.gas_station.app.gas_station_coursework.model.Sale;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesVolumeByFuelType {

    @FXML
    private ComboBox<String> fuelTypeCombo;
    @FXML
    private TableView<SaleRow> salesTable;
    @FXML
    private TableColumn<SaleRow, String> dateColumn;
    @FXML
    private TableColumn<SaleRow, String> quantityColumn;
    @FXML
    private TableColumn<SaleRow, String> addressColumn;
    @FXML
    private Button showSalesButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> fuelTypes = session
                    .createQuery("SELECT DISTINCT f.type FROM Fuel f", String.class)
                    .getResultList();
            fuelTypeCombo.setItems(FXCollections.observableArrayList(fuelTypes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSaleDate()));
        quantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuantity()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
    }

    @FXML
    private void handleShowSales() {
        String selectedType = fuelTypeCombo.getValue();
        if (selectedType == null || selectedType.isEmpty()) {
            showAlert("Выберите тип топлива!");
            return;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Sale s JOIN FETCH s.station st JOIN FETCH s.fuel f WHERE f.type = :fuelType ORDER BY s.saleDate DESC";
            List<Sale> sales = session.createQuery(hql, Sale.class)
                    .setParameter("fuelType", selectedType)
                    .getResultList();

            ObservableList<SaleRow> tableData = FXCollections.observableArrayList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            for (Sale sale : sales) {
                tableData.add(new SaleRow(
                        sale.getSaleDate() != null ? sale.getSaleDate().format(formatter) : "",
                        sale.getQuantity() != null ? sale.getQuantity().toString() : "",
                        sale.getStation() != null ? sale.getStation().getAddress() : ""));
            }
            salesTable.setItems(tableData);
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

    public static class SaleRow {
        private final String saleDate;
        private final String quantity;
        private final String address;

        public SaleRow(String saleDate, String quantity, String address) {
            this.saleDate = saleDate;
            this.quantity = quantity;
            this.address = address;
        }

        public String getSaleDate() {
            return saleDate;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getAddress() {
            return address;
        }
    }
}