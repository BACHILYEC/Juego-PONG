package com.adrian;

import com.adrian.LogicBusiness.Menu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Pong");

        Scene scene = new Scene(new Menu(stage), 800, 600);
        scene.setFill(Color.rgb(25, 25, 35));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
