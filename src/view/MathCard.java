package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import dijkstracalculator.*;
import controller.SceneCntl;

import java.util.ArrayList;


/**
 * MathCard is an interactive calculator.
 *
 * - INTENT:
 *  - User inputs formula
 *  - Calculate using PERMDAS order of operations, division, multiplication, signs
 *      etc.. during card use.
 *
 *      -Pop up the order of execution in a drop down scroll box. The user may interact
 *      with the expression by hovering the mouse over the boxes below. This gives the
 *      user a greater understanding of what was done and when.
 *
 *  - To add additional operators, add them in the appropriate class or the Operator class.
 *  Add them as an enum. Include it's type in the getOperator() switch in DijkstraParser. Provide
 *  it's priority and ensure that it is correct.
 */
public class MathCard {//implements GenericAnswer<MathCard> {

    protected DijkstraParser parser;// = new DijkstraParser();

    /** Panes **/
    // Contains the upper Section
    private HBox upperHBox;
    // contains the lower Section
    private VBox lowerVBox;
    // Contains the interactive indicator over the question
    private Pane pane;

    HBox masterHBox;//
    TextArea  expressionTArea;
    TextField ansField;
    // The Mathmatical expression from/for the question
    protected String expression;
    // Problem as executed by DijkstraParser
    private static String[] ansComponents;
    // Buttons
    Button calcButton = new Button("Calc");


    /** METHODS **/

    /**
     * Default constructor
     */

    public MathCard()
    {
        masterHBox = new HBox();
        masterHBox.setPrefHeight(SceneCntl.getHt());
        expressionTArea = new TextArea();
        ansField = new TextField();
        upperHBox = new HBox();
        upperHBox.setPrefSize(SceneCntl.getWd(), 60);
        //upperHBox.setMaxHeight(60);
        // upperHBox.setMinHeight(60);
        upperHBox.setPadding(new Insets(2, 8, 2, 8));
        pane = new Pane();
        pane.setMaxHeight(SceneCntl.getCellHt());
        pane.setMinHeight(SceneCntl.getCellHt());
    }


    /**
     *
     * @return Returns the Vbox with problem card and response card.
     */
    //@Override
    public VBox getCalcPane()
    {
        // Instantiate vBox and "set spacing"
        VBox vBox = new VBox(2);

        // Set prompt in Question/upperBox
        expressionTArea.clear();
        expressionTArea.setPromptText("Enter Math Formula");
        expressionTArea.setPrefWidth(SceneCntl.getWd());
        upperHBox.getChildren().clear();
        upperHBox.getChildren().add(expressionTArea);

        calcButton.setOnAction(e -> {

            expression = expressionTArea.getText();
            calcButtonAction(expression, expressionTArea);
        });

        pane.getChildren().clear();
        pane.getChildren().add(upperHBox);
        lowerVBox = getAnswerBox();
        lowerVBox.setPadding(new Insets(2, 8, 2, 8));
        vBox.getChildren().addAll(pane, lowerVBox);
        return vBox;
    }

    private String isNumber(String inStr) {

        try
        {
            Double.parseDouble(inStr);
        }
        catch(NumberFormatException e)
        {
            return "Please enter a valid number";
        }

        return inStr;
    }

    //@Override
    public void reset() {
        parser.clearStructures();
    }

    /**
     * Used by lambda in getResponseBox
     */
    private HBox expHBox = new HBox();
    private int prevIdx = 0;
    private Circle circle = new Circle(9);
    //private Rectangle rect = new Rectangle(18 , 22);
    private ArrayList<TextField> txtFldAry;
    /**
     * Provides the interactive step by step evaluation of the
     * problem
     * @return returns an HBox containing the evaluations
     * in ordered form.
     */
    private ScrollPane getResponseBox(ArrayList expList)
    {

        System.out.println("*** getResponseBox ***");

        VBox vBox = new VBox(2);

        ScrollPane scrollPane = new ScrollPane();

        while( ! Operator.getAnsComponents().isEmpty())
        {
            ExpNode exp = Operator.getAnsComponents().poll();

            TextField textField = new TextField(exp.getExpSolved());
            textField.setPadding(new Insets(6));
            // Action on mouse entered
            textField.setOnMouseEntered(e -> {

                textField.setStyle("-fx-border-color:" + UIColors.HIGHLIGHT_PINK);
                // prevent textField in scrollPane from causing jitter bc of expansion
                textField.setPadding(new Insets(5));//Height(8);
                setIActiveCircle(exp.getIndex());
                prevIdx = exp.getIndex();

            });
            textField.setOnMouseExited(e -> {
                mouseExitedExpBox();
                textField.setPadding(new Insets(6));//Height(8);
                textField.setStyle("-fx-border-color: default");
            });
            vBox.getChildren().add(textField);
        }

        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxHeight(200);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /**
     * The users response/answer box
     * @return
     */
    private VBox getAnswerBox() {

        HBox box = new HBox(2);
        ansField = new TextField();
        ansField.setPromptText("pres calc for answer");
        ansField.setEditable(false);
        ansField.setPrefColumnCount(10);
        box.getChildren().addAll( calcButton, ansField);
        box.setAlignment(Pos.CENTER);
        VBox vB = new VBox(2);
        vB.getChildren().add(box);

        return vB;
    }


    /**
     * Sets the Interactive question when the users response is answered incorrectly. It
     * adds a circle/rectangle over the operator when the user selects an answer part in
     * the response box.
     * @param expIdx
     */
    private void setIActiveCircle( int expIdx) {

        System.out.println("*** !!! *** setIActiveCircle() *** !!! ***");

        if(pane.getChildren().contains(circle))
        {
            pane.getChildren().remove(circle);
        }

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.web(UIColors.HIGHLIGHT_PINK));
        circle.setStrokeWidth(1.6);


        TextField textField = txtFldAry.get(expIdx);
        // circle centerX and centerY
        double x = textField.getLayoutX() + (textField.getWidth() / 2);
        double y = textField.getLayoutY() + (textField.getHeight() / 2);
        circle.setCenterX(x + 8);
        circle.setCenterY(y + 2);

        pane.getChildren().add(circle);
        // make the interactive question with transparent boxes.
        textField.setStyle("-fx-stroke-width: 0; -fx-stroke: TRANSPARENT; -fx-background-color: TRANSPARENT;");

    }

    private void mouseExitedExpBox() {

        System.out.println("*** Called mouseExitedExpBox() ***");

        pane.getChildren().remove(circle);
    }

    /**
     * Builds each part of the expression (number or operator) as a textField
     * to allow interactive highlighting of the operators.
     * @param expList
     * @return
     */
    private HBox interActiveQuestion(ArrayList expList) {

        expHBox = new HBox();
        expHBox.setMaxHeight(20);
        expHBox.setMinHeight(20);
        txtFldAry = new ArrayList<>(10);
        TextField tf;
        int length;
        for(int i = 0; i < expList.size(); i++) {

            length = expList.get(i).toString().length();
            tf = new TextField(expList.get(i).toString());
            tf.setMinHeight(14);
            tf.setMaxHeight(14);
            tf.setMaxWidth(length * 9);
            tf.setMinWidth(length * 9);
            tf.setStyle("-fx-stroke-width: 0; -fx-stroke: TRANSPARENT; -fx-background-color: TRANSPARENT;");
            // Important !!!
            tf.setPadding(Insets.EMPTY);
            tf.alignmentProperty().setValue(Pos.CENTER);

            txtFldAry.add(tf);
            expHBox.getChildren().add(tf);
        }

        return expHBox;
    }


    // TESTING the input

    /**
     * Calculates the expression. If there is invalid input, posts an error message in
     * the ansField.
     * @param expression
     * @param textArea
     */
    public void calcButtonAction(String expression, TextArea textArea) {

        System.out.println("**** calcButtonAction in MathCard ****");

        parser = new DijkstraParser(expression);

        if(inputHandler(parser, expression, textArea))
        {
            String result = Double.toString(parser.getResult());
            ansField.setText(result);
            upperHBox.getChildren().clear();
            upperHBox.getChildren().add(interActiveQuestion(parser.getWriterList()));
            Label label = new Label("Calculated from left to right using PERMDAS");
            lowerVBox.getChildren().clear();
            lowerVBox.getChildren().addAll( label);
            lowerVBox.getChildren().add(getResponseBox(parser.getWriterList()));
        }

        System.out.println("Results: " + parser.getResult());
    }

    /**
     * Verifies input, If not displays an error message in the input TextArea for a
     * few seconds then returns to the original input.
     * @param parser
     * @param expression
     * @param ta
     * @return
     */
    protected boolean inputHandler(DijkstraParser parser, String expression, TextArea ta) {

        if(parser.isInvalidInput())
        {

            // Insert a temporary error message into the
            // text area
            StringBuilder sb = new StringBuilder();
            sb.append(expression);
            sb.append("\n\n " + parser.getErrorMessage());
            ta.setText(sb.toString());

            // After a delay, return the textArea back
            // to the original expression.
            EventHandler<ActionEvent> eventHandler = e -> {
                ta.setText(expression);
            };

            Timeline animation = new Timeline(new KeyFrame(Duration.millis(5000), eventHandler));
            animation.play();
            return false;
        }
        return true;
    }

}
