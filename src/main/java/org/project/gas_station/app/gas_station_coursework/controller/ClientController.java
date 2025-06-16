package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.project.gas_station.app.gas_station_coursework.dao.ClientDAO;
import org.project.gas_station.app.gas_station_coursework.model.Client;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

public class ClientController {

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, Integer> idColumn;
    @FXML
    private TableColumn<Client, String> nameColumn;
    @FXML
    private TextField nameField;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private Client selectedClient;

    @FXML
    public void initialize() {
        setupTable();
        loadClients();
        setupSelectionListener();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Скрываем кнопки редактирования/удаления до выбора клиента
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList(
                clientDAO.getAll());
        clientTable.setItems(clients);
    }

    private void setupSelectionListener() {
        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedClient = newSelection;
                    updateButton.setDisable(newSelection == null);
                    deleteButton.setDisable(newSelection == null);

                    if (newSelection != null) {
                        nameField.setText(newSelection.getName());
                    }
                });
    }

    @FXML
    private void handleAddClient() {
        String name = nameField.getText().trim();

        if (!name.isEmpty()) {
            Client newClient = new Client();
            newClient.setName(name);

            clientDAO.save(newClient);
            clearForm();
            loadClients();
        } else {
            showAlert("Ошибка", "Поле имени не может быть пустым");
        }
    }

    @FXML
    private void handleUpdateClient() {
        if (selectedClient != null) {
            String name = nameField.getText().trim();

            if (!name.isEmpty()) {
                selectedClient.setName(name);
                clientDAO.update(selectedClient);
                clearForm();
                loadClients();
            } else {
                showAlert("Ошибка", "Поле имени не может быть пустым");
            }
        }
    }

    @FXML
    private void handleDeleteClient() {
        if (selectedClient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить клиента?");
            alert.setContentText(selectedClient.getName());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    clientDAO.delete(selectedClient.getId());
                    clearForm();
                    loadClients();
                }
            });
        }
    }

    private void clearForm() {
        nameField.clear();
        selectedClient = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}