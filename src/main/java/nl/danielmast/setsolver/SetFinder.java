package nl.danielmast.setsolver;

import nl.danielmast.setsolver.card.Card;
import nl.danielmast.setsolver.card.CardSet;

import java.util.HashSet;
import java.util.Set;

public class SetFinder {
    public static Set<CardSet> findSets(Set<Card> cards) {
        Card[] c = cards.toArray(new Card[]{});

        Set<CardSet> sets = new HashSet<>();

        for (int i = 0; i < c.length - 2; i++) {
            for (int j = i+1; j < c.length - 1; j++) {
                for (int k = j+1; k < c.length; k++) {
                    if (CardSet.isSet(c[i], c[j], c[k])) {
                        sets.add(new CardSet(c[i], c[j], c[k]));
                    }
                }
            }
        }

        return sets;
    }
}
