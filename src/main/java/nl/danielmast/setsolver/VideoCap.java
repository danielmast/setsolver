package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import nl.danielmast.setsolver.card.CardSet;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2RGB;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class VideoCap {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    private FrameCombiner frameCombiner = new FrameCombiner();

    VideoCap(){
        cap = new VideoCapture();
        cap.open(0);
        cap.set(Videoio.CAP_PROP_FRAME_WIDTH, 1280);
        cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 960);
    }

    long minDiff = Long.MAX_VALUE;
    long maxDiff = 0;
    long totalDiff = 0;
    long c = 1;
    BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);

        Mat original = mat2Img.mat;

        Mat rgb = new Mat();
        cvtColor(original, rgb, COLOR_BGR2RGB);

        long start = System.currentTimeMillis();

        frameCombiner.update(rgb);
        Map<Card, Point> cards = frameCombiner.getCards();
        System.out.println(String.format("%d Cards: %s", cards.size(), cards));

        long end = System.currentTimeMillis();
        long diff = end - start;
        minDiff = Math.min(minDiff, diff);
        maxDiff = Math.max(maxDiff, diff);
        totalDiff += diff;
        System.out.println(String.format("Diff: %dms, Avg: %d, Min: %d, Max: %d", diff, totalDiff / c++, minDiff, maxDiff));

        Set<CardSet> sets = SetFinder.findSets(cards.keySet());
        System.out.println(String.format("%d Sets: %s", sets.size(), sets));

        Mat result = ImageProcessor.drawSets(original, sets, cards);
        return mat2Img.getImage(result);
    }
}