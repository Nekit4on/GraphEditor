package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Rectangle extends Shape implements Serializable {
    private double width, height;

    public Rectangle(Color color, double x, double y, double width, double height) {
        super(color, x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!visible) return;

        if (shadowEnabled) {
            gc.setFill(Color.gray(0, 0.7));
            gc.fillRect((int)(x - width / 2 + 10), (int)(y - height / 2 + 10), (int)width, (int)height);
        }

        gc.setFill(color);
        gc.fillRect((int)(x - width / 2), (int)(y - height / 2), (int)width, (int)height);

        if ("hatch".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)(-height / 2); i <= (int)(height / 2); i += 8) {
                gc.strokeLine((int)(x - width / 2), (int)(y + i), (int)(x + width / 2), (int)(y + i));
            }
        } else if ("dots".equals(fillPattern)) {
            gc.setFill(Color.BLACK);
            for (int dx = (int)(-width / 2); dx <= (int)(width / 2); dx += 10) {
                for (int dy = (int)(-height / 2); dy <= (int)(height / 2); dy += 10) {
                    gc.fillOval((int)(x + dx - 2), (int)(y + dy - 2), 4, 4);
                }
            }
        } else if ("grid".equals(fillPattern)) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            for (int i = (int)(-width / 2); i <= (int)(width / 2); i += 10) {
                gc.strokeLine((int)(x + i), (int)(y - height / 2), (int)(x + i), (int)(y + height / 2));
            }
            for (int i = (int)(-height / 2); i <= (int)(height / 2); i += 10) {
                gc.strokeLine((int)(x - width / 2), (int)(y + i), (int)(x + width / 2), (int)(y + i));
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
        return px >= x - width / 2 && px <= x + width / 2
                && py >= y - height / 2 && py <= y + height / 2;
    }

    @Override
    public Shape copy() {
        Rectangle copy = new Rectangle(color, x, y, width, height);
        copy.setShadow(shadowEnabled);
        copy.setVisible(visible);
        return copy;
    }

    @Override
    public void resize(double factor) {
        width *= factor;
        height *= factor;
        if (width < 10) width = 10;
        if (height < 10) height = 10;
        if (width > 300) width = 300;
        if (height > 300) height = 300;
    }
}