package model.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point extends Pixel {
    private double size;

    public Point(double x, double y, Color color, double size) {
        super(x, y, color);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x, y, size, size);
    }

    @Override
    public boolean contains(double px, double py) {
        return px >= x && px <= x + size && py >= y && py <= y + size;
    }

    @Override
    public Pixel copy() {
        return new Point(x, y, color, size);
    }

    @Override
    public void resize(double factor) {
        this.size *= factor;
    }
}

