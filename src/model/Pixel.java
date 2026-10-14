package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Pixel implements Shape {
    protected double x, y;
    protected Color color;
    protected boolean visible = true;
    protected boolean shadowEnabled = false;
    protected String text = "";
    protected String fillPattern = "solid";

    public Pixel(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract void draw(GraphicsContext gc);
    public abstract boolean contains(double px, double py);
    public abstract Pixel copy();
    public abstract void resize(double factor);

    public double getX() { return x; }
    public double getY() { return y; }

    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void setShadow(boolean enabled) { this.shadowEnabled = enabled; }
    public boolean isShadowEnabled() { return shadowEnabled; }
    public void setText(String text) { this.text = text; }
    public String getText() { return text; }
    public void setFillPattern(String pattern) { this.fillPattern = pattern; }
    public String getFillPattern() { return fillPattern; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}