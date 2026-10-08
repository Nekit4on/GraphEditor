package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Dot extends Shape implements Serializable {
    private double size;

    public Dot(Color color, double x, double y, double size) {
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
    public Shape copy() {
        Dot copy = new Dot(color, x, y, size);
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
}