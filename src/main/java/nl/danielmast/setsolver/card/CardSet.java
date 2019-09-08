package nl.danielmast.setsolver.card;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CardSet {
    private Card[] cards;

    public CardSet(Card card1, Card card2, Card card3) {
        cards = new Card[]{card1, card2, card3};
    }

    public Card getCard1() {
        return cards[0];
    }

    public Card getCard2() {
        return cards[1];
    }

    public Card getCard3() {
        return cards[2];
    }

    public static boolean isSet(Card card1, Card card2, Card card3) {
        for (int f = 0; f < 4; f++) {
            if (!isSet(card1.getFeatures()[f], card2.getFeatures()[f], card3.getFeatures()[f])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSet(Feature feature1, Feature feature2, Feature feature3) {
        if (feature1.equals(feature2)) {
            return feature1.equals(feature3);
        }
        return !feature1.equals(feature3) && !feature2.equals(feature3);
    }

    @Override
    public int hashCode() {
        return toJavaSet().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CardSet)) {
            return false;
        }

        CardSet other = (CardSet) o;
        return toJavaSet().equals(other.toJavaSet());
    }

    private Set<Card> toJavaSet() {
        return new HashSet<>(Arrays.asList(cards));
    }

    @Override
    public String toString() {
        return String.format("Set(%s, %s, %s)", cards[0], cards[1], cards[2]);
    }
}
