package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

import org.hibernate.Session;
import org.project.gas_station.app.gas_station_coursework.model.Firm;
import org.project.gas_station.app.gas_station_coursework.model.GasStation;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

public class AddGasStationController {

    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<Firm> firmComboBox;
    @FXML
    private Label confirmationMessage;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Загрузка списка фирм в ComboBox
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            firmComboBox.getItems()
                    .setAll(session.createQuery("FROM Firm WHERE isOwner = true", Firm.class).getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка загрузки данных", "Не удалось загрузить список фирм.");
        }
    }

    @FXML
    private void handleAddGasStation() {
        String address = addressField.getText();
        Firm selectedFirm = firmComboBox.getValue();

        if (address.isEmpty() || selectedFirm == null) {
            showAlert(Alert.AlertType.WARNING, "Ошибка ввода", "Пожалуйста, заполните все поля.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            GasStation newGasStation = new GasStation();
            newGasStation.setAddress(address);
            newGasStation.setFirm(selectedFirm);

            // Добавьте логирование перед сохранением
            System.out.println("Before persist: " + newGasStation);
            System.out.println("ID before persist: " + newGasStation.getId());

            session.persist(newGasStation);
            session.getTransaction().commit();

            // Добавьте логирование после сохранения
            System.out.println("After persist: " + newGasStation);
            System.out.println("ID after persist: " + newGasStation.getId());

            confirmationMessage.setText("АЗС успешно добавлена!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось добавить АЗС: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() throws IOException {
        Parent prevView = FXMLLoader.load(getClass().getResource(
                "/org/project/gas_station/app/gas_station_coursework/app/PreparedQueries.fxml"));
        // Получаем главный контейнер контента и заменяем текущий вид на предыдущий
        VBox contentPane = (VBox) backButton.getScene().lookup("#contentPane");
        if (contentPane != null) {
            contentPane.getChildren().setAll(prevView);
        }
    }

}
