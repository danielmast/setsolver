package nl.danielmast.setsolver;

import org.opencv.core.Scalar;

public class SetColors {
    private static final Scalar[] colors = {
            // https://en.wikipedia.org/wiki/Help:Distinguishable_colors
            new Scalar(220, 117, 0), // Blue
            new Scalar(72, 206, 43), // Green
            new Scalar(16, 0, 255), // Red
            new Scalar(0, 255, 255), // Yellow
            new Scalar(5, 164, 255), // Orpiment (orange)
            new Scalar(255, 163, 240), // Amethyst (pink)
            new Scalar(0, 63, 153), // Caramel (brown)
            new Scalar(92, 0, 76), // Damson (purple)
            new Scalar(25, 25, 25), // Ebony (black)
            new Scalar(49, 92, 0), // Forest (dark green)
            new Scalar(153, 204, 255), // Honeydew (beige)
            new Scalar(128, 128, 128), // Iron (gray)
            new Scalar(181, 255, 148), // Jade
    };

    public static Scalar getColor(int c) {
        return colors[c];
    }
}
