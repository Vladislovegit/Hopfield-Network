package sample;

import imageProcessing.ImageLoader;
import imageProcessing.ImageProcessing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;


public class Main extends Application {

    public TextField firstImageTxtField;
    public TextField thirdImageTxtField;
    public TextField secondImageTxtField;
    public Button teachBtn;
    public TextField recognizingImageTxtField;
    public TextField noisePercentageTxtField;
    public Button recognizeBtn;
    public Button calculateMaxNoiseBtn;
    public Text recognizingStatus;
    public Text teachingStatus;
    public Text maxNoisePercentage;

    private HopfieldNetwork network;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_view.fxml"));
        fxmlLoader.setController(this);
        primaryStage.setTitle("Hopfield Network");
        primaryStage.setScene(new Scene(fxmlLoader.load(), 300, 260));
        primaryStage.show();

        teachBtn.setOnMouseClicked(event -> onTeachBtnClick());
        recognizeBtn.setOnMouseClicked(event -> onRecognizeBthClick());
        calculateMaxNoiseBtn.setOnMouseClicked(event -> onCalculateMaxNoiseBtnClick());
    }

    private void onTeachBtnClick() {
        ArrayList<Integer[]> images = new ArrayList<>();
        Integer[][] image = ImageProcessing.getImage(firstImageTxtField.getText());
        images.add(ImageProcessing.getBipolarVector(image));
        image = ImageProcessing.getImage(secondImageTxtField.getText());
        images.add(ImageProcessing.getBipolarVector(image));
        image = ImageProcessing.getImage(thirdImageTxtField.getText());
        images.add(ImageProcessing.getBipolarVector(image));

        network = new HopfieldNetwork(images);
        teachingStatus.setText("Success");
        recognizeBtn.setDisable(false);
    }

    private void onRecognizeBthClick() {
        Integer percentageOfNoise = Integer.parseInt(noisePercentageTxtField.getText());

        String imageName = recognizingImageTxtField.getText();
        Integer[][] image = ImageProcessing.getImage(imageName);

        Integer width = image.length;
        Integer height = image[0].length;
        Integer[] imageWithNoise = ImageProcessing.applyNoise(ImageProcessing.getImageVector(image), percentageOfNoise);

        ImageLoader.save(imageWithNoise,
                "images/with_noise/" + imageName + "_" + percentageOfNoise + "%",
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        );

        Integer[][] res = ImageProcessing.getImageValuesFromBipolarVector(
                network.recognize(ImageProcessing.getBipolarVector(imageWithNoise)),
                width,
                height
        );

        ImageLoader.save(
                res,
                "images/res/" + imageName + "_" + percentageOfNoise + "%",
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        );

        recognizingStatus.setText("Success");
        calculateMaxNoiseBtn.setDisable(false);
    }

    private void onCalculateMaxNoiseBtnClick() {
        String imageName = recognizingImageTxtField.getText();
        Integer[][] image = ImageProcessing.getImage(imageName);
        Integer[] originalImageVector = ImageProcessing.getImageVector(image);
        Integer[] originalBipolarVector = ImageProcessing.getBipolarVector(image);
        Integer[] recognizedImageVector, recognizedBipolarVector;
        for (int i = 1; i < 100; i++) {
            recognizedImageVector = ImageProcessing.applyNoise(originalImageVector, i);
            recognizedBipolarVector = network.recognize(ImageProcessing.getBipolarVector(recognizedImageVector));
            if (!Arrays.equals(originalBipolarVector, recognizedBipolarVector)) {
                maxNoisePercentage.setText((--i) + "%");
                break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

