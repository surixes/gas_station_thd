package org.project.gas_station.app.gas_station_coursework.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.project.gas_station.app.gas_station_coursework.util.HibernateUtil;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        HibernateUtil.getSessionFactory();

        Parent root = FXMLLoader.load(
                getClass().getResource("/org/project/gas_station/app/gas_station_coursework/app/StartScreen.fxml"));
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        primaryStage.setTitle("Управление АЗС");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.getSessionFactory().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
