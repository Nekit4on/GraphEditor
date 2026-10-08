package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Point extends Pixel implements Serializable {
    private double size;

    public Point(double x, double y, Color color, double size) {
        super(color, x, y);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;
        gc.setFill(color);
        gc.fillOval(x, y, size, size);
    }

    @Override
    public boolean contains(double px, double py) {
        return px >= x && px <= x + size && py >= y && py <= y + size;
    }

    @Override
    public Point copy() {
        Point copy = new Point(x, y, color, size);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        return copy;
    }

    @Override
    public void resize(double factor) {
        size *= factor;
        if (size < 1) size = 1;
        if (size > 20) size = 20;
    }

    public double getSize() {
        return size;
    }
}
