package com.adrian.LogicBusiness;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

import com.adrian.DataAccess.DatabaseConnection;
import com.adrian.DataAccess.MatchDAO;
import com.adrian.DataAccess.MatchDTO;

public class Game extends Pane {

    private static final double MAX_BALL_SPEED = 15;

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
        setStyle("-fx-background-color: rgb(25, 25, 35);");

        ball = new Circle(400, 300, 10);
        ball.setFill(Color.rgb(100, 200, 255));

        leftRectangle = new Rectangle(10, 100);
        leftRectangle.setX(30);
        leftRectangle.setY(300);
        leftRectangle.setFill(Color.rgb(140, 140, 160));

        rightRectangle = new Rectangle(10, 100);
        rightRectangle.setX(750);
        rightRectangle.setY(300);
        rightRectangle.setFill(Color.rgb(140, 140, 160));

        Font labelscores = Font.font("Arial", FontWeight.BOLD, 15);
        scoreLeft = 0;
        labelScoreLeft = new Label(player1 + ": \n" + String.valueOf(scoreLeft));
        labelScoreLeft.setTextFill(Color.rgb(220, 220, 230));
        labelScoreLeft.setFont(labelscores);

        scoreRight = 0;
        labelScoreRight = new Label(player2 + ": \n" + String.valueOf(scoreRight));
        labelScoreRight.setTextFill(Color.rgb(220, 220, 230));
        labelScoreRight.setFont(labelscores);

        line = new Line();
        line.setStartX(0);
        line.endXProperty().bind(widthProperty());
        line.setStartY(100);
        line.setEndY(100);
        line.setStroke(Color.rgb(80, 80, 100));
        line.setStrokeWidth(3);

        countdown = new Label();
        countdown.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        countdown.setTextFill(Color.rgb(220, 220, 230));

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

        ball.setCenterX(getWidth() / 2);
        ball.setCenterY(getHeight() / 2);

        ballVelocityX = 4;
        ballVelocityY = 6;

        labelScoreLeft.setLayoutX((getWidth() / 4) - labelScoreLeft.prefWidth(-1));
        labelScoreLeft.setLayoutY((line.getStartY() - labelScoreLeft.getHeight()) / 2);
        labelScoreRight.setLayoutX((getWidth() * 3 / 4) - labelScoreRight.prefWidth(-1));
        labelScoreRight.setLayoutY((line.getStartY() - labelScoreRight.getHeight()) / 2);

        countdown.setLayoutY(100 + ((getHeight() - 100 - countdown.getHeight()) / 2));

        if (rm.nextBoolean()) {
            ballVelocityX *= -1;
        }

        if (rm.nextBoolean()) {
            ballVelocityY *= -1;
        }

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                labelScoreLeft.setText(player1 + ": \n" + String.valueOf(scoreLeft));
                labelScoreRight.setText(player2 + ": \n" + String.valueOf(scoreRight));

                if (ball.getCenterY() + ball.getRadius() >= getHeight()
                        || ball.getBoundsInParent().intersects(line.getBoundsInParent())) {

                    ballVelocityY = -ballVelocityY;
                    ballVelocityY *= 1.03;
                    rectangleVelocity *= 1.03;
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

                ball.setCenterX(
                        ball.getCenterX() + ballVelocityX);

                ball.setCenterY(
                        ball.getCenterY() + ballVelocityY);

                if (keysPressed.contains(KeyCode.S)
                        && leftRectangle.getY() < getHeight() - leftRectangle.getHeight()) {

                    leftRectangle.setY(
                            leftRectangle.getY() + rectangleVelocity);
                }

                double limiteSuperior = line.getStartY() + line.getStrokeWidth() / 2;

                if (keysPressed.contains(KeyCode.W)
                        && leftRectangle.getY() - rectangleVelocity >= limiteSuperior) {

                    leftRectangle.setY(leftRectangle.getY() - rectangleVelocity);
                }

                if (keysPressed.contains(KeyCode.UP)
                        && rightRectangle.getY() - rectangleVelocity >= limiteSuperior) {

                    rightRectangle.setY(rightRectangle.getY() - rectangleVelocity);
                }

                if (keysPressed.contains(KeyCode.DOWN)
                        && rightRectangle.getY() < getHeight() - rightRectangle.getHeight()) {

                    rightRectangle.setY(
                            rightRectangle.getY() + rectangleVelocity);
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

                if (Math.abs(ballVelocityX) > MAX_BALL_SPEED) {
                    ballVelocityX = MAX_BALL_SPEED * Math.signum(ballVelocityX);
                }
                if (Math.abs(ballVelocityY) > MAX_BALL_SPEED) {
                    ballVelocityY = MAX_BALL_SPEED * Math.signum(ballVelocityY);
                }
                if (rectangleVelocity > MAX_BALL_SPEED) {
                    rectangleVelocity = MAX_BALL_SPEED;
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
            gameLoop.start();
        });

        tempLine = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> {
                    countdown.setText("Presiona ESC para salir");
                    countdown.setTextFill(Color.rgb(207, 188, 66));
                    centerLabel();
                }),
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            countdown.setText("3!");
                            countdown.setTextFill(Color.rgb(255, 120, 120));
                            centerLabel();
                        }),
                new KeyFrame(
                        Duration.seconds(2),
                        event -> {
                            countdown.setText("2!");
                            countdown.setTextFill(Color.rgb(255, 210, 90));
                            centerLabel();
                        }),
                new KeyFrame(
                        Duration.seconds(3),
                        event -> {
                            countdown.setText("1!");
                            countdown.setTextFill(Color.rgb(120, 220, 140));
                            centerLabel();
                        }),
                new KeyFrame(
                        Duration.seconds(4),
                        event -> {
                            countdown.setText("AHORA!");
                            countdown.setTextFill(Color.rgb(220, 220, 230));
                            centerLabel();
                            pause.play();
                        }));
        tempLine.play();

    }

    private void centerLabel() {
        countdown.applyCss();
        countdown.layout();

        double width = countdown.prefWidth(-1);

        countdown.setLayoutX((getWidth() - width) / 2);
    }

    private void resetBall() {
        ball.setCenterX(getWidth() / 2);
        ball.setCenterY(100 + (getHeight() - 100) / 2);

        ballVelocityX = 4;
        ballVelocityY = 6;

        if (rm.nextBoolean()) {
            ballVelocityX *= -1;
        }

        if (rm.nextBoolean()) {
            ballVelocityY *= -1;
        }
    }

    private void resetRectangle() {

        rectangleVelocity = 6.0;

        double centerY = 100 + (getHeight() - 100) / 2;

        leftRectangle.setX(30);
        leftRectangle.setY(centerY - leftRectangle.getHeight() / 2);

        rightRectangle.setX(getWidth() - 40);
        rightRectangle.setY(centerY - rightRectangle.getHeight() / 2);
    }

    private void exitGame() {

        gameLoop.stop();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Quieres salir del juego?");
        alert.setContentText("La partida actual terminará.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            endgame = System.nanoTime();
            timeofgame = (long) ((endgame - startgame) / 1_000_000_000.0);

            MatchDTO dto = new MatchDTO(player1, player2, scoreLeft, scoreRight, timeofgame);

            try {
                MatchDAO dao = new MatchDAO(DatabaseConnection.getConnection());
                dao.save(dto);

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

        } else {
            gameLoop.start();
        }
    }
}
