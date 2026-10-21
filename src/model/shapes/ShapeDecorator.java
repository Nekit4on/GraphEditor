package model.shapes;

import javafx.scene.canvas.GraphicsContext;

public abstract class ShapeDecorator implements Shape {
    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape) {
        this.decoratedShape = decoratedShape;
    }

    @Override
    public void draw(GraphicsContext gc) {
        decoratedShape.draw(gc);
    }

    @Override
    public double getX() {
        return decoratedShape.getX();
    }

    @Override
    public double getY() {
        return decoratedShape.getY();
    }

    @Override
    public boolean contains(double px, double py) {
        return decoratedShape.contains(px, py);
    }

    @Override
    public void move(double dx, double dy) {
        decoratedShape.move(dx, dy);
    }
}

