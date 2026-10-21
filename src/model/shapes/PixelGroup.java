package model.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class PixelGroup extends Pixel {
    private List<Pixel> children = new ArrayList<>();

    public PixelGroup(double x, double y) {
        super(x, y, Color.BLACK);
    }

    public void addChild(Pixel shape) {
        children.add(shape);
    }

    public void removeChild(Pixel shape) {
        children.remove(shape);
    }

    public List<Pixel> getChildren() {
        return children;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!isVisible()) return;
        for (Pixel child : children) {
            child.draw(gc);
        }
    }

    @Override
    public boolean contains(double px, double py) {
        for (Pixel child : children) {
            if (child.contains(px, py)) return true;
        }
        return false;
    }

    @Override
    public void move(double dx, double dy) {
        super.move(dx, dy);
        for (Pixel child : children) {
            child.move(dx, dy);
        }
    }

    @Override
    public Pixel copy() {
        PixelGroup copy = new PixelGroup(getX(), getY());
        copy.setColor(getColor());
        copy.setShadow(isShadowEnabled());
        copy.setVisible(isVisible());
        for (Pixel child : children) {
            copy.addChild((Pixel) child.copy());
        }
        return copy;
    }

    @Override
    public void resize(double factor) {
        for (Pixel child : children) {
            child.resize(factor);
        }
    }
}

