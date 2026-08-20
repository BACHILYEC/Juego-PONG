package com.adrian.LogicBusiness;

import java.util.List;

import com.adrian.DataAccess.DatabaseConnection;
import com.adrian.DataAccess.MatchDAO;
import com.adrian.DataAccess.MatchDTO;

import java.sql.Connection;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

import java.util.Optional;

public class ScoreBoard extends VBox {

    private static final Color CARD = Color.rgb(22, 22, 30);
    private static final Color ACCENT = Color.rgb(195, 165, 105);
    private static final Color TEXT = Color.rgb(225, 220, 210);
    private static final Color TEXT_DIM = Color.rgb(110, 108, 100);

    private MatchDAO dao;
    private Label title;
    private TableView<MatchDTO> table;
    private Button back;
    private Button deleteHistory;

    public ScoreBoard() {
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
        Background deleteBg = new Background(
                new BackgroundFill(Color.rgb(34, 24, 22), new CornerRadii(10), null));
        Background deleteHoverBg = new Background(
                new BackgroundFill(Color.rgb(50, 32, 30), new CornerRadii(10), null));

        title = new Label("HISTORIAL");
        title.setTextFill(ACCENT);
        title.setFont(Font.font("System", FontWeight.BOLD, 26));
        title.setBackground(cardBg);
        title.setPadding(new Insets(12, 40, 12, 40));
        title.setBorder(new Border(
                new BorderStroke(Color.rgb(50, 48, 42), BorderStrokeStyle.SOLID, new CornerRadii(14),
                        new BorderWidths(1))));
        title.setEffect(new DropShadow(30, Color.rgb(0, 0, 0)));

        table = new TableView<>();
        table.setBackground(new Background(
                new BackgroundFill(CARD, new CornerRadii(10), null)));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setMaxWidth(Double.MAX_VALUE);

        Label placeholder = new Label("No hay partidas registradas");
        placeholder.setTextFill(TEXT_DIM);
        placeholder.setFont(Font.font("System", 13));
        table.setPlaceholder(placeholder);

        String headerStyle = "-fx-background-color: #161618; -fx-text-fill: #c3a569; -fx-font-weight: bold; -fx-font-family: System;";
        String cellStyle = "-fx-text-fill: #e1dcc8; -fx-background-color: #161618; -fx-font-family: System;";

        TableColumn<MatchDTO, String> player1Column = new TableColumn<>("Jugador 1");
        player1Column.setCellValueFactory(new PropertyValueFactory<>("player1"));
        player1Column.setStyle(headerStyle);
        player1Column.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(cellStyle);
            }
        });

        TableColumn<MatchDTO, String> player2Column = new TableColumn<>("Jugador 2");
        player2Column.setCellValueFactory(new PropertyValueFactory<>("player2"));
        player2Column.setStyle(headerStyle);
        player2Column.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(cellStyle);
            }
        });

        TableColumn<MatchDTO, String> scoreColumn = new TableColumn<>("Puntaje");
        scoreColumn.setCellValueFactory(cellData -> {
            MatchDTO match = cellData.getValue();
            return new SimpleStringProperty(
                    match.getPlayer1Score() + " - " + match.getPlayer2Score());
        });
        scoreColumn.setStyle(headerStyle + "-fx-alignment: CENTER;");
        scoreColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(cellStyle + "-fx-alignment: CENTER;");
            }
        });

        TableColumn<MatchDTO, Long> durationColumn = new TableColumn<>("Duracion (s)");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationSeconds"));
        durationColumn.setStyle(headerStyle + "-fx-alignment: CENTER;");
        durationColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(item));
                setStyle(cellStyle + "-fx-alignment: CENTER;");
            }
        });

        TableColumn<MatchDTO, String> dateColumn = new TableColumn<>("Fecha");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setStyle(headerStyle);
        dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle(cellStyle);
            }
        });

        table.getColumns().add(player1Column);
        table.getColumns().add(player2Column);
        table.getColumns().add(scoreColumn);
        table.getColumns().add(durationColumn);
        table.getColumns().add(dateColumn);

        player1Column.setPrefWidth(150);
        player2Column.setPrefWidth(150);
        scoreColumn.setPrefWidth(120);
        durationColumn.setPrefWidth(100);
        dateColumn.setPrefWidth(170);

        table.setPrefHeight(400);

        loadData();

        back = new Button("REGRESAR");
        back.setTextFill(TEXT);
        back.setFont(Font.font("System", FontWeight.NORMAL, 15));
        back.setBackground(btnPrimaryBg);
        back.setPadding(new Insets(14, 60, 14, 60));
        back.setEffect(new DropShadow(12, Color.rgb(0, 0, 0)));

        back.setOnMouseEntered(e -> {
            back.setBackground(btnPrimaryHover);
            back.setEffect(new DropShadow(18, Color.rgb(150, 120, 60)));
        });
        back.setOnMouseExited(e -> {
            back.setBackground(btnPrimaryBg);
            back.setEffect(new DropShadow(12, Color.rgb(0, 0, 0)));
        });

        back.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            Menu menu = new Menu(stage);
            stage.getScene().setRoot(menu);
            menu.applyScale(stage.getScene().getWidth() / 800.0);
        });

        deleteHistory = new Button("BORRAR HISTORIAL");
        deleteHistory.setTextFill(Color.rgb(170, 110, 100));
        deleteHistory.setFont(Font.font("System", FontWeight.NORMAL, 15));
        deleteHistory.setBackground(deleteBg);
        deleteHistory.setPadding(new Insets(14, 50, 14, 50));
        deleteHistory.setBorder(new Border(
                new BorderStroke(Color.rgb(60, 40, 38), BorderStrokeStyle.SOLID, new CornerRadii(10),
                        new BorderWidths(1))));

        deleteHistory.setOnMouseEntered(e -> {
            deleteHistory.setBackground(deleteHoverBg);
            deleteHistory.setTextFill(Color.rgb(200, 130, 120));
        });
        deleteHistory.setOnMouseExited(e -> {
            deleteHistory.setBackground(deleteBg);
            deleteHistory.setTextFill(Color.rgb(170, 110, 100));
        });

        deleteHistory.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Borrar historial");
            confirm.setHeaderText("Estas seguro?");
            confirm.setContentText("Se eliminaran todas las partidas registradas.");

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    MatchDAO dao = new MatchDAO(conn);
                    dao.delete();
                    DatabaseConnection.close(conn);
                    table.getItems().clear();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("No se pudo borrar el historial");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });

        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(30));
        getChildren().addAll(title, table, back, deleteHistory);
    }

    private void loadData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            dao = new MatchDAO(conn);
            List<MatchDTO> dtos = dao.findAll();
            table.getItems().addAll(dtos);
            DatabaseConnection.close(conn);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No se pudo cargar el historial");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
}
