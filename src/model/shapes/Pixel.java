package model.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public abstract class Pixel implements Shape, Serializable {
    protected double x, y;
    protected transient Color color;
    protected double colorR, colorG, colorB, colorOpacity;
    protected boolean visible = true;
    protected boolean shadowEnabled = false;
    protected String text = "";
    protected String fillPattern = "solid";

    public Pixel(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        setColor(color);
    }

    public abstract void draw(GraphicsContext gc);
    public abstract boolean contains(double px, double py);
    public abstract Pixel copy();
    public abstract void resize(double factor);

    public double getX() { return x; }
    public double getY() { return y; }

    public void move(double dx, double dy) { x += dx; y += dy; }

    public void setShadow(boolean enabled) { this.shadowEnabled = enabled; }
    public boolean isShadowEnabled() { return shadowEnabled; }
    public void setText(String text) { this.text = text; }
    public String getText() { return text; }
    public void setFillPattern(String pattern) { this.fillPattern = pattern; }
    public String getFillPattern() { return fillPattern; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { 
        this.color = color; 
        this.colorR = color.getRed();
        this.colorG = color.getGreen();
        this.colorB = color.getBlue();
        this.colorOpacity = color.getOpacity();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeDouble(colorR);
        out.writeDouble(colorG);
        out.writeDouble(colorB);
        out.writeDouble(colorOpacity);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.colorR = in.readDouble();
        this.colorG = in.readDouble();
        this.colorB = in.readDouble();
        this.colorOpacity = in.readDouble();
        this.color = new Color(colorR, colorG, colorB, colorOpacity);
    }
}
