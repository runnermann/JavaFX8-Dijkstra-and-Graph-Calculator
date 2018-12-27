package view;


import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Tooltip;
//import type.tools.imagery.Fit;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

/**
 * SectionEditor is the default editor shown in CreateCard and EditCard ?. It contains the buttons to create/ edit an image,
 * add or delete video or audio files, add drawings to images, and delete. It contains the ability to take a screen snap
 * shot or drag & drop files into the left pane.
 *
 * Algorithm,
 * - Each section of a card is edited by the section editor. Each cell type contains its own editing methods
 *  - User creates a card. The default card is a doubleVertical section containing two sections.
 *  - The default section view is a singleSection with containing a tCell.textVBox (TextCell)
 *
 *  !!!***  NOTE ***!!!
 *  Methods that contain the SnapShot() constructor call do
 *  not return to that method. Calls after the SnapShot constructor call may not
 *  behave correctly or may not execute until later. The Runnable statement may be needed
 *  to execute them.
 *
 *  - (SnapShot) If the user clicks on snapShot,
 *      - SnapShot creates a full screen stage and contains the methods and variables to allow the user to draw a dashed
 *      rectangle anywhere on the screen. When the user releases the mouse button, an image of the area within the
 *      rectangle is saved to the buffer. The size and location of the snapshot rectangle is saved and passed to DrawTools.
 *          - The image is saved to file, and the fileName is saved to the arrayOfFiles which is stored in each FlashCard
 *          object.
 *          - An image is shown in the rightPane, The rightpane is contained in the right StackPane.
 *          - A delete button is added to the right StackPane.
 *          - A delete button is added to the left StackPane. Left StackPane contains the textVBox.
 *      - DrawTools recreates the rectangle the user created to take the snapshot. The rectangle is solid blue and is the
 *      same shape/size and in the same location.
 *      - The DrawTools provides access to shape classes, methods and variables to create, edit, & delete shapes.
 *      - Shapes are added to a shapeArray that is saved to each FlashCard object so they are editable later.
 *      - See DrawTools for further details when needed.
 *      - When the user closes drawtools, the blue rectangle is removed from the screen.
 *   - (Drag & Drop) If the user drags an image, audio, or video file onto the TextArea
 *      - If the file is of the right type, not all files are supported. The file is saved to to its location on the users
 *      computer. The fileName is saved to the arrayOfBuilderShapes according to its location on the screen. IE upper area,
 *      lower area.
 *      - If an image is added, it has all the features of an image meaning it has a pop-up view capability which has
 *      the abilty to add & edit shapes.
 *      - If an audio or video file is added, then the multi-media player is shown to the viewer and the user can
 *      play the file (but not edit).
 */
public class Editor //implements FMMouseInterface
{

    /**
     * The array of FM shapes as opposed to the arrayOfBuilderShapes which are JavaShapes. FM
     * shapes have added variables and methods
     */
    //public ArrayList<GenericShape> arrayOfFMShapes;// = new ArrayList<>(5);

    /**
     * VARIABLES
     **/
    private Stage window;
    public  HBox masterHBox;
    public  VBox txtCellVBox;
    private Button clearBtn;
    private HBox buttonBox;

    // The currentCard's qID (From the calling class)
    // used when naming a multimedia file.
    //private static int currentCardCID;
    private char qOra;
    /**
     * Double cell sections
     * 'M' text cell on left and media to right
     * 'C' Text cell on left and canvas on right
     *
     * Single cell sections
     * 'm' media only
     * 'c' canvas only
     * 'f' fill in the blank
     * 'm' media audio or video
     * 't' text only
     */
    private char sectionType;// = 't'; // double or single section

    public StackPane stackL;
    public TextCell tCell;


    // Disable flag for Drag n Drop
    private boolean disable;
    // isCharDouble()
    private boolean bool;


    /**
     * Constructor
     */
    public Editor()
    {

        this.tCell   = new TextCell();
        this.masterHBox  = new HBox();
        this.stackL = new StackPane();

        this.txtCellVBox = new VBox(tCell.buildCell("", ""));

        // The stackpanes containing left and right items. Allows
        // delete buttons on the layer above the panes.
        stackL.getChildren().add(txtCellVBox);
        stackL.setAlignment(Pos.TOP_RIGHT);


        tCell.getTextArea().setEditable(true);

        //this.qOra = letter;

        this.clearBtn = new Button("x");
        this.clearBtn.setFocusTraversable(false);
        this.clearBtn.setTooltip(new Tooltip("Clear the text area"));

        this.clearBtn.setOnAction((ActionEvent e) ->
        {
            tCell.getTextArea().setText("");
            tCell.getTextArea().requestFocus();
        });






        this.buttonBox = new HBox();
        this.buttonBox.getChildren().add(this.clearBtn);
        this.buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        masterHBox.setSpacing(6);
        masterHBox.setPadding(new Insets(6, 6, 6, 6));
        masterHBox.setStyle("-fx-background-color: white");
        masterHBox.setAlignment(Pos.BOTTOM_LEFT);


        this.txtCellVBox.getChildren().add(this.buttonBox);
        // add stackL (stackLeft) to masterHBox
        this.masterHBox.getChildren().addAll(this.stackL);

    } // END CONSTRUCTOR


    // Clear stackR from masterHBox & remove
    // deleteTCellbtn from stackL
    public void deleteMMcellAction()
    {
        this.sectionType = 't';

        masterHBox.getChildren().clear();
        stackL.getChildren().clear();//remove(deleteMMCellBtn);
        stackL.getChildren().add(txtCellVBox);
        masterHBox.getChildren().add(this.stackL);
    }

    /**
     * Clears the text area, removes the clear button
     * and removes stackR.
     */
    public void resetSection()
    {
        tCell.getTextArea().setText("");
        deleteMMcellAction();
    }



    /** ************************************************************************************************************ **
     *                                                                                                                 *
     *                                                      GETTERS                                                    *
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/


    /**
     * Returns the text from the textCell textArea.
     * Convienience method.
     * @return
     */
    public String getText()
    {
        return this.tCell.getTextArea().getText();
    }



    /** ************************************************************************************************************ **
     *                                                                                                                 *
     *                                                      SETTERS                                                    *
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/


    /**
     * Sets the character color and style to
     * a prompt appearance
     */
    public void styleToPrompt()
    {
        this.tCell.getTextArea().setStyle("-fx-prompt-text-fill: rgba(0,0,0,.5); ");
    }

    /**
     * Sets the character color and appearance
     * to normal.
     */
    public void styleToNormal()
    {
        this.tCell.getTextArea().setStyle("-fx-prompt-text-fill: rgba(f,f,f,1); ");
    }

    /**
     * Sets the text in the textCells textArea.
     * Convienience method
     * @param text
     */
    public void setText(String text)
    {
        this.tCell.getTextArea().setText(text);
    }

    /**
     * Sets the prompt text in the textCells textArea
     * @param text
     */
    public void setPrompt(String text)
    {
        this.tCell.getTextArea().setPromptText(text);
    }


    public void setStyleError() {
        tCell.getTextArea().setStyle("-fx-text-fill: RED");
    }

    public void setStyleNormal() {
        tCell.getTextArea().setStyle("-fx-text-fill: BLACK");
    }



    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     OTHER METHODS
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/



    /**
     * Populates the TextField for otherAnswers with
     * a string of integers to be displayed for the user.
     * @param intAry
     * @return
     */
    public String populate(ArrayList<Integer> intAry)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < intAry.size(); i++)
        {
            sb.append(intAry.get(i) + ", ");
        }

        System.out.println("in populate, sb: " + sb);

        return sb.toString();
    }


    /**
     * Checks this cardEditor for content
     * @return Returns false if the text area is empty or if
     * the rightPane does not have any children.
     */
    public boolean hasContent()
    {
        if(! tCell.getTextArea().getText().isEmpty()) { return true; }

        return false;
    }

    /**
     * Clears the textArea for this object
     * @param textArea
     */
    protected void clearTextArea(TextArea textArea)
    {
        textArea.setText("");
        textArea.requestFocus();
    }


    /**
     * Call this when leaving or opening a new window.
     * Closes this window, clears vBox and sets textArea edit to false.
     */
    public void onClose()
    {
        //MainGenericsPaneTest.onClose();
        window.close();
        //if(window != null) { window.close() }
        //super.getTextCellVbox().getChildren().clear();
        //super.getTextCellVbox().getChildren().add(super.getTextArea());
        //super.getTextArea().setEditable(false);
    }

}
