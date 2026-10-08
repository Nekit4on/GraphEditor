package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;


public abstract class Shape implements Serializable {
    protected Color color;
    protected double x, y;
    protected boolean visible = true;
    protected int zOrder = 0;
    protected boolean shadowEnabled = false;
    protected String text = "";
    protected String fillPattern = "solid"; // solid, hatch, dots, grid

    public Shape(Color color, double x, double y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public abstract void draw(GraphicsContext gc);
    public abstract boolean contains(double px, double py);
    public abstract Shape copy();

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
    public double getX() { return x; }
    public double getY() { return y; }
    public int getZOrder() { return zOrder; }
    public void setZOrder(int zOrder) { this.zOrder = zOrder; }

    public abstract void resize(double factor);
}