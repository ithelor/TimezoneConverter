package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Controller {

    @FXML BorderPane borderPane;
    @FXML ScrollPane scrollPane;
    @FXML AnchorPane spAnchorPane;

    final static int utcIndex = 12, timeZonesNumber = 24;
    int currentTimeZoneIndex;

    Label[] labels = new Label[timeZonesNumber];
    TextField[] textFields = new TextField[timeZonesNumber];

    String[] TimeZoneAbbreviations = {
            "NST", "SST", "AET", "JST", "CCT", "VST", "BST", "PLT",
            "NET", "MSK", "EET", "ECT", "UTC", "CAT", "UTC -2", "BET",
            "PRT", "EST", "CST", "PNT", "PST", "AST", "HST", "MIT"
    };

    SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
    Timeline clock;

    // Not used
    public String getUTCOffset()
    {

        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = GregorianCalendar.getInstance(timeZone);
        int offsetInMillis = timeZone.getOffset(calendar.getTimeInMillis());

        return "UTC " + (offsetInMillis >= 0 ? "+" : "-") + String.format("%02d", Math.abs(offsetInMillis / 3600000));
    }

    public String _trim(String string)
    {
        return string.replaceAll("[^0-9?!.\\Q-+\\E]","");
    }

    public String getTrimmedNodeId(Node node)
    {
        return _trim(node.getId().substring(node.getId().length() - 3));
    }

    public int getTimeZoneIndex()
    {

        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = GregorianCalendar.getInstance(timeZone);
        int offsetInMillis = timeZone.getOffset(calendar.getTimeInMillis());

        String offset = "UTC " + (offsetInMillis >= 0 ? "+" : "-") + String.format("%02d", Math.abs(offsetInMillis / 3600000));

        Label result = null;

        for (Label curLabel : labels)
        {

            String offsetTrimmed = _trim(offset.substring(offset.length() - 3));
            String curLabelIdTrimmed = getTrimmedNodeId(curLabel);

            if (Double.parseDouble(offsetTrimmed) == Double.parseDouble(curLabelIdTrimmed))
                result = curLabel;
        }

        for (int i = 0; i < labels.length; i++)
            if (labels[i] == result)
                return i;

        return 12;
    }

    public void updateTextFields()
    {
        for (TextField curTextField : textFields)
        {
            curTextField.setText(HHmmss.format(
                    new Date(System.currentTimeMillis() +
                            (3600 * (int)Double.parseDouble(_trim(curTextField.getId()))) * 1000))
            );
        }
    }

    public void initRightBorder(int timeZonesNumber)
    {

        for (int i = 0; i < timeZonesNumber; i++)
        {

            labels[i] = new Label("UTC " + ((12 - i) > 0 ? "+" : "") + (12 - i));
            labels[i].setId("tzLabel" + ((12 - i) > 0 ? "+" : "") + (12 - i));
            labels[i].setAlignment(Pos.CENTER_RIGHT);
            labels[i].setLayoutX(0); labels[i].setLayoutY(35 * i);
            labels[i].setPrefSize(50, 30);
            labels[i].setText(TimeZoneAbbreviations[i]);
            AnchorPane.setLeftAnchor(labels[i], 0.0);
            spAnchorPane.getChildren().add(labels[i]);

            textFields[i] = new TextField();
            textFields[i].setId("tzTextField" + ((12 - i) > 0 ? "+" : "") + (12 - i));
            textFields[i].setLayoutX(40); textFields[i].setLayoutY(35 * i);
            textFields[i].setPrefSize(80, 30);
            textFields[i].setEditable(false);
            textFields[i].setAlignment(Pos.CENTER);
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

        initRightBorder(timeZonesNumber);
//        String offset = getUTCOffset();

        currentTimeZoneIndex = getTimeZoneIndex();

        labels[currentTimeZoneIndex].setStyle("-fx-background-color: rgba(255, 180, 255, 0.75);");
        textFields[currentTimeZoneIndex].setStyle("-fx-background-color: rgba(255, 180, 255, 0.75);");

        HHmmss.setTimeZone(TimeZone.getTimeZone("UTC"));

        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            textFields[utcIndex].setText(HHmmss.format(new Date()));
            updateTextFields();

        }), new KeyFrame(Duration.seconds(0.25)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}