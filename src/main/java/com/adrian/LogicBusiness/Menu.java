package com.adrian.LogicBusiness;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Menu extends VBox {

    private Label title;
    private Label label1;
    private TextField textField1;
    private Label label2;
    private TextField textField2;
    private Button play;
    private Button scoreBoard;

    public Menu(Stage stage) {
        setStyle("-fx-background-color: rgb(25, 25, 35);");
        Background stylebackground = new Background(
                new BackgroundFill(Color.rgb(45, 50, 65), new CornerRadii(10), null));

        title = new Label("PING-PONG");
        title.setTextFill(Color.rgb(220, 220, 230));
        title.setBackground(stylebackground);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setPadding(new Insets(10));
        title.setBorder(new Border(
                new BorderStroke(Color.rgb(80, 80, 100), BorderStrokeStyle.SOLID, new CornerRadii(10),
                        new BorderWidths(1))));

        label1 = new Label("Jugador 1:");
        label1.setTextFill(Color.rgb(220, 220, 230));

        textField1 = new TextField();
        textField1.setPromptText("Nombre Jugador 1");
        textField1.setPrefHeight(30);
        textField1.setPrefWidth(200);
        textField1.setFocusTraversable(false);

        label2 = new Label("Jugador 2:");
        label2.setTextFill(Color.rgb(220, 220, 230));

        textField2 = new TextField();
        textField2.setPromptText("Nombre Jugador 2");
        textField2.setPrefHeight(30);
        textField2.setPrefWidth(200);
        textField2.setFocusTraversable(false);

        play = new Button("Empezar");
        play.setTextFill(Color.rgb(220, 220, 230));
        play.setBackground(stylebackground);

        play.setOnAction(event -> {
            String player1 = textField1.getText().trim().isEmpty() ? "Jugador 1" : textField1.getText();
            String player2 = textField2.getText().trim().isEmpty() ? "Jugador 2" : textField2.getText();

            if (player1.equals(player2)) {
                errorPane();
                return;
            }

            Game game = new Game(player1, player2);
            stage.getScene().setRoot(game);
            game.start();
        });

        scoreBoard = new Button("Historial");
        scoreBoard.setTextFill(Color.rgb(220, 220, 230));
        scoreBoard.setBackground(stylebackground);

        scoreBoard.setOnAction(event -> {
            ScoreBoard scoreBoardScene = new ScoreBoard();
            stage.getScene().setRoot(scoreBoardScene);
        });

        setAlignment(Pos.CENTER);
        setSpacing(10);
        setPadding(new Insets(10));
        getChildren().addAll(title, label1, textField1, label2, textField2, play, scoreBoard);
    }

    private void errorPane() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No Válido");
        alert.setHeaderText("No pueden tener el mismo nombre los jugadores");
        alert.setContentText("Ingresa de nuevo los nombres");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            textField1.clear();
            textField2.clear();
            Stage stage = (Stage) getScene().getWindow();
            stage.getScene().setRoot(this);
        }
    }
}
