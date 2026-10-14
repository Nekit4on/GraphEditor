package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends Pixel {
    private double size;

    public Square(double x, double y, Color color, double size) {
        super(x, y, color);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x - size / 2, y - size / 2, size, size);
    }

    @Override
    public boolean contains(double px, double py) {
        return px >= x - size / 2 && px <= x + size / 2
            && py >= y - size / 2 && py <= y + size / 2;
    }

    @Override
    public Pixel copy() {
        return new Square(x, y, color, size);
    }

    @Override
    public void resize(double factor) {
        this.size *= factor;
    }
}
