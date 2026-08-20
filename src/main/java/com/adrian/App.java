package com.adrian;

import com.adrian.LogicBusiness.Menu;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("PONG");
        stage.setResizable(false);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double width = bounds.getWidth() * 0.7;
        double height = bounds.getHeight() * 0.8;

        Menu menu = new Menu(stage);
        Scene scene = new Scene(menu, width, height);
        scene.setFill(Color.rgb(14, 14, 20));

        stage.setScene(scene);
        menu.applyScale(width / 800.0);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
