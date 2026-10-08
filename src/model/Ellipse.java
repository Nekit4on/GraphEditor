package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Ellipse extends Shape implements Serializable {
    private double radiusX, radiusY;

    public Ellipse(Color color, double x, double y, double radiusX, double radiusY) {
        super(color, x, y);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;

        if (shadowEnabled) {
            gc.setFill(Color.gray(0, 0.7));
            gc.fillOval((int)(x - radiusX + 10), (int)(y - radiusY + 10), (int)(radiusX * 2), (int)(radiusY * 2));
        }

        gc.setFill(color);
        gc.fillOval((int)(x - radiusX), (int)(y - radiusY), (int)(radiusX * 2), (int)(radiusY * 2));

        if ("hatch".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)-radiusY; i <= (int)radiusY; i += 8) {
                double y1 = y + i;
                double xStart = x - Math.sqrt(Math.max(0, radiusX * radiusX * (1 - (i * i) / (radiusY * radiusY))));
                double xEnd = x + Math.sqrt(Math.max(0, radiusX * radiusX * (1 - (i * i) / (radiusY * radiusY))));
                gc.strokeLine((int)xStart, (int)y1, (int)xEnd, (int)y1);
            }
        } else if ("dots".equals(fillPattern)) {
            gc.setFill(Color.BLACK);
            for (int dx = (int)-radiusX; dx <= (int)radiusX; dx += 10) {
                for (int dy = (int)-radiusY; dy <= (int)radiusY; dy += 10) {
                    double dist = (dx * dx) / (radiusX * radiusX) + (dy * dy) / (radiusY * radiusY);
                    if (dist <= 1.0) {
                        gc.fillOval((int)(x + dx - 2), (int)(y + dy - 2), 4, 4);
                    }
                }
            }
        } else if ("grid".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)-radiusY; i <= (int)radiusY; i += 10) {
                double y1 = y + i;
                double xStart = x - Math.sqrt(Math.max(0, radiusX * radiusX * (1 - (i * i) / (radiusY * radiusY))));
                double xEnd = x + Math.sqrt(Math.max(0, radiusX * radiusX * (1 - (i * i) / (radiusY * radiusY))));
                gc.strokeLine((int)xStart, (int)y1, (int)xEnd, (int)y1);
            }
            for (int i = (int)-radiusX; i <= (int)radiusX; i += 10) {
                double x1 = x + i;
                double yStart = y - Math.sqrt(Math.max(0, radiusY * radiusY * (1 - (i * i) / (radiusX * radiusX))));
                double yEnd = y + Math.sqrt(Math.max(0, radiusY * radiusY * (1 - (i * i) / (radiusX * radiusX))));
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
        double dx = (px - x) / radiusX;
        double dy = (py - y) / radiusY;
        return dx * dx + dy * dy <= 1;
    }

    @Override
    public Shape copy() {
        Ellipse copy = new Ellipse(color, x, y, radiusX, radiusY);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        return copy;
    }

    @Override
    public void resize(double factor) {
        radiusX *= factor;
        radiusY *= factor;
        if (radiusX < 5) radiusX = 5;
        if (radiusY < 5) radiusY = 5;
        if (radiusX > 200) radiusX = 200;
        if (radiusY > 200) radiusY = 200;
    }
}