package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.*;

public class FrameCombiner {
    private static final int NUMBER_OF_FRAMES = 5;
    private static final double THRESHOLD = 0.5;

    private Queue<Mat> frames = new LinkedList<>();
    private Map<Mat, Set<Card>> cardsPerFrame = new HashMap<>();
    private Map<Card, Set<Mat>> framesPerCard = new HashMap<>();
    private Map<Card, Point> lastLocation = new HashMap<>();

    public void update(Mat frame) {
        Map<Card, Point> cards;
        try {
            cards = ImageProcessor.getCards(frame);
        } catch (ClassificationException e) {
            return;
        }

        if (frames.size() == NUMBER_OF_FRAMES) {
            removeOldestFrame();
        }

        addFrame(frame, cards);
    }

    public void addFrame(Mat frame, Map<Card, Point> cards) {
        frames.add(frame);

        cardsPerFrame.put(frame, cards.keySet());

        for (Card card : cards.keySet()) {
            Set<Mat> fpc = framesPerCard.computeIfAbsent(card, c -> new HashSet<>());
            fpc.add(frame);
            lastLocation.put(card, cards.get(card));
        }
    }

    public void removeOldestFrame() {
        Mat oldestFrame = frames.remove();

        for (Card card : cardsPerFrame.get(oldestFrame)) {
            this.framesPerCard.get(card).remove(oldestFrame);
        }

        cardsPerFrame.remove(oldestFrame);
    }

    public Map<Card, Point> getCards() {
        Map<Card, Point> result = new HashMap<>();

        for (Card card : framesPerCard.keySet()) {
            if (framesPerCard.get(card).size() > THRESHOLD * frames.size()) {
                result.put(card, lastLocation.get(card));
            }
        }

        return result;
    }
}