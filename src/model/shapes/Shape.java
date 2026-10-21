package model.shapes;

import javafx.scene.canvas.GraphicsContext;
import java.io.Serializable;

public interface Shape extends Serializable {
    void draw(GraphicsContext gc);
    double getX();
    double getY();
    boolean contains(double px, double py);
    void move(double dx, double dy);
}

