package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static nl.danielmast.setsolver.card.Color.*;
import static nl.danielmast.setsolver.card.Filling.*;
import static nl.danielmast.setsolver.card.Number.*;
import static nl.danielmast.setsolver.card.Shape.*;
import static org.junit.jupiter.api.Assertions.*;

public class ImageProcessorTest {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    @Test
    @DisplayName("Resize an image from which the width is larger than the resize width")
    void testResizeLargeImage() {
        Mat image = Imgcodecs.imread("src/test/resources/81cards_day.jpg");

        int resizeWidth = 1200;
        Mat resized = ImageProcessor.resize(image, resizeWidth);
        assertEquals(resizeWidth, resized.width());
    }

    @Test
    @DisplayName("Resize an image from which the width is smaller than the resize width")
    void testResizeSmallImage() {
        Mat image = Imgcodecs.imread("src/test/resources/table1.jpg");

        int resizeWidth = 1200;
        Mat resized = ImageProcessor.resize(image, resizeWidth);
        assertEquals(image.width(), resized.width());
    }

    @Test
    void testGetCardsTable1() {
        Mat image = Imgcodecs.imread("src/test/resources/table1.jpg");

        Set<Card> expectedCards = new HashSet<>(12);
        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, HALF));
        expectedCards.add(new Card(ONE, RED, WAVE, EMPTY));
        expectedCards.add(new Card(THREE, GREEN, OVAL, HALF));
        expectedCards.add(new Card(THREE, PURPLE, RECTANGLE, EMPTY));
        expectedCards.add(new Card(ONE, PURPLE, WAVE, FULL));
        expectedCards.add(new Card(THREE, RED, WAVE, FULL));
        expectedCards.add(new Card(TWO, GREEN, RECTANGLE, FULL));
        expectedCards.add(new Card(THREE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(THREE, PURPLE, WAVE, HALF));
        expectedCards.add(new Card(TWO, RED, RECTANGLE, HALF));
        expectedCards.add(new Card(TWO, PURPLE, OVAL, FULL));
        expectedCards.add(new Card(TWO, GREEN, WAVE, EMPTY));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable2() {
        Mat image = Imgcodecs.imread("src/test/resources/table2.jpg");

        Set<Card> expectedCards = new HashSet<>(12);
        expectedCards.add(new Card(THREE, GREEN, OVAL, HALF));
        expectedCards.add(new Card(ONE, RED, WAVE, EMPTY));
        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, FULL));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable3() {
        Mat image = Imgcodecs.imread("src/test/resources/table3.jpg");

        Set<Card> expectedCards = new HashSet<>(32);
        expectedCards.add(new Card(ONE, GREEN, WAVE, EMPTY));
        expectedCards.add(new Card(TWO, GREEN, RECTANGLE, FULL));
        expectedCards.add(new Card(TWO, RED, OVAL, FULL));
        expectedCards.add(new Card(ONE, RED, WAVE, EMPTY));

        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, EMPTY));
        expectedCards.add(new Card(THREE, PURPLE, WAVE, HALF));
        expectedCards.add(new Card(THREE, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(THREE, RED, OVAL, FULL));

        expectedCards.add(new Card(THREE, PURPLE, OVAL, FULL));
        expectedCards.add(new Card(THREE, RED, WAVE, FULL));
        expectedCards.add(new Card(THREE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, GREEN, WAVE, HALF));

        expectedCards.add(new Card(THREE, RED, OVAL, HALF));
        expectedCards.add(new Card(TWO, PURPLE, OVAL, FULL));
        expectedCards.add(new Card(ONE, RED, RECTANGLE, HALF));
        expectedCards.add(new Card(THREE, PURPLE, WAVE, EMPTY));

        expectedCards.add(new Card(TWO, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, RED, OVAL, HALF));
        expectedCards.add(new Card(TWO, RED, OVAL, EMPTY));
        expectedCards.add(new Card(THREE, PURPLE, OVAL, HALF));

        expectedCards.add(new Card(ONE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(THREE, GREEN, RECTANGLE, FULL));
        expectedCards.add(new Card(TWO, RED, WAVE, EMPTY));
        expectedCards.add(new Card(TWO, RED, RECTANGLE, EMPTY));

        expectedCards.add(new Card(ONE, RED, OVAL, EMPTY));
        expectedCards.add(new Card(THREE, RED, OVAL, EMPTY));
        expectedCards.add(new Card(TWO, PURPLE, WAVE, HALF));
        expectedCards.add(new Card(TWO, GREEN, WAVE, HALF));

        expectedCards.add(new Card(ONE, PURPLE, OVAL, HALF));
        expectedCards.add(new Card(THREE, GREEN, OVAL, FULL));
        expectedCards.add(new Card(THREE, GREEN, RECTANGLE, EMPTY));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, EMPTY));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable5() {
        Mat image = Imgcodecs.imread("src/test/resources/table5.jpg");

        Set<Card> expectedCards = new HashSet<>(10);
        expectedCards.add(new Card(ONE, RED, OVAL, FULL));
        expectedCards.add(new Card(TWO, RED, OVAL, FULL));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, RED, WAVE, FULL));
        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, HALF));
        expectedCards.add(new Card(THREE, PURPLE, OVAL, EMPTY));
        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, EMPTY));
        expectedCards.add(new Card(ONE, PURPLE, RECTANGLE, EMPTY));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, EMPTY));
        expectedCards.add(new Card(THREE, GREEN, WAVE, FULL));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable6() {
        Mat image = Imgcodecs.imread("src/test/resources/table6.jpg");

        Set<Card> expectedCards = new HashSet<>(10);
        expectedCards.add(new Card(ONE, RED, OVAL, FULL));
        expectedCards.add(new Card(TWO, RED, OVAL, FULL));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, RED, WAVE, FULL));
        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, HALF));
        expectedCards.add(new Card(THREE, PURPLE, OVAL, EMPTY));
        expectedCards.add(new Card(TWO, PURPLE, RECTANGLE, EMPTY));
        expectedCards.add(new Card(ONE, PURPLE, RECTANGLE, EMPTY));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, EMPTY));
        expectedCards.add(new Card(THREE, GREEN, WAVE, FULL));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable7() {
        Mat image = Imgcodecs.imread("src/test/resources/table7.jpg");

        Set<Card> expectedCards = new HashSet<>(16);
        expectedCards.add(new Card(THREE, PURPLE, OVAL, FULL));
        expectedCards.add(new Card(THREE, RED, WAVE, FULL));
        expectedCards.add(new Card(THREE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, GREEN, WAVE, HALF));
        expectedCards.add(new Card(THREE, RED, OVAL, HALF));
        expectedCards.add(new Card(TWO, PURPLE, OVAL, FULL));
        expectedCards.add(new Card(ONE, RED, RECTANGLE, HALF));
        expectedCards.add(new Card(THREE, PURPLE, WAVE, EMPTY));
        expectedCards.add(new Card(TWO, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, RED, OVAL, HALF));
        expectedCards.add(new Card(TWO, RED, OVAL, EMPTY));
        expectedCards.add(new Card(THREE, PURPLE, OVAL, HALF));
        expectedCards.add(new Card(ONE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(THREE, GREEN, RECTANGLE, FULL));
        expectedCards.add(new Card(TWO, RED, WAVE, EMPTY));
        expectedCards.add(new Card(TWO, RED, RECTANGLE, EMPTY));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable8() {
        Mat image = Imgcodecs.imread("src/test/resources/table8.jpg");

        Set<Card> expectedCards = new HashSet<>(15);
        expectedCards.add(new Card(TWO, RED, RECTANGLE, HALF));
        expectedCards.add(new Card(TWO, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(ONE, RED, RECTANGLE, EMPTY));
        expectedCards.add(new Card(THREE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, GREEN, WAVE, HALF));
        expectedCards.add(new Card(TWO, RED, WAVE, HALF));
        expectedCards.add(new Card(ONE, PURPLE, WAVE, EMPTY));
        expectedCards.add(new Card(THREE, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(TWO, GREEN, WAVE, HALF));
        expectedCards.add(new Card(THREE, PURPLE, WAVE, FULL));
        expectedCards.add(new Card(TWO, PURPLE, WAVE, FULL));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(ONE, PURPLE, OVAL, HALF));
        expectedCards.add(new Card(THREE, PURPLE, RECTANGLE, FULL));
        expectedCards.add(new Card(TWO, GREEN, WAVE, FULL));

        testGetCards(image, expectedCards);
    }

    @Test
    void testGetCardsTable9() {
        Mat image = Imgcodecs.imread("src/test/resources/table9.jpg");

        Set<Card> expectedCards = new HashSet<>(15);
        expectedCards.add(new Card(TWO, RED, RECTANGLE, HALF));
        expectedCards.add(new Card(TWO, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(ONE, RED, RECTANGLE, EMPTY));
        expectedCards.add(new Card(THREE, RED, RECTANGLE, FULL));
        expectedCards.add(new Card(ONE, GREEN, WAVE, HALF));
        expectedCards.add(new Card(TWO, RED, WAVE, HALF));
        expectedCards.add(new Card(ONE, PURPLE, WAVE, EMPTY));
        expectedCards.add(new Card(THREE, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(TWO, GREEN, WAVE, HALF));
        expectedCards.add(new Card(THREE, PURPLE, WAVE, FULL));
        expectedCards.add(new Card(TWO, PURPLE, WAVE, FULL));
        expectedCards.add(new Card(ONE, GREEN, RECTANGLE, HALF));
        expectedCards.add(new Card(ONE, PURPLE, OVAL, HALF));
        expectedCards.add(new Card(THREE, PURPLE, RECTANGLE, FULL));
        expectedCards.add(new Card(TWO, GREEN, WAVE, FULL));

        testGetCards(image, expectedCards);
    }

    private static void testGetCards(Mat image, Set<Card> expectedCards) {
        Map<Card, Point> result = ImageProcessor.getCards(image);
        Set<Card> cards = result.keySet();

        assertEquals(expectedCards.size(), cards.size(), "Wrong size");

        assertAll(expectedCards.stream()
            .map(expectedCard ->
                () -> assertTrue(cards.contains(expectedCard), "Card not found: " + expectedCard.toString())));
    }
}
