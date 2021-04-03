package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.time.Instant;
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

    SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
    Timeline clock;

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
        }

        return result;
    }

    public TextField getTextFieldByLabel(Label label)
    {

        TextField result = null;

        for (TextField curTextField : textFields)
        {
            String labelID = label.getId().substring(label.getId().length() - 3);
            String textFieldID = curTextField.getId().substring(curTextField.getId().length() - 3);

            if (labelID.equals(textFieldID))
                result = curTextField;
        }

        return result;
    }

    // TODO: getID functions
    public void updateTextFields(TextField utcTextField)
    {
        for (TextField curTextField : textFields)
        {
            String textFieldID = curTextField.getId().replaceAll("[^0-9?!.\\Q-\\E]","");
            System.out.println(textFieldID);

//            if (Double.parseDouble(textFieldID) < 0)
                curTextField.setText(f.format(new Date()));
        }
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

            spAnchorPane.getChildren().add(labels[i]);

            textFields[i] = new TextField();
            textFields[i].setId("tzTextField" + ((12 - i) > 0 ? "+" : "") + (12 - i));
            textFields[i].setLayoutX(40);
            textFields[i].setLayoutY(35 * i);
            textFields[i].setPrefSize(80, 30);
            AnchorPane.setLeftAnchor(textFields[i], 55.0);
            AnchorPane.setRightAnchor(textFields[i], 0.0);

            spAnchorPane.getChildren().add(textFields[i]);

            spAnchorPane.addEventHandler(MouseEvent.MOUSE_ENTERED, e ->
            {
                clock.pause();
                spAnchorPane.setStyle("-fx-background-color: rgba(180, 180, 180, 0.5); -fx-background-radius: 20;");
            });

            spAnchorPane.addEventHandler(MouseEvent.MOUSE_EXITED, e ->
            {
                clock.play();
                spAnchorPane.setStyle(null);
            });
        }
    }

    public void initialize()
    {

        initRightBorder();

        String offset = getUTCOffset();
        System.out.println(offset);

        Label curLabel = getLabelByTimeZone(offset);
        TextField curTextField = getTextFieldByLabel(curLabel);
        curTextField.setStyle("-fx-background-color: rgba(255, 180, 255, 0.75);");

        f.setTimeZone(TimeZone.getTimeZone("UTC"));

        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            textFields[12].setText(f.format(new Date()));
            updateTextFields(textFields[12]);

        }), new KeyFrame(Duration.seconds(0.25)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
