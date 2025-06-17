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
import org.project.gas_station.app.gas_station_coursework.model.Fuel;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

public class FuelSuppliersController {

    @FXML
    private VBox contentPane;
    @FXML
    private ComboBox<Fuel> fuelTypeCombo;
    @FXML
    private Button showSuppliersButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView<SupplierRow> suppliersTable;
    @FXML
    private TableColumn<SupplierRow, String> supplierNameColumn;
    @FXML
    private TableColumn<SupplierRow, String> legalAddressColumn;
    @FXML
    private TableColumn<SupplierRow, String> legalNumberColumn;

    @FXML
    public void initialize() {
        supplierNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        legalAddressColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLegalAddress()));
        legalNumberColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLegalNumber()));

        loadFuelTypes();
    }

    private void loadFuelTypes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Fuel> fuels = session.createQuery("from Fuel", Fuel.class).list();
            fuelTypeCombo.setItems(FXCollections.observableArrayList(fuels));
        }
    }

    @FXML
    private void handleShowSuppliers() {
        Fuel selectedFuel = fuelTypeCombo.getValue();

        if (selectedFuel == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Не выбрано топливо");
            alert.setContentText("Пожалуйста, выберите тип топлива.");
            alert.showAndWait();
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                    SELECT f.name, f.legalAddress, f.legalNumber
                    FROM SupplierFuel sf
                    JOIN sf.firm f
                    WHERE sf.fuel = :fuel
                    """;

            List<Object[]> results = session.createQuery(hql, Object[].class)
                    .setParameter("fuel", selectedFuel)
                    .list();

            ObservableList<SupplierRow> supplierData = FXCollections.observableArrayList();
            for (Object[] row : results) {
                supplierData.add(new SupplierRow(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2]));
            }

            suppliersTable.setItems(supplierData);
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Parent prevView = FXMLLoader.load(getClass().getResource(
                "/org/project/gas_station/app/gas_station_coursework/app/PreparedQueries.fxml"));
        contentPane.getChildren().setAll(prevView);
    }

    public static class SupplierRow {
        private final String name;
        private final String legalAddress;
        private final String legalNumber;

        public SupplierRow(String name, String legalAddress, String legalNumber) {
            this.name = name;
            this.legalAddress = legalAddress;
            this.legalNumber = legalNumber;
        }

        public String getName() {
            return name;
        }

        public String getLegalAddress() {
            return legalAddress;
        }

        public String getLegalNumber() {
            return legalNumber;
        }
    }
}
