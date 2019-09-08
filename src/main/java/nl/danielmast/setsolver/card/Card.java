package nl.danielmast.setsolver.card;

import java.util.Arrays;
import java.util.Objects;

public class Card {
    private Feature[] features;

    public Card(Number number, Color color, Shape shape, Filling filling) {
        features = new Feature[]{number, color, shape, filling};
    }

    public Feature[] getFeatures() {
        return features;
    }

    @Override
    public int hashCode() {
        return Objects.hash(features[0], features[1], features[2], features[3]);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card)) {
            return false;
        }
        return Arrays.equals(features, ((Card)o).features);
    }

    @Override
    public String toString() {
        return String.format("%s_%s_%s_%s", features[0], features[1], features[2], features[3]);
    }
}
