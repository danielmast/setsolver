package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import nl.danielmast.setsolver.card.Color;
import nl.danielmast.setsolver.card.Filling;
import nl.danielmast.setsolver.card.Shape;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.danielmast.setsolver.card.Number.*;

public class Classifier {
    private static Classifier instance;

    private static final String modelFile = "src/main/resources/model.h5";
    private MultiLayerNetwork model;
    private NativeImageLoader loader;

    private Classifier() {
        try {
            model = KerasModelImport.
                    importKerasSequentialModelAndWeights(modelFile);
        } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
            throw new RuntimeException(e);
        }
        loader = new NativeImageLoader();
        // Warm up the loader. Otherwise the conversion of the first image to an INDArray will take much longer.
        loader.asMat(Nd4j.ones(1,1, 1));
    }

    public static Classifier getInstance() {
        if (instance == null) {
            instance = new Classifier();
        }
        return instance;
    }

    public List<Card> classify(List<Mat> images) {
        List<INDArray> imageMatricesList = images.stream()
                .map(i -> {
                    try {
                        INDArray matrix = loader.asMatrix(i);
                        // Maximize the contrast
                        matrix = matrix.sub(matrix.minNumber());
                        matrix = matrix.div(matrix.maxNumber());
                        return matrix;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        INDArray imageMatrices = Nd4j.vstack(imageMatricesList);

        INDArray predictions = model.output(imageMatrices);

        return IntStream.range(0, (int)predictions.size(0))
                .boxed()
                .map(predictions::getRow)
                .map(this::predictionToCard)
                .collect(Collectors.toList());
    }

    private Card predictionToCard(INDArray prediction) {
        prediction = prediction.reshape(4, 3);
        INDArray predictionMax = prediction.amax(1);
        double[][] preds = prediction.toDoubleMatrix();

        double[] maxes = predictionMax.toDoubleVector();

        Color color = null;
        if (maxes[0] == preds[0][0]) {
            color = Color.RED;
        } else if (maxes[0] == preds[0][1]) {
            color = Color.PURPLE;
        } else if (maxes[0] == preds[0][2]) {
            color = Color.GREEN;
        }

        nl.danielmast.setsolver.card.Number number = null;
        if (maxes[1] == preds[1][0]) {
            number = ONE;
        } else if (maxes[1] == preds[1][1]) {
            number = TWO;
        } else if (maxes[1] == preds[1][2]) {
            number = THREE;
        }

        Filling filling = null;
        if (maxes[2] == preds[2][0]) {
            filling = Filling.EMPTY;
        } else if (maxes[2] == preds[2][1]) {
            filling = Filling.HALF;
        } else if (maxes[2] == preds[2][2]) {
            filling = Filling.FULL;
        }

        Shape shape = null;
        if (maxes[3] == preds[3][0]) {
            shape = Shape.WAVE;
        } else if (maxes[3] == preds[3][1]) {
            shape = Shape.RECTANGLE;
        } else if (maxes[3] == preds[3][2]) {
            shape = Shape.OVAL;
        }

        return new Card(number, color, shape, filling);
    }

}