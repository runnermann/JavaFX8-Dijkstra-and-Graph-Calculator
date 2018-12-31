package Main;

import controller.SceneCntl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.GraphCard;
import view.MathCard;



public class Main extends Application {


    @Override
    public void start(Stage primaryStage)
    {

        Button calcButton = new Button("Calculator");
        Button graphButton = new Button("Graph");
        HBox btnHBox = new HBox();
        btnHBox.setAlignment(Pos.CENTER);
        btnHBox.setPadding(new Insets(2, 2, 2, 2));
        btnHBox.getChildren().setAll(calcButton, graphButton);

        BorderPane bPane = new BorderPane();
        bPane.setTop(btnHBox);

        MathCard mathCard = new MathCard();
        GraphCard graphCard = new GraphCard();

        Pane calcPane = new Pane();
        calcPane.getChildren().add(mathCard.getCalcPane());
        bPane.setCenter(calcPane);

        calcButton.setOnAction(event -> {

            calcPane.getChildren().clear();
            calcPane.getChildren().add(mathCard.getCalcPane());

        });

        graphButton.setOnAction(event -> {
            calcPane.getChildren().clear();
            calcPane.getChildren().add(graphCard.getCalcPane());
        });


        Scene mainScene = new Scene(bPane, SceneCntl.getWd(), SceneCntl.getHt());
        mainScene.getStylesheets().add("css/CalculatorStyleSheet.css");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
