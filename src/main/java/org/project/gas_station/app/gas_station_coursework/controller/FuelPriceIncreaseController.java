package org.project.gas_station.app.gas_station_coursework.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.hibernate.Session;
import org.project.gas_station.app.gas_station_coursework.model.Firm;
import org.project.gas_station.app.gas_station_coursework.model.PriceDynamics;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Transaction;

public class FuelPriceIncreaseController {

    @FXML
    private ComboBox<Firm> firmComboBox;
    @FXML
    private TextField priceIncreaseTextField;
    @FXML
    private Button applyIncreaseButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView<PriceRow> beforePriceTable;
    @FXML
    private TableColumn<PriceRow, String> fuelTypeColumn;
    @FXML
    private TableColumn<PriceRow, String> oldPriceColumn;
    @FXML
    private TableView<PriceRow> afterPriceTable;
    @FXML
    private TableColumn<PriceRow, String> fuelTypeColumnAfter;
    @FXML
    private TableColumn<PriceRow, String> newPriceColumn;

    @FXML
    public void initialize() {

        fuelTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFuelType()));
        oldPriceColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getOldPrice())));

        fuelTypeColumnAfter
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFuelType()));
        newPriceColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getNewPrice())));


        loadFirms();
    }

    private void loadFirms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Firm> firms = session.createQuery("FROM Firm", Firm.class).getResultList();
            firmComboBox.setItems(FXCollections.observableArrayList(firms));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка загрузки данных", "Не удалось загрузить список фирм.");
        }
    }

    @FXML
    private void handleApplyPriceIncrease() {
        Firm selectedFirm = firmComboBox.getValue();
        String priceIncreaseText = priceIncreaseTextField.getText();

        if (selectedFirm == null) {
            showAlert(Alert.AlertType.WARNING, "Ошибка выбора фирмы", "Пожалуйста, выберите фирму.");
            return;
        }

        if (priceIncreaseText == null || priceIncreaseText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ошибка ввода", "Пожалуйста, укажите процент повышения цен.");
            return;
        }

        double priceIncreasePercentage;
        try {
            priceIncreasePercentage = Double.parseDouble(priceIncreaseText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Ошибка ввода", "Введите корректный процент.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hqlBefore = "FROM PriceDynamics pd WHERE pd.firm.id = :firmId AND pd.startDate = " +
                    "(SELECT MAX(pd2.startDate) FROM PriceDynamics pd2 WHERE pd2.fuel.id = pd.fuel.id AND pd2.firm.id = :firmId)";
            List<PriceDynamics> priceListBefore = session.createQuery(hqlBefore, PriceDynamics.class)
                    .setParameter("firmId", selectedFirm.getId())
                    .getResultList();

            ObservableList<PriceRow> beforeData = FXCollections.observableArrayList();
            for (PriceDynamics pd : priceListBefore) {
                beforeData.add(new PriceRow(pd.getFuel().getType(), pd.getPrice().doubleValue()));
            }
            beforePriceTable.setItems(beforeData);

            Transaction transaction = session.beginTransaction();
            String hqlUpdate = "UPDATE PriceDynamics pd SET pd.price = pd.price * (1 + :increasePercentage / 100.0) WHERE pd.firm.id = :firmId";
            int updatedCount = session.createQuery(hqlUpdate)
                    .setParameter("increasePercentage", priceIncreasePercentage)
                    .setParameter("firmId", selectedFirm.getId())
                    .executeUpdate();
            transaction.commit(); 

            
            session.clear(); 

            
            List<PriceDynamics> priceListAfter = session.createQuery(hqlBefore, PriceDynamics.class)
                    .setParameter("firmId", selectedFirm.getId())
                    .getResultList();

            ObservableList<PriceRow> afterData = FXCollections.observableArrayList();
            for (PriceDynamics pd : priceListAfter) {
                afterData.add(new PriceRow(pd.getFuel().getType(), pd.getPrice().doubleValue()));
            }
            afterPriceTable.setItems(afterData);

            showAlert(Alert.AlertType.INFORMATION, "Успех",
                    "Цены успешно обновлены.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось обновить цены: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Parent prevView = FXMLLoader.load(
                getClass().getResource("/org/project/gas_station/app/gas_station_coursework/app/PreparedQueries.fxml"));
        VBox contentPane = (VBox) backButton.getScene().lookup("#contentPane");
        if (contentPane != null) {
            contentPane.getChildren().setAll(prevView);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class PriceRow {
        private final String fuelType;
        private final double oldPrice;
        private final double newPrice;

        public PriceRow(String fuelType, double oldPrice) {
            this.fuelType = fuelType;
            this.oldPrice = oldPrice;
            this.newPrice = oldPrice; 
        }

        public String getFuelType() {
            return fuelType;
        }

        public double getOldPrice() {
            return oldPrice;
        }

        public double getNewPrice() {
            return newPrice;
        }
    }
}
