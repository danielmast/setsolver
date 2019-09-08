package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import nl.danielmast.setsolver.card.CardSet;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Map;
import java.util.Set;

import static nl.danielmast.setsolver.ImageProcessor.RESIZE_WIDTH;

public class Main {
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        String infile = "src/test/resources/table3.jpg";
        String outfile = "target/result.jpg";

        // Load the model
        Classifier.getInstance();

        Mat original = Imgcodecs.imread(infile);
        Mat resized = ImageProcessor.resize(original, RESIZE_WIDTH);

        Map<Card, Point> cards = ImageProcessor.getCards(resized);
        System.out.println(String.format("%d Cards: %s", cards.size(), cards));

        Set<CardSet> sets = SetFinder.findSets(cards.keySet());
        System.out.println(String.format("%d Sets:", sets.size()));
        System.out.println(sets);

        Mat result = ImageProcessor.drawSets(resized, sets, cards);
        Imgcodecs.imwrite(outfile, result);
    }
}