package com.adrian.LogicBusiness;

import java.util.List;

import com.adrian.DataAccess.DatabaseConnection;
import com.adrian.DataAccess.MatchDAO;
import com.adrian.DataAccess.MatchDTO;

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

import java.util.Optional;

public class ScoreBoard extends VBox {

    private MatchDAO dao;
    private Label title;
    private TableView<MatchDTO> table;
    private Button back;
    private Button deleteHistory;

    public ScoreBoard() {
        setStyle("-fx-background-color: rgb(25, 25, 35);");

        Background cardBg = new Background(
                new BackgroundFill(Color.rgb(35, 40, 55), new CornerRadii(10), null));
        Background btnBg = new Background(
                new BackgroundFill(Color.rgb(45, 50, 65), new CornerRadii(10), null));
        Background btnHoverBg = new Background(
                new BackgroundFill(Color.rgb(60, 65, 80), new CornerRadii(10), null));

        title = new Label("MARCADOR");
        title.setTextFill(Color.rgb(100, 200, 255));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setBackground(cardBg);
        title.setPadding(new Insets(10, 30, 10, 30));
        title.setBorder(new Border(
                new BorderStroke(Color.rgb(100, 200, 255), BorderStrokeStyle.SOLID, new CornerRadii(10),
                        new BorderWidths(1))));

        table = new TableView<>();
        table.setBackground(new Background(
                new BackgroundFill(Color.rgb(30, 35, 50), new CornerRadii(8), null)));

        Label placeholder = new Label("No hay partidas registradas");
        placeholder.setTextFill(Color.rgb(120, 120, 140));
        table.setPlaceholder(placeholder);

        String headerStyle = "-fx-background-color: rgb(137, 153, 199); -fx-text-fill: rgb(220, 220, 230); -fx-font-weight: bold;";
        String cellStyle = "-fx-text-fill: rgb(200, 200, 210); -fx-background-color: rgb(30, 35, 50);";

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

        TableColumn<MatchDTO, Long> durationColumn = new TableColumn<>("Duración (s)");
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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        loadData();

        back = new Button("Regresar");
        back.setTextFill(Color.rgb(220, 220, 230));
        back.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        back.setBackground(btnBg);
        back.setBorder(new Border(
                new BorderStroke(Color.rgb(80, 80, 100), BorderStrokeStyle.SOLID, new CornerRadii(10),
                        new BorderWidths(1))));
        back.setPadding(new Insets(10, 40, 10, 40));

        back.setOnMouseEntered(e -> back.setBackground(btnHoverBg));
        back.setOnMouseExited(e -> back.setBackground(btnBg));

        back.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            Menu menu = new Menu(stage);
            stage.getScene().setRoot(menu);
            menu.applyScale(stage.getScene().getWidth() / 800.0);
        });

        deleteHistory = new Button("Borrar Historial");
        deleteHistory.setTextFill(Color.rgb(220, 220, 230));
        deleteHistory.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deleteHistory.setBackground(btnBg);
        deleteHistory.setBorder(new Border(
                new BorderStroke(Color.rgb(150, 60, 60), BorderStrokeStyle.SOLID, new CornerRadii(10),
                        new BorderWidths(1))));
        deleteHistory.setPadding(new Insets(10, 30, 10, 30));

        deleteHistory.setOnMouseEntered(e -> deleteHistory.setBackground(
                new Background(new BackgroundFill(Color.rgb(150, 60, 60), new CornerRadii(10), null))));
        deleteHistory.setOnMouseExited(e -> deleteHistory.setBackground(btnBg));

        deleteHistory.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Borrar historial");
            confirm.setHeaderText("¿Estás seguro?");
            confirm.setContentText("Se eliminarán todas las partidas registradas.");

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    MatchDAO dao = new MatchDAO(DatabaseConnection.getConnection());
                    dao.delete();
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

        setAlignment(Pos.TOP_CENTER);
        setSpacing(20);
        setPadding(new Insets(20));
        getChildren().addAll(title, table, back, deleteHistory);
    }

    private void loadData() {
        try {
            dao = new MatchDAO(DatabaseConnection.getConnection());
            List<MatchDTO> dtos = dao.findAll();
            table.getItems().addAll(dtos);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No se pudo cargar el historial");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
}
