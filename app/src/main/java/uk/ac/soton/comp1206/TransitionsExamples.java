package uk.ac.soton.comp1206;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TransitionsExamples extends Application {
    private IntegerProperty lives = new SimpleIntegerProperty(3);
    private StringProperty livesText = new SimpleStringProperty("Lives: 3");

    @Override
    public void start(Stage primaryStage) {

//        rotator(primaryStage);
//        fader(primaryStage);
//        timer(primaryStage);
//      translaterKeyFrames(primaryStage);
        animationTimerExample(primaryStage);

    }
    public void rotator(Stage primaryStage) {
        // Load the image (ensure the path is correct based on your project structure)
        Image image = new Image("/afc.png");

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);

        // Set up a RotateTransition for the ImageView
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), imageView);
        rotateTransition.setByAngle(360); // Rotate by 360 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Rotate indefinitely
        rotateTransition.setAutoReverse(true); // Enable auto-reverse to rotate back
        rotateTransition.play(); // Start the rotation

        // Set up the scene and stage
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 600, 450);

        primaryStage.setTitle("Rotate Image Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void timer(Stage primaryStage) {

        // Create a rectangle with initial width of 200
        Rectangle rectangle = new Rectangle(200, 50, Color.BLUE);

        // Create a label to display lives
        Label livesLabel = new Label();
        livesLabel.textProperty().bind(lives.asString("Lives: %d"));
        livesLabel.setLayoutX(10); // Positioning the label
        livesLabel.setLayoutY(10);

        // Create a Timeline animation
        Timeline timeline = new Timeline();

        // Define the KeyFrame for animating the rectangle's width to 0
        KeyFrame shrinkKeyFrame = new KeyFrame(Duration.seconds(5), new KeyValue(rectangle.widthProperty(), 0));

        timeline.getKeyFrames().add(shrinkKeyFrame);
        timeline.setOnFinished(e -> {
            // Decrease lives by 1 when the animation finishes
            lives.set(lives.get() - 1);

            // Check if lives are above 0 before restarting the animation
            if (lives.get() > 0) {
                // Reset the rectangle's width
                rectangle.setWidth(200);
                // Restart the animation
                timeline.playFromStart();
            }
        });

        // Start the animation
        timeline.play();

        // Setting up the scene and stage
        Pane root = new Pane();
        HBox b = new HBox();
        b.getChildren().addAll(livesLabel,rectangle);
        root.getChildren().addAll(b);

        Scene scene = new Scene(root, 300, 150);

        primaryStage.setTitle("Bar Animation with Restart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void fader(Stage primaryStage){
        Rectangle rectangle = new Rectangle(800, 800, 400, 400);
        rectangle.setFill(Color.BLUE);
        HBox b = new HBox();
        b.getChildren().addAll(rectangle);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3));
        fadeTransition.setFromValue(1.0); // Fully visible
        fadeTransition.setToValue(0.0); // Fully transparent
        fadeTransition.setAutoReverse(true);
        //fadeTransition.play();
        Scene scene = new Scene(b, 1200, 600);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1));
        scaleTransition.setToX(2.0); // Scale to double width
        scaleTransition.setToY(2.0); // Scale to double height
        scaleTransition.setDelay(Duration.seconds(2));
        scaleTransition.setAutoReverse(true); // Automatically reverse the animation
//        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE); // Run the animation indefinitely
//        SequentialTransition seq = new SequentialTransition(rectangle, scaleTransition,fadeTransition);
//        seq.play();
        ParallelTransition pt = new ParallelTransition(rectangle, scaleTransition,fadeTransition);
        pt.play();
        pt.setAutoReverse(true);
        primaryStage.setTitle("Bar Animation with Restart");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void translaterKeyFrames(Stage primaryStage){
        Rectangle rectangle = new Rectangle(100, 40, 100, 100);

        // Moving the rectangle 10 pixels to the right
        rectangle.translateXProperty().set(10);
        KeyFrame kf = new KeyFrame(Duration.seconds(1),new KeyValue(rectangle.translateXProperty(),25));
        KeyFrame kf2 = new KeyFrame(Duration.seconds(2),new KeyValue(rectangle.translateYProperty(),50));
        KeyFrame kf3 = new KeyFrame(Duration.seconds(3),new KeyValue(rectangle.translateYProperty(),300));
        KeyFrame kf4 = new KeyFrame(Duration.seconds(3),new KeyValue(rectangle.translateXProperty(),300));
        Timeline t = new Timeline();
        t.getKeyFrames().addAll(kf,kf2,kf3,kf4);
        t.play();
        Pane root = new Pane(rectangle);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Translation Example");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

    public void animationTimerExample(Stage primaryStage){
        Circle circle = new Circle(200, 300, 40, Color.BLUE);

        // Create a root group and add the circle to it
        Group root = new Group(circle);
        Scene scene = new Scene(root, 800, 600);

        // AnimationTimer to move the circle across the screen
        AnimationTimer animator = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0;
                    double deltaX = 100 * elapsedSeconds; // Move 100 pixels per second
                    double deltaY = (2000*Math.random()-1000) * elapsedSeconds; // Move 100 pixels per second
                    double newX = circle.getTranslateX() + deltaX;
                    double newY = circle.getTranslateY() + deltaY;
                    if (newX > scene.getWidth()) {
                        newX = 0; // Reset to the left side
                    }
                    if (newY > scene.getHeight()) {
                        newY = 0; // Reset to the left side
                    }
                    circle.setTranslateX(newX);
                    circle.setTranslateY(newY);
                }
                lastUpdate = now;
            }
        };

        // Start the animation
        animator.start();

        primaryStage.setTitle("AnimationTimer Example");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}


