package model;

import model.shapes.*;
import javafx.scene.paint.Color;

public class PixelFactory {
    public enum PixelType {
        POINT, CIRCLE, SQUARE
    }

    public static Pixel createPixel(PixelType type, double x, double y, Color color) {
        switch (type) {
            case POINT:
                return new Point(x, y, color, 4.0);
            case CIRCLE:
                return new Circle(x, y, color, 30.0);
            case SQUARE:
                return new Square(x, y, color, 60.0);
            default:
                throw new IllegalArgumentException("Неизвестный тип: " + type);
        }
    }
}