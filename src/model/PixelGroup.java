package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PixelGroup extends Pixel implements Serializable {
    private List<Pixel> children = new ArrayList<>();

    public PixelGroup(double x, double y) {
        super(Color.BLACK, x, y);
    }

    public void addChild(Pixel pixel) {
        children.add(pixel);
    }

    public void removeChild(Pixel pixel) {
        children.remove(pixel);
    }

    public List<Pixel> getChildren() {
        return children;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;
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
        PixelGroup copy = new PixelGroup(x, y);
        copy.setColor(color);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        for (Pixel child : children) {
            copy.addChild(child.copy());
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