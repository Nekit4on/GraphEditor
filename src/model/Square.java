package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Square extends Pixel implements Serializable {
    private double size;

    public Square(double x, double y, Color color, double size) {
        super(color, x, y);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;

        if (shadowEnabled) {
            gc.setFill(Color.gray(0, 0.7));
            gc.fillRect((int)(x - size / 2 + 10), (int)(y - size / 2 + 10), (int)size, (int)size);
        }

        gc.setFill(color);
        gc.fillRect((int)(x - size / 2), (int)(y - size / 2), (int)size, (int)size);

        if ("hatch".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)(-size / 2); i <= (int)(size / 2); i += 8) {
                gc.strokeLine((int)(x - size / 2), (int)(y + i), (int)(x + size / 2), (int)(y + i));
            }
        } else if ("dots".equals(fillPattern)) {
            gc.setFill(Color.BLACK);
            for (int dx = (int)(-size / 2); dx <= (int)(size / 2); dx += 10) {
                for (int dy = (int)(-size / 2); dy <= (int)(size / 2); dy += 10) {
                    gc.fillOval((int)(x + dx - 2), (int)(y + dy - 2), 4, 4);
                }
            }
        } else if ("grid".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)(-size / 2); i <= (int)(size / 2); i += 10) {
                gc.strokeLine((int)(x + i), (int)(y - size / 2), (int)(x + i), (int)(y + size / 2));
            }
            for (int i = (int)(-size / 2); i <= (int)(size / 2); i += 10) {
                gc.strokeLine((int)(x - size / 2), (int)(y + i), (int)(x + size / 2), (int)(y + i));
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
        return px >= x - size / 2 && px <= x + size / 2
                && py >= y - size / 2 && py <= y + size / 2;
    }

    @Override
    public Square copy() {
        Square copy = new Square(x, y, color, size);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        return copy;
    }

    @Override
    public void resize(double factor) {
        size *= factor;
        if (size < 10) size = 10;
        if (size > 300) size = 300;
    }

    public double getSize() {
        return size;
    }
}
