package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.project.gas_station.app.gas_station_coursework.dao.*;
import org.project.gas_station.app.gas_station_coursework.model.*;

import java.time.LocalDate;

public class FormViewController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField clientNameField;
    @FXML
    private TextField clientPhoneField;
    @FXML
    private TextField clientAddressField;

    @FXML
    private TextField stationAddressField;
    @FXML
    private ComboBox<Firm> stationFirmCombo;

    @FXML
    private ComboBox<Client> saleClientCombo;
    @FXML
    private ComboBox<Fuel> saleFuelCombo;
    @FXML
    private ComboBox<GasStation> saleStationCombo;
    @FXML
    private DatePicker saleDatePicker;
    @FXML
    private TextField saleQuantityField;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Stage stage;
    private EntityType entityType;
    private Object entity;

    public enum EntityType {
        CLIENT, STATION, SALE
    }

    public void initForm(Stage stage, EntityType type, Object entity) {
        this.stage = stage;
        this.entityType = type;
        this.entity = entity;
        titleLabel.setText(type == EntityType.CLIENT ? "Клиент" : type == EntityType.STATION ? "АЗС" : "Продажа");

        if (type == EntityType.CLIENT) {
            Client c = (Client) entity;
            if (c != null) {
                clientNameField.setText(c.getName());
                clientPhoneField.setText(c.getPhone());
                clientAddressField.setText(c.getAddress());
            }
        } else if (type == EntityType.STATION) {
            GasStationDAO dao = new GasStationDAO();
            stationFirmCombo.getItems().setAll(dao.getAllFirms());
            GasStation s = (GasStation) entity;
            if (s != null) {
                stationAddressField.setText(s.getAddress());
                stationFirmCombo.getSelectionModel().select(s.getFirm());
            }
        } else if (type == EntityType.SALE) {
            SaleDAO dao = new SaleDAO();
            saleClientCombo.getItems().setAll(dao.getAllClients());
            saleFuelCombo.getItems().setAll(new FuelDAO().getAll());
            saleStationCombo.getItems().setAll(new GasStationDAO().getAll());
            if (entity != null) {
                Sale s = (Sale) entity;
                saleClientCombo.getSelectionModel().select(s.getClient());
                saleFuelCombo.getSelectionModel().select(s.getFuel());
                saleStationCombo.getSelectionModel().select(s.getStation());
                saleDatePicker.setValue(s.getSaleDate());
                saleQuantityField.setText(s.getQuantity().toString());
            } else {
                saleDatePicker.setValue(LocalDate.now());
            }
        }
    }

    @FXML
    private void onSave() {
        try {
            if (entityType == EntityType.CLIENT) {
                ClientDAO dao = new ClientDAO();
                Client c = entity != null ? (Client) entity : new Client();
                c.setName(clientNameField.getText());
                c.setPhone(clientPhoneField.getText());
                c.setAddress(clientAddressField.getText());
                if (entity != null)
                    dao.update(c);
                else
                    dao.save(c);
            } else if (entityType == EntityType.STATION) {
                GasStation s = entity != null ? (GasStation) entity : new GasStation();
                s.setAddress(stationAddressField.getText());
                s.setFirm(stationFirmCombo.getValue());
                if (entity != null)
                    new GasStationDAO().update(s);
                else
                    new GasStationDAO().save(s);
            } else if (entityType == EntityType.SALE) {
                Sale sale = entity != null ? (Sale) entity : new Sale();
                sale.setClient(saleClientCombo.getValue());
                sale.setFuel(saleFuelCombo.getValue());
                sale.setStation(saleStationCombo.getValue());
                sale.setSaleDate(saleDatePicker.getValue());
                sale.setQuantity(new java.math.BigDecimal(saleQuantityField.getText()));
                if (entity != null)
                    new SaleDAO().update(sale);
                else
                    new SaleDAO().save(sale);
            }
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка сохранения:\n" + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        stage.close();
    }
}
