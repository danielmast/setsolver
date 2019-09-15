package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import nl.danielmast.setsolver.card.CardSet;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static nl.danielmast.setsolver.card.Color.*;
import static nl.danielmast.setsolver.card.Filling.*;
import static nl.danielmast.setsolver.card.Filling.EMPTY;
import static nl.danielmast.setsolver.card.Number.*;
import static nl.danielmast.setsolver.card.Number.TWO;
import static nl.danielmast.setsolver.card.Shape.*;
import static nl.danielmast.setsolver.card.Shape.WAVE;
import static org.junit.jupiter.api.Assertions.*;

class SetFinderTest {

    @Test
    void testFindSets() {
        Set<Card> cards = new HashSet<>(12);
        cards.add(new Card(TWO, PURPLE, RECTANGLE, HALF));
        cards.add(new Card(ONE, RED, WAVE, EMPTY));
        cards.add(new Card(THREE, GREEN, OVAL, HALF));
        cards.add(new Card(THREE, PURPLE, RECTANGLE, EMPTY));
        cards.add(new Card(ONE, PURPLE, WAVE, FULL));
        cards.add(new Card(THREE, RED, WAVE, FULL));
        cards.add(new Card(TWO, GREEN, RECTANGLE, FULL));
        cards.add(new Card(THREE, RED, RECTANGLE, FULL));
        cards.add(new Card(THREE, PURPLE, WAVE, HALF));
        cards.add(new Card(TWO, RED, RECTANGLE, HALF));
        cards.add(new Card(TWO, PURPLE, OVAL, FULL));
        cards.add(new Card(TWO, GREEN, WAVE, EMPTY));

        Set<CardSet> expectedSets = new HashSet<>(2);
        expectedSets.add(
            new CardSet(
                new Card(THREE, RED, WAVE, FULL),
                new Card(THREE, PURPLE, RECTANGLE, EMPTY),
                new Card(THREE, GREEN, OVAL, HALF)
            )
        );
        expectedSets.add(
            new CardSet(
                new Card(TWO, RED, RECTANGLE, HALF),
                new Card(TWO, GREEN, WAVE, EMPTY),
                new Card(TWO, PURPLE, OVAL, FULL)
            )
        );

        Set<CardSet> sets = SetFinder.findSets(cards);

        assertEquals(expectedSets.size(), sets.size(), "Wrong size");
        assertAll(expectedSets.stream()
            .map(expectedSet ->
                () -> assertTrue(sets.contains(expectedSet), "Set not found: " + expectedSet.toString())));
    }

    @Test
    void findSetsNoCards() {
        Set<Card> cards = new HashSet<>();
        Set<CardSet> sets = SetFinder.findSets(cards);
        assertEquals(0, sets.size(), "Wrong size");
    }
}