package com.adrian.LogicBusiness;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import java.sql.Connection;

import com.adrian.DataAccess.DatabaseConnection;
import com.adrian.DataAccess.MatchDAO;
import com.adrian.DataAccess.MatchDTO;

public class Game extends Pane {

    private static final Color P1_COLOR = Color.rgb(175, 145, 85);
    private static final Color P2_COLOR = Color.rgb(155, 90, 80);
    private static final Color BALL_COLOR = Color.rgb(215, 205, 185);
    private static final Color LINE_COLOR = Color.rgb(38, 36, 34);
    private static final Color TEXT = Color.rgb(225, 220, 210);

    private double maxBallSpeed;
    private double scale = 1.0;
    private long lastTime;

    private double startgame;
    private double endgame;
    private long timeofgame;

    private final Set<KeyCode> keysPressed = new HashSet<>();

    private double ballVelocityX = 3;
    private double ballVelocityY = 5;
    private double rectangleVelocity = 7.0;

    private AnimationTimer gameLoop;

    private Circle ball;
    private Rectangle leftRectangle;
    private Rectangle rightRectangle;

    private Label countdown;

    private Label labelScoreLeft;
    private int scoreLeft;
    private Label labelScoreRight;
    private int scoreRight;

    private Timeline tempLine;
    private PauseTransition pause;

    private Line line;

    private Random rm;

    private String player1;
    private String player2;

    public Game(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        setStyle("-fx-background-color: #0e0e14;");

        ball = new Circle(10);
        ball.setFill(BALL_COLOR);
        ball.setEffect(new DropShadow(18, Color.rgb(180, 170, 140)));

        leftRectangle = new Rectangle(10, 100);
        leftRectangle.setArcWidth(6);
        leftRectangle.setArcHeight(6);
        leftRectangle.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, P1_COLOR),
                new Stop(1, Color.rgb(130, 105, 55))));
        leftRectangle.setEffect(new DropShadow(12, Color.rgb(0, 0, 0)));

        rightRectangle = new Rectangle(10, 100);
        rightRectangle.setArcWidth(6);
        rightRectangle.setArcHeight(6);
        rightRectangle.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, P2_COLOR),
                new Stop(1, Color.rgb(110, 60, 50))));
        rightRectangle.setEffect(new DropShadow(12, Color.rgb(0, 0, 0)));

        Font labelscores = Font.font("System", FontWeight.BOLD, 15);
        scoreLeft = 0;
        labelScoreLeft = new Label(player1 + "\n" + String.valueOf(scoreLeft));
        labelScoreLeft.setTextFill(P1_COLOR);
        labelScoreLeft.setFont(labelscores);

        scoreRight = 0;
        labelScoreRight = new Label(player2 + "\n" + String.valueOf(scoreRight));
        labelScoreRight.setTextFill(P2_COLOR);
        labelScoreRight.setFont(labelscores);

        line = new Line();
        line.setStartX(0);
        line.endXProperty().bind(widthProperty());
        line.setStartY(100);
        line.setEndY(100);
        line.setStroke(LINE_COLOR);
        line.setStrokeWidth(1);

        countdown = new Label();
        countdown.setFont(Font.font("System", FontWeight.BOLD, 30));
        countdown.setTextFill(TEXT);
        countdown.setEffect(new DropShadow(20, Color.rgb(0, 0, 0)));

        getChildren().addAll(labelScoreLeft, labelScoreRight,
                line,
                countdown);

        setOnKeyPressed(event -> {
            keysPressed.add(event.getCode());

            if (event.getCode() == KeyCode.ESCAPE) {
                exitGame();
            }
        });

        setOnKeyReleased(event -> {
            keysPressed.remove(event.getCode());
        });
    }

    public void start() {

        applyCss();
        layout();

        requestFocus();

        rm = new Random();

        layoutGame();

        labelScoreLeft.setLayoutX((getWidth() / 4) - labelScoreLeft.prefWidth(-1));
        labelScoreLeft.setLayoutY((line.getStartY() - labelScoreLeft.getHeight()) / 2);
        labelScoreRight.setLayoutX((getWidth() * 3 / 4) - labelScoreRight.prefWidth(-1));
        labelScoreRight.setLayoutY((line.getStartY() - labelScoreRight.getHeight()) / 2);

        countdown.setLayoutY(line.getStartY() + ((getHeight() - line.getStartY() - countdown.getHeight()) / 2));

        if (rm.nextBoolean()) {
            ballVelocityX *= -1;
        }

        if (rm.nextBoolean()) {
            ballVelocityY *= -1;
        }

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                labelScoreLeft.setText(player1 + "\n" + String.valueOf(scoreLeft));
                labelScoreRight.setText(player2 + "\n" + String.valueOf(scoreRight));

                if (ball.getCenterY() + ball.getRadius() >= getHeight()
                        || ball.getBoundsInParent().intersects(line.getBoundsInParent())) {

                    ballVelocityY = -ballVelocityY;
                }

                if (ball.getCenterX() + ball.getRadius() >= getWidth()) {
                    gameLoop.stop();
                    getChildren().removeAll(ball, leftRectangle, rightRectangle);
                    getChildren().add(countdown);
                    scoreLeft++;

                    resetBall();
                    resetRectangle();
                    tempLine.playFromStart();

                    return;
                }

                if (ball.getCenterX() - ball.getRadius() <= 0) {
                    gameLoop.stop();
                    getChildren().removeAll(ball, leftRectangle, rightRectangle);
                    getChildren().add(countdown);
                    scoreRight++;

                    resetBall();
                    resetRectangle();
                    tempLine.playFromStart();

                    return;
                }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                ballVelocityX += 35 * deltaTime * Math.signum(ballVelocityX);
                ballVelocityY += 35 * deltaTime * Math.signum(ballVelocityY);
                rectangleVelocity += 20 * deltaTime;

                ball.setCenterX(
                        ball.getCenterX() + ballVelocityX * deltaTime);

                ball.setCenterY(
                        ball.getCenterY() + ballVelocityY * deltaTime);

                if (keysPressed.contains(KeyCode.S)
                        && leftRectangle.getY() < getHeight() - leftRectangle.getHeight()) {

                    leftRectangle.setY(
                            leftRectangle.getY() + rectangleVelocity * deltaTime);
                }

                double limiteSuperior = line.getStartY() + line.getStrokeWidth() / 2;

                if (keysPressed.contains(KeyCode.W)
                        && leftRectangle.getY() - rectangleVelocity * deltaTime >= limiteSuperior) {

                    leftRectangle.setY(leftRectangle.getY() - rectangleVelocity * deltaTime);
                }

                if (keysPressed.contains(KeyCode.UP)
                        && rightRectangle.getY() - rectangleVelocity * deltaTime >= limiteSuperior) {

                    rightRectangle.setY(rightRectangle.getY() - rectangleVelocity * deltaTime);
                }

                if (keysPressed.contains(KeyCode.DOWN)
                        && rightRectangle.getY() < getHeight() - rightRectangle.getHeight()) {

                    rightRectangle.setY(
                            rightRectangle.getY() + rectangleVelocity * deltaTime);
                }
                if (ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent())
                        && ballVelocityX < 0) {

                    ball.setCenterX(
                            leftRectangle.getX()
                                    + leftRectangle.getWidth()
                                    + ball.getRadius());

                    ballVelocityX = -ballVelocityX;
                    ballVelocityX *= 1.03;
                    rectangleVelocity *= 1.03;

                }

                if (ball.getBoundsInParent().intersects(rightRectangle.getBoundsInParent())
                        && ballVelocityX > 0) {

                    ball.setCenterX(
                            rightRectangle.getX()
                                    - ball.getRadius());

                    ballVelocityX = -ballVelocityX;
                    ballVelocityX *= 1.03;
                    rectangleVelocity *= 1.03;

                }

                if (Math.abs(ballVelocityX) > maxBallSpeed) {
                    ballVelocityX = maxBallSpeed * Math.signum(ballVelocityX);
                }
                if (Math.abs(ballVelocityY) > maxBallSpeed) {
                    ballVelocityY = maxBallSpeed * Math.signum(ballVelocityY);
                }
                if (rectangleVelocity > maxBallSpeed) {
                    rectangleVelocity = maxBallSpeed;
                }
            }
        };

        pause = new PauseTransition(Duration.seconds(1));

        pause.setOnFinished(event -> {
            getChildren().remove(countdown);
            getChildren().addAll(ball,
                    leftRectangle,
                    rightRectangle);
            startgame = System.nanoTime();
            lastTime = System.nanoTime();
            gameLoop.start();
        });

        tempLine = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> {
                    countdown.setText("ESC para salir");
                    countdown.setTextFill(Color.rgb(100, 98, 90));
                    centerLabel();
                }),
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            countdown.setText("3");
                            countdown.setTextFill(ACCENT);
                            centerLabel();
                        }),
                new KeyFrame(
                        Duration.seconds(2),
                        event -> {
                            countdown.setText("2");
                            countdown.setTextFill(ACCENT);
                            centerLabel();
                        }),
                new KeyFrame(
                        Duration.seconds(3),
                        event -> {
                            countdown.setText("1");
                            countdown.setTextFill(ACCENT);
                            centerLabel();
                        }),
                new KeyFrame(
                        Duration.seconds(4),
                        event -> {
                            countdown.setText("GO");
                            countdown.setTextFill(TEXT);
                            countdown.setEffect(new DropShadow(25, Color.rgb(170, 140, 80)));
                            centerLabel();
                            pause.play();
                        }));
        tempLine.play();

    }

    private static final Color ACCENT = Color.rgb(195, 165, 105);

    private void centerLabel() {
        countdown.applyCss();
        countdown.layout();

        double width = countdown.prefWidth(-1);

        countdown.setLayoutX((getWidth() - width) / 2);
    }

    private void layoutGame() {
        scale = getWidth() / 800.0;
        maxBallSpeed = 900 * scale;
        rectangleVelocity = 480.0 * scale;

        ball.setRadius(getHeight() / 60.0);

        leftRectangle.setWidth(getWidth() / 80.0);
        leftRectangle.setHeight(getHeight() / 6.0);

        rightRectangle.setWidth(leftRectangle.getWidth());
        rightRectangle.setHeight(leftRectangle.getHeight());

        line.setStartY(getHeight() / 6.0);
        line.setEndY(line.getStartY());

        double fontScore = 15 * scale;
        labelScoreLeft.setFont(Font.font("System", FontWeight.BOLD, fontScore));
        labelScoreRight.setFont(Font.font("System", FontWeight.BOLD, fontScore));
        countdown.setFont(Font.font("System", FontWeight.BOLD, 30 * scale));

        double top = line.getStartY();
        double centerY = top + (getHeight() - top) / 2;
        double margin = getWidth() * 30.0 / 800;

        ball.setCenterX(getWidth() / 2);
        ball.setCenterY(centerY);

        leftRectangle.setX(margin);
        leftRectangle.setY(centerY - leftRectangle.getHeight() / 2);

        rightRectangle.setX(getWidth() - leftRectangle.getWidth() - margin);
        rightRectangle.setY(centerY - rightRectangle.getHeight() / 2);

        ballVelocityX = 240 * scale;
        ballVelocityY = 360 * scale;
    }

    private void resetBall() {
        double top = line.getStartY();

        ball.setCenterX(getWidth() / 2);
        ball.setCenterY(top + (getHeight() - top) / 2);

        ballVelocityX = 240 * scale;
        ballVelocityY = 360 * scale;

        if (rm.nextBoolean()) {
            ballVelocityX *= -1;
        }

        if (rm.nextBoolean()) {
            ballVelocityY *= -1;
        }
    }

    private void resetRectangle() {

        rectangleVelocity = 420.0 * scale;

        double top = line.getStartY();
        double centerY = top + (getHeight() - top) / 2;
        double margin = getWidth() * 30.0 / 800;

        leftRectangle.setX(margin);
        leftRectangle.setY(centerY - leftRectangle.getHeight() / 2);

        rightRectangle.setX(getWidth() - leftRectangle.getWidth() - margin);
        rightRectangle.setY(centerY - rightRectangle.getHeight() / 2);
    }

    private void exitGame() {

        gameLoop.stop();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("Quieres salir del juego?");
        alert.setContentText("La partida actual terminara.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            endgame = System.nanoTime();
            timeofgame = (long) ((endgame - startgame) / 1_000_000_000.0);

            MatchDTO dto = new MatchDTO(player1, player2, scoreLeft, scoreRight, timeofgame);

            try {
                Connection conn = DatabaseConnection.getConnection();
                MatchDAO dao = new MatchDAO(conn);
                dao.save(dto);
                DatabaseConnection.close(conn);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error de base de datos");
                errorAlert.setHeaderText("No se pudo guardar la partida");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }

            Stage stage = (Stage) getScene().getWindow();

            Menu menu = new Menu(stage);

            stage.getScene().setRoot(menu);
            menu.applyScale(stage.getScene().getWidth() / 800.0);

        } else {
            lastTime = System.nanoTime();
            gameLoop.start();
        }
    }
}
