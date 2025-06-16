package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.hibernate.Session;
import org.project.gas_station.app.gas_station_coursework.model.Fuel;
import org.project.gas_station.app.gas_station_coursework.model.GasStation;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

public class GasStationsWithoutFuelController {

    @FXML
    private ComboBox<Fuel> fuelTypeCombo;
    @FXML
    private Button showGasStationsButton;
    @FXML
    private TableView<String> gasStationsTable; // Теперь просто строки с адресами
    @FXML
    private TableColumn<String, String> stationAddressColumn; // Упрощенная колонка
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Загружаем доступные типы топлива в ComboBox
        loadFuelTypes();

        // Устанавливаем фабрику значений для таблицы
        stationAddressColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
    }

    private void loadFuelTypes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Fuel> fuelTypes = session.createQuery("FROM Fuel", Fuel.class).getResultList();
            fuelTypeCombo.setItems(FXCollections.observableArrayList(fuelTypes));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка загрузки данных",
                    "Не удалось загрузить список типов топлива: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowGasStations() {
        Fuel selectedFuel = fuelTypeCombo.getValue();
        if (selectedFuel == null) {
            showAlert(Alert.AlertType.WARNING, "Ошибка выбора топлива", "Пожалуйста, выберите тип топлива.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL запрос
            String hql = "SELECT gs.address FROM GasStation gs " +
                    "WHERE gs.id NOT IN (" +
                    "   SELECT DISTINCT s.station.id FROM Sale s " +
                    "   WHERE s.fuel.id = :fuelId" +
                    ") " +
                    "ORDER BY gs.address";

            List<String> addresses = session.createQuery(hql, String.class)
                    .setParameter("fuelId", selectedFuel.getId())
                    .getResultList();

            // Обновляем таблицу
            gasStationsTable.setItems(FXCollections.observableArrayList(addresses));

            if (addresses.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Результат",
                        "Не найдено АЗС без топлива: " + selectedFuel.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка",
                    "Не удалось загрузить данные о АЗС: " + e.getMessage());
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
        VBox contentPane = (VBox) backButton.getScene().lookup("#contentPane");
        if (contentPane != null) {
            contentPane.getChildren().setAll(prevView);
        }
    }
}