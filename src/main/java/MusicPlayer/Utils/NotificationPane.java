package MusicPlayer.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class NotificationPane extends Pane {
    private final Rectangle background;
    private final Label message;

    public NotificationPane() {
        background = new Rectangle(300, 40, Color.LIGHTGRAY);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setOpacity(0.8);

        message = new Label();
        message.setWrapText(true);
        message.setMaxWidth(280);

        setMaxSize(300, 40);
        setClip(background);
        getChildren().addAll(background, message);
    }

    public void showNotification(String text) {
        message.setText(text);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(translateYProperty(), -40)),
                new KeyFrame(Duration.millis(500), new KeyValue(translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(3.5), new KeyValue(translateYProperty(), -40))
        );

        timeline.play();
    }
}
