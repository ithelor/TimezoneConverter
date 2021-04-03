package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Controller {

    @FXML GridPane gridPane;
    @FXML ScrollPane scrollPane;
    @FXML AnchorPane spAnchorPane;

    Label[] labels = new Label[25];
    TextField[] textFields = new TextField[25];

    public String getUTCOffset()
    {

        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = GregorianCalendar.getInstance(timeZone);
        int offsetInMillis = timeZone.getOffset(calendar.getTimeInMillis());

        return "UTC " + (offsetInMillis >= 0 ? "+" : "-") + String.format("%02d", Math.abs(offsetInMillis / 3600000));
    }

    public Label getLabelByTimeZone(String timeZone)
    {

        Label result = null;

        for (Label curLabel : labels)
        {
            if (timeZone.equals(curLabel.getText()))
                result = curLabel;
//            result = (timeZone.equals(curLabel.getText())) ? curLabel : null;
//            if (result != null) break;
        }

        return result;
    }

    public TextField getTextFieldByLabel(Label label)
    {

        TextField result = null;

        String labelID = null;
        String textFieldID = null;

        for (TextField curTextField : textFields)
        {
            labelID = label.getId().substring(label.getId().length() - 3);
            textFieldID = curTextField.getId().substring(curTextField.getId().length() - 3);

            if (labelID.equals(textFieldID))
                result = curTextField;
        }

        return result;
    }

    public void initRightBorder()
    {

        for (int i = 0; i < 25; i++)
        {

            labels[i] = new Label("UTC " + ((12 - i) > 0 ? "+" : "") + (12 - i));
            labels[i].setId("tzLabel" + ((12 - i) > 0 ? "+" : "") + (12 - i));
            labels[i].setAlignment(Pos.CENTER_RIGHT);
            labels[i].setLayoutX(0);
            labels[i].setLayoutY(35 * i);
            labels[i].setPrefSize(50, 30);
            AnchorPane.setLeftAnchor(labels[i], 0.0);

            System.out.println(labels[i].getId());

            spAnchorPane.getChildren().add(labels[i]);

            textFields[i] = new TextField();
            textFields[i].setId("tzTextField" + ((12 - i) > 0 ? "+" : "") + (12 - i));
            textFields[i].setLayoutX(40);
            textFields[i].setLayoutY(35 * i);
            textFields[i].setPrefSize(80, 30);
            AnchorPane.setLeftAnchor(textFields[i], 55.0);
            AnchorPane.setRightAnchor(textFields[i], 0.0);

            System.out.println(textFields[i].getId());

            spAnchorPane.getChildren().add(textFields[i]);
        }
    }

    public void initialize()
    {

        initRightBorder();

        String offset = getUTCOffset();
        System.out.println(offset);

        Label curLabel = getLabelByTimeZone(offset);
        System.out.println(curLabel);

        TextField curTextField = getTextFieldByLabel(curLabel);
        System.out.println(curTextField);

        curTextField.setText(java.time.LocalTime.now().toString());

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            curTextField.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
