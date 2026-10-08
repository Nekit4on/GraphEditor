package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShapeGroup extends Shape implements Serializable {
    private List<Shape> children = new ArrayList<>();

    public ShapeGroup(double x, double y) {
        super(Color.BLACK, x, y);
    }

    public void addChild(Shape shape) {
        children.add(shape);
    }

    public void removeChild(Shape shape) {
        children.remove(shape);
    }

    public List<Shape> getChildren() {
        return children;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;
        for (Shape child : children) {
            child.draw(gc);
        }
    }

    @Override
    public boolean contains(double px, double py) {
        for (Shape child : children) {
            if (child.contains(px, py)) return true;
        }
        return false;
    }

    @Override
    public void move(double dx, double dy) {
        super.move(dx, dy);
        for (Shape child : children) {
            child.move(dx, dy);
        }
    }

    @Override
    public Shape copy() {
        ShapeGroup copy = new ShapeGroup(x, y);
        copy.setColor(color);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        for (Shape child : children) {
            copy.addChild(child.copy());
        }
        return copy;
    }

    @Override
    public void resize(double factor) {
        for (Shape child : children) {
            child.resize(factor);
        }
    }
}