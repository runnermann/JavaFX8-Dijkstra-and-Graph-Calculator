package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import dijkstracalculator.*;
import controller.SceneCntl;
import controller.FMTransition;

import java.util.ArrayList;


/**
 * MathCard is an interactive calculator.
 *
 * - INTENT:
 *  - Allows user to input formula
 *  - Ensures compliance with PERMDAS, division, multiplication, signs
 *      etc.. during card use.
 *      Provides advice upon an incorrect answer and uses
 *      logic by calculating incorrect answers when attempting
 *      to understand the users mistakes. Then provides the user
 *      advice on their error. And or what to check.
 *      -Pops up advice about the possible errors. IE
 *      PERMDAS error. and provides correction. As a possiblity
 *      may provide additional problems to assist with correcting
 *      the error.
 *      - Card may input random number values into fields and test the user
 *      on the card.
 *  - Ensures compliance with PERMDAS, Signs, Multiplication, Division, Addition,
 *      subtraction, etc...
 *
 *  - To add additional operators add them in the appropriate class or the Operator class.
 *  Add them as an enum. Include it's type in the getOperator() switch in DijkstraParser. Provide
 *  it's priority and ensure that it is correct.
 */
public class MathCard {//implements GenericAnswer<MathCard> {

    protected DijkstraParser parser;// = new DijkstraParser();

    /** Panes **/
    // Contains the entire card
    protected GridPane gPane;// vBox;
    // Contains the upper Section
    private HBox upperHBox;
    // contains the lower Section
    private VBox lowerVBox;
    // Contains the interactive indicator over the question
    private Pane pane;

    HBox masterHBox = new HBox();



    TextField ansField;

    /** The GenericSection object **/
    //private GenericSection genSection;

    /** Answer Button **/
    private Button answerButton;

    /** The Mathmatical expression from/for the question **/
    protected String expression;

    /** Problem as executed by DijkstraParser **/
    private static String[] ansComponents;

    // Buttons
    Button calcButton = new Button("Calc");
    Button addGraph = new Button("graph");


    /**
     *
     * @param p The formula for the math/Algebra/Trig/Calc problem
     * @param r The response when the user gets the answer wrong or correct.
     * @return Returns the Vbox with problem card and response card.
     */
    //@Override
    public VBox getTEditorPane( Editor p, Editor r)
    {
        // Instantiate vBox and "set spacing" !important!!!

        VBox vBox = new VBox(2);
        StackPane sPane = new StackPane();
        masterHBox.setPrefHeight(SceneCntl.getHt());
        // Set prompt in Question/upperBox
        p.setPrompt("Enter Math Formula");
        r.setPrompt("Enter the response if answered incorrectly");
        sPane.getChildren().add(p.masterHBox);
        vBox.getChildren().addAll(sPane, r.masterHBox);

        //boolean bool = p.verifyIsValid();

        // TESTING
        vBox.getChildren().add(calcButton);

        calcButton.setOnAction(e -> {
            expression = p.getText();
            calcButtonAction(expression, p);
        });
        // END TESTING

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
    public Button[] getAnsButtons() {
        return new Button[] {answerButton};
    }

    //@Override
    public Button getAnsButton() {
        return answerButton;
    }

    //@Override
    public void ansButtonAction() {

        // The users response
        String response = ansField.getText();
        parser = new DijkstraParser(expression);

        if( response.isEmpty()) {

            ansField.setPromptText("Enter a response." );

        } else if(response.contains("/") && expression.contains("/")) { // It's a fraction

            System.out.println("Attempting a fraction response ");
            ansField.setEditable(false);
            answerButton.setDisable(true);

            ExpNode exp = Operator.getAnsComponents().getLast();
            String[] parts = response.split("/");
            String answer = exp.getExpSolved();
            String[] correctParts = answer.split(" ");

            // Testing
            System.out.println("correctParts");
            for(int i = 0; i < correctParts.length; i++) {
                System.out.println(i + ") " + correctParts[i]);
            }
            // Testing
            System.out.println("\nparts");
            for(int i = 0; i < parts.length; i++) {
                System.out.println(i + ") " + parts[i]);
            }

            if (Double.parseDouble(parts[0]) == Double.parseDouble(correctParts[1])
                    && Double.parseDouble(parts[1]) == Double.parseDouble(correctParts[3])) {

                ansField.setId("right_border");

            } else {
                responseWrong();
            }

        } else {

            ansField.setEditable(false);
            answerButton.setDisable(true);

            Double correctAnsDbl = 0.0;
            correctAnsDbl = parser.getResult();

            System.out.println("\tcorrectNum == " + correctAnsDbl);
            System.out.println("\tresponse == " + response);

            double responseDbl = Double.parseDouble(response);

            if(responseDbl == correctAnsDbl) {
                ansField.setId("right_border");
                //ansField.setStyle( "-fx-border-color: " + UIColors.HIGHLIGHT_GREEN + "; -fx-border-width: 5;");

            } else { // Answer is wrong
                responseWrong();
            }
        }
    }

    //@Override
    public void reset() {
        parser.clearStructures();
    }

    /**
     * Actions when the user provides the incorrect answer. Clears the
     * upperBox and replaces with the interActiveQuestion. Each segment of
     * the expression is then displayed -contained in TextFields. In the lowerBox,
     * An ordered list of the operations as they were executed is displayed.
     * The executed operations (in the lowerBox) are contained in TextAreas and are clickable.
     * Each ordered (executed) operation contains a reference it's operator in the interActiveQuestion.
     * The operator in the interActiveQuestion is highlighted with a circle that is displayed
     * in a StackPane above the interActiveQuestion. Thus relating to the user which operation
     * produced which results in a PERMDAS order of operations.
     */
    private void responseWrong() {

        upperHBox.getChildren().clear();
        upperHBox.getChildren().add(interActiveQuestion(parser.getWriterList()));
        ansField.setId("wrong_border");
        //ansField.setStyle("-fx-border-color: " + UIColors.FM_RED_WRONG_OPAQUE + "; -fx-border-width: 3;");
        lowerVBox.getChildren().add(new TextField("Solve L -> R using PERMDAS"));
        lowerVBox.getChildren().add(getResponseBox(parser.getWriterList()));
    }

    /**
     * The users response/answer box
     * @return
     */
    private VBox getAnswerBox() {

        HBox box = new HBox(2);
        ansField = new TextField();
        ansField.setEditable(true);
        ansField.setPrefColumnCount(10);
        box.getChildren().addAll(ansField, answerButton);
        VBox vB = new VBox(2);
        vB.getChildren().add(box);

        return vB;
    }

    /**
     * Used by lambda in getResponseBox
     */
    HBox expHBox = new HBox();
    int prevIdx = 0;
    Circle circle = new Circle(9);
    Rectangle rect = new Rectangle(18 , 22);
    ArrayList<TextField> tfs;
    /**
     * Provides the step by step evaluation of the
     * problem
     * @return returns an HBox containing the evaluations
     * in ordered form.
     */
    private ScrollPane getResponseBox(ArrayList expList)
    {
        VBox vBox = new VBox(2);

        ScrollPane scrollPane = new ScrollPane();

        while( ! Operator.getAnsComponents().isEmpty())
        {
            ExpNode exp = Operator.getAnsComponents().poll();

            TextField tf = new TextField(exp.getExpSolved());
            tf.setOnMouseEntered(e -> { //  setOnMouseClicked(e -> {

                tf.setStyle("-fx-border-color:" + UIColors.HIGHLIGHT_PINK);
                setIActiveStyle( prevIdx, exp.getIndex());
                prevIdx = exp.getIndex();

            });
            tf.setOnMouseExited(e -> {
                mouseExitedExpBox();
                tf.setStyle("-fx-border-color: DEFAULT");
            });
            vBox.getChildren().add(tf);
        }

        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }


    /**
     * Sets the Interactive question when the users response is answered incorrectly. It
     * adds a circle/rectangle over the operator when the user selects an answer part in
     * the response box.
     * @param prevIdx
     * @param expIdx
     */
    private void setIActiveStyle(int prevIdx, int expIdx) {

        pane.getChildren().remove(circle);
        //pane.getChildren().remove(rect);

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.web(UIColors.HIGHLIGHT_PINK));
        circle.setStrokeWidth(1.6);
        rect.setFill(Color.TRANSPARENT);

        TextField tef = tfs.get(expIdx);
        // circle centerX and centerY
        double x = tef.getLayoutX() + (tef.getWidth() / 2);
        double y = tef.getLayoutY() + (tef.getHeight() / 2);
        circle.setCenterX(x);
        circle.setCenterY(y);

        System.out.println("Circle center is x & y " + circle.getCenterX() + " " + circle.getCenterY() );

        pane.getChildren().add(circle);
        // make the interactive question with transparent boxes.
        tef.setStyle("-fx-stroke-width: 0; -fx-stroke: TRANSPARENT; -fx-background-color: TRANSPARENT;");
    }

    private void mouseExitedExpBox() {

        System.out.println("*** Called mouseExitedExpBox() ***");

        pane.getChildren().remove(circle);
    }

    private HBox interActiveQuestion(ArrayList expList) {

        expHBox = new HBox();
        tfs = new ArrayList<>(10);
        int length;
        for(int i = 0; i < expList.size(); i++) {

            length = expList.get(i).toString().length();

            //String str = new DecimalFormat(".###").format(expList.get(i).toString());
            //str.
            TextField tf = new TextField(expList.get(i).toString());
            //tf.setPrefColumnCount(length);
            //double width = tf.getPrefColumnCount();
            tf.setMaxWidth(length * 9);
            tf.setMinWidth(length * 9);

            tf.setStyle("-fx-stroke-width: 0; -fx-stroke: TRANSPARENT; -fx-background-color: TRANSPARENT;");
            //tf.setMaxWidth(20);
            tf.setPadding(Insets.EMPTY);

            tf.alignmentProperty().setValue(Pos.CENTER);
            //tf.setId("roundCorners");
            tfs.add(tf);
            expHBox.getChildren().add(tf);

        }

        return expHBox;
    }


    // TESTING the input
    public void calcButtonAction(String expression, Editor editor) {

        parser = new DijkstraParser(expression);

        if(parser.isInvalidInput()) {


            System.out.println("TestButton checking for inValidInput set to true: " + parser.isInvalidInput());
            // Insert a temporary error message into the
            // text area
            StringBuilder sb = new StringBuilder();
            sb.append(expression);
            sb.append("\n\n " + parser.getErrorMessage());
            editor.setText(sb.toString());
            editor.setStyleError();
            // After a delay, return the textArea back
            // to the original expression.
            EventHandler<ActionEvent> eventHandler = e -> {
                editor.setText(expression);
                editor.setStyleNormal();
            };

            Timeline animation = new Timeline(new KeyFrame(Duration.millis(5000), eventHandler));
            animation.play();

        } else {

            //Editor.masterHBox.setId("right_border");

        }

        System.out.println("\n\t *~*~* Done parsing: *~*~*" );
        System.out.println("Results: " + parser.getResult());
    }

}
