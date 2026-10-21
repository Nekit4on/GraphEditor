package model.shapes;

import javafx.scene.canvas.GraphicsContext;

public interface Shape {
    void draw(GraphicsContext gc);
    double getX();
    double getY();
    boolean contains(double px, double py);
    void move(double dx, double dy);
}

