package view;


import controller.SceneCntl;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import java.awt.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import dijkstracalculator.*;



/**
 * - To add additional operators to this class's capabilities,
 * add them in the appropriate class, or the Operator class.
 *  Add them as an enum. Include it's type in the getOperator() switch in DijkstraParser.
 */
public class GraphCard extends MathCard {

    private Pane graphPane;
    private int graphXLow;
    private int graphXHigh;
    private int graphYLow;
    private int graphYHigh;
    private Graph cardGraph;
    private DijkstraParser parser;



    /** METHODS **/

    public GraphCard()
    {
        super();
    }

    private void init() {

        graphPane = new Pane();
        graphPane.setMaxHeight(200);
        graphPane.setId("graphPane");

        graphXHigh = 6;
        graphXLow = -8;
        graphYHigh = 300;
        graphYLow = -20;

        int centerX = 150;
        int centerY = 100;

        parser = new DijkstraParser();
        cardGraph = new Graph(centerX, centerY);
    }

    /***  SETTERS ***/

    public void setGraphXHigh(int high) {
        this.graphXHigh = high;
    }

    public void setGraphXLow(int low) {
        this.graphXLow = low;
    }

    /*** GETTERS ***/


    /**
     * Returns the Graph X High
     * @return
     */
    public int getGraphXHigh() {
        return this.graphXHigh;
    }

    /**
     * Returns the Graph X Low
     * @return
     */
    public int getGraphXLow() {
        return this.graphXLow;
    }


    /*** OTHERS ***/

    @Override
    public void reset() {

        graphPane.getChildren().clear();
        graphPane.getChildren().add(cardGraph.createXYAxis(Color.BLACK, 300, 200));
        graphPane.getChildren().add( cardGraph.createGrid(Color.web(UIColors.GRAPH_BGND), 300, 200 ));
        cardGraph.axisNums(graphPane, SceneCntl.getWd(), 200);
    }


    @Override
    public VBox getCalcPane() {

        VBox vBox = super.getCalcPane();
        vBox.setSpacing(2);

        init();

        expressionTArea.setPromptText("Enter Math Formulas as \"y = ...\". Each graph starts with a new line." +
                "\n example:" +
                "\n y = x ^ 2 " +
                "\n y = 2 * x ^ 2 - 2");

        FlowPane flow = new FlowPane();
        graphPane.getChildren().add(cardGraph.createXYAxis(Color.BLACK, 300, 200));
        graphPane.getChildren().add( cardGraph.createGrid(Color.web(UIColors.GRID_GREY), 300, 200));
        cardGraph.axisNums(graphPane, SceneCntl.getWd(), 200);
        graphPane.setOnMouseClicked((e) -> graphClickAction());

        flow.getChildren().addAll(graphPane, userSettingsBox());

        flow.setMaxHeight(226);
        flow.setVgap(4);
        flow.setAlignment(Pos.CENTER);

        vBox.getChildren().add(0, flow);

        return vBox;
    }


    private HBox userSettingsBox() {

        HBox box = new HBox(2);
        TextField xLow = new TextField();
        xLow.setPrefColumnCount(4);

        xLow.requestFocus();
        TextField xHigh = new TextField();
        xHigh.setPrefColumnCount(4);
        TextField yLow = new TextField();
        yLow.setPrefColumnCount(4);
        TextField yHigh = new TextField();
        yHigh.setPrefColumnCount(4);
        xLow.setId("graph_settings_border");
        xLow.setPromptText("xLow");
        xHigh.setId("graph_settings_border");
        xHigh.setPromptText("xHigh");
        yLow.setId("graph_settings_border");
        yLow.setPromptText("yLow");
        yHigh.setId("graph_settings_border");
        yHigh.setPromptText("yHigh");
        box.getChildren().clear();
        box.getChildren().addAll(xLow, xHigh, yLow, yHigh);
        return box;
    }



    /**
     * Splits a text area with multiple expressions into seperate expressions.
     * Searches for the new line or "\n".
     * @param exps
     * @return
     */
    private String[] splitExps(String exps) {

        return exps.split("\n");
    }


    /*******************************************************************************************************************
     *                                                                                                                 *
     *                                            EXPRESSION CALC                                                      *
     *                                                                                                                 *
     ******************************************************************************************************************/



    /**
     * Recieves the expression and inserts it into an existing graph.
     * @param exp   The expression
     * @param xLowLim The low X limit
     * @param xHighLim The high X limit
     * @param graphP The pane the graph exists in
     * @return
     */
    public Pane graphExpression(String exp, double xLowLim, double xHighLim, double yLowLim, double yHighLim,
                                Pane graphP, int width, int height)
    {

        SVGPath path = new SVGPath();
        Canvas graphCanvas = new Canvas();
        graphCanvas.setHeight(height);
        graphCanvas.setWidth(width);
        // clip the graph edges to prevent any bleed over
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(graphCanvas.getLayoutX());
        clip.setLayoutY(graphCanvas.getLayoutY());
        graphCanvas.setStyle("-fx-background-color: white");

        path.setFill(Color.TRANSPARENT);
        path.setStroke(Color.BLUEVIOLET);

        path.setContent(graphFunction(exp, (int) xLowLim, (int) xHighLim, (int) yLowLim, (int) yHighLim, width / 2, height / 2));

        graphP.getChildren().add(path);

        graphP.setClip(clip);
        return graphP;
    }

    /**
     * Creates an svgPath string from a y = function.
     *  Expects that y = is not included in the expression.
     *  Executes y = x as per expression. X begins at xStart and
     *  ends at xEnd. Calculations beyond yLowLim or yHighLim are
     *  excluded. YlowLim and yHighLim are the high and low bounderies.
     *  Low being the top or 0  and high being the bottom or 300 by default.
     *  Bounderies are the height and width in pixels divided by 10 or the height
     *  in pixels between each tickmark on the x and y axis.
     * @param expression
     * @param xStart
     * @param xEnd
     * @param yLowLim upper pane boundry, 0 by default
     * @param yHighLim Lower pane boundry, 300 by default
     * @return
     */
    private String graphFunction(String expression, int xStart, int xEnd, int yLowLim, int yHighLim, int centerX, int centerY) {

        System.out.println(" *!*!* IN graphFunction() !*!*!");

        int length = xEnd - xStart;
        length *= 5;
        System.out.println( " length: " + length);

        double y;
        double x = xStart;

        double xCoord;
        double yCoord;

        StringBuilder sb = new StringBuilder();

        sb.append("M");

        for(int i = xStart; i < length; i++) {

            int index = 0;
            x += .2;
            y = evaluate(expression, x) * -1;

            yCoord = (y * 20) + centerY;
            xCoord = (x * 20) + centerX;

            if (yCoord < yHighLim && yCoord > yLowLim) {

                sb.append( xCoord + " " + yCoord);

                if (i < length - 1) {
                    sb.append(", L");
                }
            } else if(sb.toString().charAt(sb.length() - 1) == 'L') {
                index = sb.lastIndexOf("L");

                sb.replace(index , index + 1,"M");
            }
        }
        if(sb.toString().charAt(sb.length() - 1) == 'M') {
            int index = sb.lastIndexOf("M");
            sb.replace(index, index + 1, "");
        }

        return sb.toString();
    }

    /**
     * Helper to graphFUnction, evaluates an expression and replaces
     * "x" in the expression with the parameter x
     * x for this iteration.
     * @param expression
     * @param x
     * @return
     */
    private double evaluate(String expression, double x) {

        ArrayList subExpList;

        try {
            subExpList = parse(expression, x);
            parser.parseIntoRPN(subExpList);
        } catch (Exception e) {
            // @ todo Handle operator bad input exception in GraphCard line 401
        }

        if(parser.isInvalidInput()) {

            StringBuilder sb = new StringBuilder();
            sb.append(expression);
            sb.append("\n\n " + parser.getErrorMessage());
            expressionTArea.setText(sb.toString());

            return 0;

        } else {

            return parser.execute(DijkstraParser.getOutQueue());
        }
    }

    /**
     * Helper to evaluate
     * Parses the expression and adds a value for x
     * @param express The string expression
     * @param x
     * @return
     */
    private ArrayList parse(String express, double x) throws Exception {

        String expMinus = express.substring(3);
        expMinus = expMinus.trim();
        String element;
        String[] elements = expMinus.split(" ");
        ArrayList subExpList = new ArrayList(10);
        String strX = "";
        String strY = "";

        for(int i = 0; i < elements.length; i++) {

            element = elements[i];

            int length = element.length();
            char c;

            if (length > 1) {
                c = element.charAt(1);
            } else {
                c = element.charAt(0);
            }
            if (Character.isDigit(c)) {
                double num = Double.parseDouble(element);
                subExpList.add(num);

            } else if (c == 'x') {

                if (element.charAt(0) == '-') {
                    subExpList.add(-x);
                } else {
                    subExpList.add(x);
                }

            } else { // it's an operator

                OperatorInterface operator = DijkstraParser.getOperator(element);

                if (i < 0) {
                    System.err.println("ERROR: There are not enough numbers for the " + operator.getSymbol() +
                            " operation. \nCheck the format of the expression.");
                    if (operator.getPriority() != 0) {
                        throw new EmptyStackException();
                    }
                }
                if(operator.isUnaryOp()) {


                    strX = elements[i + 1];

                } else if(i > 1 && i < elements.length - 1) {

                    strX = elements[i - 1];
                    strY = elements[i + 1];

                } else {
                    if (operator.getPriority() != 0) {
//                        throw new EmptyStackException();
                    }
                }

                String strOrigSubExp = parser.getStrExpr(strX, strY, operator);
                ExpNode exp = new ExpNode(operator, strOrigSubExp, i);
                subExpList.add(exp);
            }
        }
        return subExpList;
    }


    /*******************************************************************************************************************
     *                                                                                                                 *
     *                                                  BUTTON ACTIONS                                                 *
     *                                                                                                                 *
     ******************************************************************************************************************/


    /**
     * Create the table of x & y =
     * @param expression
     */
    @Override
    public void calcButtonAction(String expression, TextArea textArea) {

        System.out.println("calcButtonAction in GraphCard");

        reset();
        String[] exps = splitExps(expression);

        for(int i = 0; i < exps.length; i++) {
            graphPane = graphExpression( exps[i], graphXLow, graphXHigh, graphYLow, graphYHigh, graphPane, 300, 200);
        }

    }

    /**
     * Creates a large graph 3/4ths the screen size
     */
    private void graphClickAction() {

        LargeGraphPane lgPane = new LargeGraphPane();
        Stage graphStage = new Stage();

        lgPane.start(graphStage);

    }


    /**
     * INNER CLASS
     * Creates a large garph area 3/4 the size of the screen
     */
    public class LargeGraphPane extends Application {

        @Override
        public void start(Stage graphStage) {

            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            double height = screensize.getHeight();
            height -= 40;
            double ratio = height / 300;
            int centerX = (int) height / 2;
            int centerY = centerX;
            Graph largeGraph = new Graph(centerX, centerY);

            System.out.println("\n\n *** ratio: " + ratio + " ***" +
                    "\n height " + height);

            graphStage.setWidth(height);
            graphStage.setHeight(height);
            Pane pane = new Pane();

            pane.getChildren().add( largeGraph.createXYAxis(Color.BLACK, height, height));
            pane.getChildren().add( largeGraph.createGrid(Color.web(UIColors.GRID_GREY), height, height ));

            largeGraph.axisNums( pane, height, height );
            String[] exps = splitExps(expression);

            for(String e : exps) {
                pane = graphExpression(
                        e,
                        graphXLow * ratio,
                        graphXHigh * ratio,
                        graphYLow * ratio,
                        graphYHigh* ratio,
                        pane,
                        (int) height,
                        (int) height);
            }

            Scene graphScene = new Scene(pane, height, height, Color.web(UIColors.FM_WHITE));
            //graphScene.getStylesheets().add("css/newCascadeStyleSheet.css");
            graphStage.setScene(graphScene);
            graphStage.show();
        }
    }
}
