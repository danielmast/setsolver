package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import nl.danielmast.setsolver.card.CardSet;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.opencv.imgproc.Imgproc.*;

public class ImageProcessor {
    static final int            RESIZE_WIDTH = 1280;
    private static final int    BLUR_SIZE = 5;
    private static final int    CARD_THRESHOLD = 140;
    private static final double APPROXIMATION_FACTOR = 0.02;
    private static final double MIN_RATIO_OF_MAX_AREA = 0.3;
    private static final int    DRAW_LIMIT = 13;
    private static final int    CARD_WIDTH = 60;
    private static final int    CARD_HEIGHT = 90;

    public static Mat resize(Mat image, int resizeWidth) {
        Mat resized = new Mat();
        if (image.width() > resizeWidth) {
            resized = new Mat();
            Imgproc.resize(image, resized, new Size(
                    resizeWidth,
                    image.rows() * resizeWidth / image.cols()
            ));
        } else {
            image.copyTo(resized);
        }

        return resized;
    }

    public static Mat drawCardNames(Mat image, Map<Card, Point> cards) {
        Mat drawn = new Mat();
        image.copyTo(drawn);

        cards.forEach((card, point) -> {
            Size size = getTextSize(card.toString(), FONT_HERSHEY_PLAIN, 0.6, 1, null);
            Point aligned = new Point(
                    point.x - (size.width / 2),
                    point.y - (size.height / 2)
            );

            Imgproc.putText(drawn, card.toString(), aligned,
                    FONT_HERSHEY_PLAIN, 0.6, new Scalar(255,0,0));
        });
        return drawn;
    }

    private static int colorCounter = 0;
    public static Mat drawSets(Mat image, Set<CardSet> sets, Map<Card, Point> cards) {
        Mat drawn = new Mat();
        image.copyTo(drawn);
        colorCounter = 0;
        sets.stream()
                .limit(DRAW_LIMIT)
                .forEach(set -> {
                    Scalar color = SetColors.getColor(colorCounter);
                    line(drawn, cards.get(set.getCard1()), cards.get(set.getCard2()), color, 4, LINE_AA);
                    line(drawn, cards.get(set.getCard2()), cards.get(set.getCard3()), color, 4, LINE_AA);
                    line(drawn, cards.get(set.getCard3()), cards.get(set.getCard1()), color, 4, LINE_AA);
                    colorCounter++;
                });
        return drawn;
    }

    public static Map<Card, Point> getCards(Mat image) {
        Mat rgb = new Mat();
        cvtColor(image, rgb, COLOR_BGR2RGB);

        Mat gray = new Mat();
        cvtColor(rgb, gray, COLOR_RGB2GRAY);

        Mat normalized = new Mat();
        Core.normalize(gray, normalized, 0.0, 255.0, Core.NORM_MINMAX);

        Mat blurred = new Mat();
        blur(normalized, blurred, new Size(BLUR_SIZE, BLUR_SIZE));

        Mat thresholded = new Mat();
        threshold(blurred, thresholded, CARD_THRESHOLD, 255, THRESH_BINARY);

        List<MatOfPoint> cardContours = getCardContours(thresholded);

        String debugfile = "target/contours.jpg";
        Mat debugImage = new Mat();
        rgb.copyTo(debugImage);
        IntStream.range(0, cardContours.size()).forEach(i ->
            drawContours(debugImage, cardContours, i, new Scalar(255, 0, 0), 2));
        cvtColor(debugImage, debugImage, COLOR_RGB2BGR);
        Imgcodecs.imwrite(debugfile, debugImage);

        Map<Mat, Point> cardImagesAndPositions = cardContours.parallelStream()
                .collect(Collectors.toMap(
                        c -> getCardImage(rgb, c),
                        ImageProcessor::getCenter
                ));

        List<Mat> cardImages = new ArrayList<>(cardImagesAndPositions.size());
        List<Point> points = new ArrayList<>(cardImagesAndPositions.size());
        for (Map.Entry<Mat, Point> entry : cardImagesAndPositions.entrySet()) {
            cardImages.add(entry.getKey());
            points.add(entry.getValue());
        }

        List<Card> cards = Classifier.getInstance().classify(cardImages);

        /* TODO A duplicate key exception occurs when classification contains duplicate cards.
            * We have to decide how to handle this.
        */
        return IntStream.range(0, cards.size())
                .boxed()
                .collect(Collectors.toMap(cards::get, points::get));
    }

    private static List<MatOfPoint> getCardContours(Mat thresholded) {
        List<MatOfPoint> contours = new ArrayList<>();

        // Get all (external) contours
        findContours(thresholded, contours, new Mat(), RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        // Get all approximated contours
        List<MatOfPoint> approximations = contours.stream()
                .map(ImageProcessor::getApproximation)
                .collect(Collectors.toList());

        // Get the largest contour area
        double maxArea = approximations.stream()
                .map(Imgproc::contourArea)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new RuntimeException("No approximations"));

        // Filter the largest contours
        // This should remain only the contours of the cards
        return approximations.stream()
                .filter(a -> a.size(0) == 4)
                .filter(a -> contourArea(a) > maxArea * MIN_RATIO_OF_MAX_AREA)
                .collect(Collectors.toList());
    }

    private static MatOfPoint getApproximation(MatOfPoint contour) {
        MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
        approxPolyDP(contour2f, contour2f, arcLength(contour2f, true) * APPROXIMATION_FACTOR,true);
        return new MatOfPoint(contour2f.toArray());
    }

    private static Point getCenter(MatOfPoint contour) {
        Moments m = moments(contour);
        return new Point(
                m.get_m10() / m.get_m00(),
                m.get_m01() / m.get_m00()
        );
    }

    // For creating debug images in getCard()
    private static int cardCounter = 0;
    private static Mat getCardImage(Mat image, MatOfPoint contour) {
        Rect boundingRect = boundingRect(contour);
        int w = boundingRect.width;
        int h = boundingRect.height;

        MatOfPoint2f tempRect = new MatOfPoint2f();

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = 0, maxY = 0;
        for (Point p : contour.toArray()) {
            if (p.x < minX) minX = p.x;
            else if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            else if (p.y > maxY) maxY = p.y;
        }

        Point[] tlTrBlBr = getTopLeftTopRightBottomLeftBottomRight(contour);
        Point tl = tlTrBlBr[0];
        Point tr = tlTrBlBr[1];
        Point bl = tlTrBlBr[2];
        Point br = tlTrBlBr[3];

        if (w <= 0.8 * h) {
            tempRect.fromArray(tl, tr, br, bl);
        } else if (w >= 1.2 * h) {
            tempRect.fromArray(bl, tl, tr, br);
        } else if (w > 0.8 * h && w < 1.2 * h) {
            if (contour.get(1, 0)[1] <= contour.get(3, 0)[1]) {
                tempRect.fromArray(
                        new Point(contour.get(1, 0)),
                        new Point(contour.get(0, 0)),
                        new Point(contour.get(3, 0)),
                        new Point(contour.get(2, 0))
                );
            } else if (contour.get(1, 0)[1] > contour.get(3, 0)[1]) {
                tempRect.fromArray(
                        new Point(contour.get(0, 0)),
                        new Point(contour.get(3, 0)),
                        new Point(contour.get(2, 0)),
                        new Point(contour.get(1, 0))
                );
            }
        } else {
            throw new RuntimeException("Unexpected dimensions");
        }

        MatOfPoint2f dst = new MatOfPoint2f();
        dst.fromArray(
                new Point(0, 0),
                new Point(CARD_WIDTH-1, 0),
                new Point(CARD_WIDTH-1, CARD_HEIGHT-1),
                new Point(0, CARD_HEIGHT-1)
        );

        Mat m = getPerspectiveTransform(tempRect, dst);

        Mat cardImage = new Mat();
        warpPerspective(image, cardImage, m, new Size(CARD_WIDTH, CARD_HEIGHT));

        String debugfile = String.format("target/debug/card%d.jpg", cardCounter++);
        Mat debugImage = new Mat();
        cvtColor(cardImage, debugImage, COLOR_RGB2BGR);
        Imgcodecs.imwrite(debugfile, debugImage);

        return cardImage;
    }

    private static Point[] getTopLeftTopRightBottomLeftBottomRight(MatOfPoint pts) {
        Point[] result = new Point[4];

        List<Point> sortedByX = pts.toList()
                .stream()
                .sorted(Comparator.comparing(p -> p.x))
                .collect(Collectors.toList());

        List<Point> sortedByY = pts.toList()
                .stream()
                .sorted(Comparator.comparing(p -> p.y))
                .collect(Collectors.toList());

        for (Point p : pts.toArray()) {
            if (sortedByX.get(0).equals(p) || sortedByX.get(1).equals(p)) {
                if (sortedByY.get(0).equals(p) || sortedByY.get(1).equals(p)) {
                    result[0] = p; // top left
                } else {
                    result[2] = p; // bottom left
                }
            } else {
                if (sortedByY.get(0).equals(p) || sortedByY.get(1).equals(p)) {
                    result[1] = p; // top right
                } else {
                    result[3] = p; // bottom right
                }
            }
        }
        return result;
    }
}
