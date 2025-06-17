package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.property.SimpleStringProperty;
import org.project.gas_station.app.gas_station_coursework.model.Sale;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;
import org.hibernate.Session;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class Query1FuelSalesController {

    @FXML
    private ComboBox<String> fuelTypeCombo;
    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private TableColumn<Sale, String> dateColumn;
    @FXML
    private TableColumn<Sale, Double> quantityColumn;
    @FXML
    private TableColumn<Sale, String> addressColumn;
    @FXML
    private Button showSalesButton;
    @FXML
    private Button backButton;


    @FXML
    public void initialize() {
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> fuelTypes = session
                    .createQuery("SELECT f.type FROM Fuel f", String.class)
                    .getResultList();
            fuelTypeCombo.setItems(FXCollections.observableArrayList(fuelTypes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("saleDate"));
        quantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));
        addressColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getStation().getAddress()));
    }

    @FXML
    private void handleShowSales() {
        String selectedType = fuelTypeCombo.getValue();
        if (selectedType == null || selectedType.isEmpty()) {
            return;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hqlQuery = "SELECT s FROM Sale s " +
                    "JOIN FETCH s.station JOIN FETCH s.fuel " +
                    "WHERE s.fuel.type = :fuelType " +
                    "ORDER BY s.saleDate DESC";
            List<Sale> resultList = session.createQuery(hqlQuery, Sale.class)
                    .setParameter("fuelType", selectedType)
                    .getResultList();
            ObservableList<Sale> salesData = FXCollections.observableArrayList(resultList);
            salesTable.setItems(salesData);
        } catch (Exception e) {
            e.printStackTrace();
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
}
