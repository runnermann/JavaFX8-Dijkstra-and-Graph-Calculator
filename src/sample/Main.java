package sample;

import controller.SceneCntl;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.Editor;
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
        btnHBox.getChildren().setAll(calcButton, graphButton);

        BorderPane bPane = new BorderPane();
        bPane.setTop(btnHBox);

        MathCard mathCard = new MathCard();
        GraphCard graphCard = new GraphCard();
        Editor u = new Editor(); // upper editor
        Editor l = new Editor(); // lower editor
        bPane.setCenter(mathCard.getTEditorPane(u, l));

        calcButton.setOnAction(event -> {
            u.tCell.getTextArea().setText("");
            bPane.setCenter(mathCard.getTEditorPane(u, l));

        });

        graphButton.setOnAction(event -> {
            u.tCell.getTextArea().setText("");
            bPane.setCenter(graphCard.getTEditorPane(u, l));

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
