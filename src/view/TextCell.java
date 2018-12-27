package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

//import javafx.scene.layout.HBox;

/**
 * This class manages the values of the VBox that contains textAreas used by FlashMonkey.
 * It does not include editable textAreas. Refer to the child Class FMTextEditable if an
 * editable TextArea is needed.
 */
public class TextCell //extends GenericCell<Pane>//implements Serializable
{
    //private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    // Variables
    public VBox textCellVbox;
    private TextArea textArea;


    /**
     * Default constructor
     */

    public TextCell() {

        textCellVbox = new VBox();
        textArea = new TextArea();
        textCellVbox.setPadding(new Insets(6, 6, 6, 6));
        textCellVbox.setStyle("-fx-background-color: white");
        textCellVbox.setAlignment(Pos.BOTTOM_LEFT);
        textArea.setWrapText(true);
        //textArea.setPromptText(promptTxt);
        textArea.setEditable(false);

        textCellVbox.getChildren().add(textArea);
    }


    /**
     * Full constructor
     * @param prompt
     */
    //protected TextCell(String prompt) {

    /**
     * Sets the text area
     * @param txt The text inside the TextArea. ie The answer or question
     */
    public VBox buildCell(String txt, String prompt)
    {

        System.out.println("\n ~^~^~ in TextCell.buildCell() ~^~^~");

        textCellVbox = new VBox();
        textArea = new TextArea();
        textCellVbox.setPadding(new Insets(2, 2, 0, 2));
        textCellVbox.setStyle("-fx-background-color: white");
        //textPane.setAlignment(Pos.BOTTOM_LEFT);
        textArea.setWrapText(true);
        textArea.setPromptText(prompt);
        textArea.setEditable(false);
        textArea.setText(txt);
        textCellVbox.getChildren().add(textArea);
        return textCellVbox;
    }

    /** SETTERS **/

    /** GETTERS **/

    public TextArea getTextArea()
    {
        return this.textArea;
    }

    public VBox getTextCellVbox()
    {
        return this.textCellVbox;
    }

}
