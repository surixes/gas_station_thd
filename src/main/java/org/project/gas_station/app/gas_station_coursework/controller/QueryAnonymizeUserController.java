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
import org.project.gas_station.app.gas_station_coursework.model.Client;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

public class QueryAnonymizeUserController {
    @FXML
    private ComboBox<Client> userCombo;
    @FXML
    private TableView<ClientRow> usersTable;
    @FXML
    private TableColumn<ClientRow, String> userIdColumn;
    @FXML
    private TableColumn<ClientRow, String> userNameColumn;
    @FXML
    private TableColumn<ClientRow, String> userEmailColumn;
    @FXML
    private Button anonymizeButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        loadUsers();
        userIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        userEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
    }

    private void loadUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Client> clients = session.createQuery("FROM Client", Client.class).getResultList();
            userCombo.setItems(FXCollections.observableArrayList(clients));
            ObservableList<ClientRow> tableData = FXCollections.observableArrayList();
            for (Client c : clients) {
                tableData.add(new ClientRow(
                        String.valueOf(c.getId()),
                        c.getName(),
                        c.getPhone() // Используем phone как email для примера
                ));
            }
            usersTable.setItems(tableData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnonymize() {
        Client selected = userCombo.getValue();
        if (selected == null) {
            showAlert("Выберите пользователя для анонимизации.");
            return;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Client client = session.get(Client.class, selected.getId());
            if (client != null) {
                client.setName("Аноним");
                client.setPhone("");
                client.setAddress("");
                session.update(client);
                session.getTransaction().commit();
                showAlert("Пользователь анонимизирован.");
                loadUsers();
            } else {
                showAlert("Пользователь не найден.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка анонимизации пользователя.");
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Вспомогательный класс для строк таблицы
    public static class ClientRow {
        private final String id;
        private final String name;
        private final String email;

        public ClientRow(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}