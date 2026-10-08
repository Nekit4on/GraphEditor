package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Circle extends Pixel implements Serializable {
    private double radius;

    public Circle(double x, double y, Color color, double radius) {
        super(color, x, y);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;

        if (shadowEnabled) {
            gc.setFill(Color.gray(0, 0.7));
            gc.fillOval((int)(x - radius + 10), (int)(y - radius + 10), (int)(radius * 2), (int)(radius * 2));
        }

        gc.setFill(color);
        gc.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));

        if ("hatch".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)-radius; i <= (int)radius; i += 8) {
                double y1 = y + i;
                double xStart = x - Math.sqrt(Math.max(0, radius * radius - i * i));
                double xEnd = x + Math.sqrt(Math.max(0, radius * radius - i * i));
                gc.strokeLine((int)xStart, (int)y1, (int)xEnd, (int)y1);
            }
        } else if ("dots".equals(fillPattern)) {
            gc.setFill(Color.BLACK);
            for (int dx = (int)-radius; dx <= (int)radius; dx += 10) {
                for (int dy = (int)-radius; dy <= (int)radius; dy += 10) {
                    double dist = Math.sqrt(dx * dx + dy * dy);
                    if (dist <= radius - 2) {
                        gc.fillOval((int)(x + dx - 2), (int)(y + dy - 2), 4, 4);
                    }
                }
            }
        } else if ("grid".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)-radius; i <= (int)radius; i += 10) {
                double y1 = y + i;
                double xStart = x - Math.sqrt(Math.max(0, radius * radius - i * i));
                double xEnd = x + Math.sqrt(Math.max(0, radius * radius - i * i));
                gc.strokeLine((int)xStart, (int)y1, (int)xEnd, (int)y1);

                double x1 = x + i;
                double yStart = y - Math.sqrt(Math.max(0, radius * radius - i * i));
                double yEnd = y + Math.sqrt(Math.max(0, radius * radius - i * i));
                gc.strokeLine((int)x1, (int)yStart, (int)x1, (int)yEnd);
            }
        }

        if (text != null && !text.isEmpty()) {
            gc.setFill(Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font(12));
            gc.fillText(text, (int)(x - 20), (int)(y + 5));
        }
    }

    @Override
    public boolean contains(double px, double py) {
        double dx = px - x;
        double dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public Circle copy() {
        Circle copy = new Circle(x, y, color, radius);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        return copy;
    }

    @Override
    public void resize(double factor) {
        radius *= factor;
        if (radius < 5) radius = 5;
        if (radius > 200) radius = 200;
    }

    public double getRadius() {
        return radius;
    }
}
