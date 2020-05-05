package controller;

import ai.BotTest;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class TestController {

    @FXML
    ProgressBar progressBar;
    @FXML
    TextField testAmountField;
    @SuppressWarnings("rawtypes")
	@FXML
    BarChart maxOperationChart;

    /*
    percentage of test progress
     */
    DoubleProperty progress = new SimpleDoubleProperty(0);


    public TestController() {

    }

    @FXML
    public void startTest() {
        try {
            //get amount of test variations per environment
            int vars = Integer.parseInt(testAmountField.getText());
            BotTest test = new BotTest(vars);
            test.runTest();
            displayResult(test);
        } catch (NumberFormatException e) {
            testAmountField.setText("enter integer");
        }
    }

    public void displayResult(BotTest test){
        //TODO next
        maxOperationChart.getData();
    }


}
