package com.adrian.LogicBusiness;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Menu extends VBox {

    private static final Color BG = Color.rgb(14, 14, 20);
    private static final Color CARD = Color.rgb(22, 22, 30);
    private static final Color ACCENT = Color.rgb(195, 165, 105);
    private static final Color TEXT = Color.rgb(225, 220, 210);
    private static final Color TEXT_DIM = Color.rgb(110, 108, 100);
    private static final Color INPUT_BG = Color.rgb(28, 28, 36);
    private static final Color INPUT_BORDER = Color.rgb(45, 43, 40);

    private Label title;
    private Label label1;
    private TextField textField1;
    private Label label2;
    private TextField textField2;
    private Button play;
    private Button scoreBoard;

    public Menu(Stage stage) {
        setStyle("-fx-background-color: #0e0e14;");

        Background cardBg = new Background(
                new BackgroundFill(CARD, new CornerRadii(14), null));
        Background btnPrimaryBg = new Background(
                new BackgroundFill(new LinearGradient(
                        0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(175, 145, 85)),
                        new Stop(1, Color.rgb(135, 105, 55))),
                        new CornerRadii(10), null));
        Background btnPrimaryHover = new Background(
                new BackgroundFill(new LinearGradient(
                        0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(205, 175, 105)),
                        new Stop(1, Color.rgb(165, 135, 75))),
                        new CornerRadii(10), null));
        Background btnSecondaryBg = new Background(
                new BackgroundFill(Color.rgb(26, 26, 34), new CornerRadii(10), null));
        Background btnSecondaryHover = new Background(
                new BackgroundFill(Color.rgb(36, 36, 46), new CornerRadii(10), null));

        title = new Label("PONG");
        title.setTextFill(ACCENT);
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.setBackground(cardBg);
        title.setPadding(new Insets(14, 50, 14, 50));
        title.setBorder(new Border(
                new BorderStroke(Color.rgb(50, 48, 42), BorderStrokeStyle.SOLID, new CornerRadii(14),
                        new BorderWidths(1))));
        title.setEffect(new DropShadow(30, Color.rgb(0, 0, 0)));

        label1 = new Label("JUGADOR 1");
        label1.setTextFill(TEXT_DIM);
        label1.setFont(Font.font("System", FontWeight.NORMAL, 11));

        textField1 = new TextField();
        textField1.setPromptText("Nombre");
        textField1.setPrefHeight(38);
        textField1.setPrefWidth(260);
        textField1.setFocusTraversable(false);
        textField1.setFont(Font.font("System", 14));
        textField1.setBackground(new Background(
                new BackgroundFill(INPUT_BG, new CornerRadii(8), null)));
        textField1.setBorder(new Border(
                new BorderStroke(INPUT_BORDER, BorderStrokeStyle.SOLID, new CornerRadii(8),
                        new BorderWidths(1))));

        label2 = new Label("JUGADOR 2");
        label2.setTextFill(TEXT_DIM);
        label2.setFont(Font.font("System", FontWeight.NORMAL, 11));

        textField2 = new TextField();
        textField2.setPromptText("Nombre");
        textField2.setPrefHeight(38);
        textField2.setPrefWidth(260);
        textField2.setFocusTraversable(false);
        textField2.setFont(Font.font("System", 14));
        textField2.setBackground(new Background(
                new BackgroundFill(INPUT_BG, new CornerRadii(8), null)));
        textField2.setBorder(new Border(
                new BorderStroke(INPUT_BORDER, BorderStrokeStyle.SOLID, new CornerRadii(8),
                        new BorderWidths(1))));

        play = new Button("JUGAR");
        play.setTextFill(Color.rgb(18, 16, 10));
        play.setFont(Font.font("System", FontWeight.BOLD, 15));
        play.setBackground(btnPrimaryBg);
        play.setPadding(new Insets(13, 60, 13, 60));
        play.setEffect(new DropShadow(15, Color.rgb(0, 0, 0)));

        play.setOnMouseEntered(e -> {
            play.setBackground(btnPrimaryHover);
            play.setEffect(new DropShadow(20, Color.rgb(150, 120, 60)));
        });
        play.setOnMouseExited(e -> {
            play.setBackground(btnPrimaryBg);
            play.setEffect(new DropShadow(15, Color.rgb(0, 0, 0)));
        });

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

        scoreBoard = new Button("HISTORIAL");
        scoreBoard.setTextFill(TEXT_DIM);
        scoreBoard.setFont(Font.font("System", FontWeight.NORMAL, 13));
        scoreBoard.setBackground(btnSecondaryBg);
        scoreBoard.setPadding(new Insets(11, 45, 11, 45));
        scoreBoard.setBorder(new Border(
                new BorderStroke(Color.rgb(42, 40, 38), BorderStrokeStyle.SOLID, new CornerRadii(10),
                        new BorderWidths(1))));

        scoreBoard.setOnMouseEntered(e -> {
            scoreBoard.setBackground(btnSecondaryHover);
            scoreBoard.setTextFill(TEXT);
        });
        scoreBoard.setOnMouseExited(e -> {
            scoreBoard.setBackground(btnSecondaryBg);
            scoreBoard.setTextFill(TEXT_DIM);
        });

        scoreBoard.setOnAction(event -> {
            ScoreBoard scoreBoardScene = new ScoreBoard();
            stage.getScene().setRoot(scoreBoardScene);
        });

        setAlignment(Pos.CENTER);
        setSpacing(16);
        setPadding(new Insets(30));
        getChildren().addAll(title, label1, textField1, label2, textField2, play, scoreBoard);
    }

    private void errorPane() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No valido");
        alert.setHeaderText("Los jugadores no pueden tener el mismo nombre");
        alert.setContentText("Ingresa de nuevo los nombres");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            textField1.clear();
            textField2.clear();
            Stage stage = (Stage) getScene().getWindow();
            stage.getScene().setRoot(this);
        }
    }

    public void applyScale(double scale) {
        title.setFont(Font.font("System", FontWeight.BOLD, 36 * scale));
        title.setPadding(new Insets(14 * scale, 50 * scale, 14 * scale, 50 * scale));

        double labelFont = 11 * scale;
        label1.setFont(Font.font("System", FontWeight.NORMAL, labelFont));
        label2.setFont(Font.font("System", FontWeight.NORMAL, labelFont));

        textField1.setPrefHeight(38 * scale);
        textField1.setPrefWidth(260 * scale);
        textField1.setFont(Font.font("System", 14 * scale));
        textField2.setPrefHeight(38 * scale);
        textField2.setPrefWidth(260 * scale);
        textField2.setFont(Font.font("System", 14 * scale));

        double buttonFont = 15 * scale;
        play.setFont(Font.font("System", FontWeight.BOLD, buttonFont));
        play.setPadding(new Insets(13 * scale, 60 * scale, 13 * scale, 60 * scale));
        scoreBoard.setFont(Font.font("System", FontWeight.NORMAL, 13 * scale));
        scoreBoard.setPadding(new Insets(11 * scale, 45 * scale, 11 * scale, 45 * scale));

        setSpacing(16 * scale);
        setPadding(new Insets(30 * scale));
    }
}
