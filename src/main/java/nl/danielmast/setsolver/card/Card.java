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

    public String toFilename() {
        StringBuilder sb = new StringBuilder();
        for (Feature feature : features) {
            sb.append(toOneCharacter(feature));
        }
        sb.append(".jpg");
        return sb.toString();
    }

    private String toOneCharacter(Feature feature) {
        if (feature instanceof Number) {
            switch ((Number)feature) {
                case ONE:
                    return "1";
                case TWO:
                    return "2";
                case THREE:
                    return "3";
            }
        } else if (feature instanceof Color) {
            switch ((Color) feature) {
                case RED:
                    return "r";
                case GREEN:
                    return "g";
                case PURPLE:
                    return "p";
            }
        } else if (feature instanceof Shape) {
            switch ((Shape)feature) {
                case RECTANGLE:
                    return "r";
                case OVAL:
                    return "o";
                case WAVE:
                    return "w";
            }
        } else if (feature instanceof Filling) {
            switch ((Filling)feature) {
                case FULL:
                    return "f";
                case EMPTY:
                    return "e";
                case HALF:
                    return "h";
            }
        }

        throw new RuntimeException("Unexpected feature");
    }
}
