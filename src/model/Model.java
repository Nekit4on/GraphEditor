package model;

import java.io.*;
import java.util.*;

public class Model implements Serializable {
    private List<Pixel> pixels = new ArrayList<>();
    private List<Point> dots = new ArrayList<>();
    private Pixel copiedPixel = null;

    public void addPixel(Pixel pixel) {
        pixels.add(pixel);
        pixels.sort(Comparator.comparingInt(Pixel::getZOrder));
    }

    public void addDot(Point dot) {
        dots.add(dot);
    }

    public void removePixel(Pixel pixel) {
        pixels.remove(pixel);
    }

    public Pixel getPixelAt(double x, double y) {
        for (int i = pixels.size() - 1; i >= 0; i--) {
            Pixel p = pixels.get(i);
            if (p instanceof Point) {
                Point point = (Point) p;
                if (x >= point.getX() && x <= point.getX() + 4 &&
                        y >= point.getY() && y <= point.getY() + 4) {
                    return p;
                }
            } else if (p instanceof Circle) {
                Circle circle = (Circle) p;
                double dx = x - circle.getX();
                double dy = y - circle.getY();
                if (dx * dx + dy * dy <= circle.getRadius() * circle.getRadius()) {
                    return p;
                }
            } else if (p instanceof Square) {
                Square square = (Square) p;
                if (x >= square.getX() - square.getSize()/2 &&
                        x <= square.getX() + square.getSize()/2 &&
                        y >= square.getY() - square.getSize()/2 &&
                        y <= square.getY() + square.getSize()/2) {
                    return p;
                }
            }
        }
        return null;
    }

    public List<Pixel> getPixels() { return pixels; }
    public List<Point> getDots() { return dots; }
    public void clear() { pixels.clear(); dots.clear(); }

    public void bringToFront(Pixel pixel) {
        if (!pixels.contains(pixel)) return;
        int maxZ = pixels.stream().mapToInt(Pixel::getZOrder).max().orElse(0);
        pixel.setZOrder(maxZ + 1);
        pixels.sort(Comparator.comparingInt(Pixel::getZOrder));
    }

    public void sendToBack(Pixel pixel) {
        if (!pixels.contains(pixel)) return;
        int minZ = pixels.stream().mapToInt(Pixel::getZOrder).min().orElse(0);
        pixel.setZOrder(minZ - 1);
        pixels.sort(Comparator.comparingInt(Pixel::getZOrder));
    }

    public PixelGroup groupShapes(List<Pixel> selected) {
        if (selected.isEmpty()) return null;
        double sumX = 0, sumY = 0;
        for (Pixel s : selected) { sumX += s.getX(); sumY += s.getY(); }
        PixelGroup group = new PixelGroup(sumX / selected.size(), sumY / selected.size());
        for (Pixel s : selected) {
            pixels.remove(s);
            group.addChild(s.copy());
        }
        addPixel(group);
        return group;
    }

    public void ungroupShape(PixelGroup group) {
        if (!pixels.contains(group)) return;
        pixels.remove(group);
        for (Pixel child : group.getChildren()) {
            addPixel(child);
        }
    }

    public void save(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(pixels);
            oos.writeObject(dots);
        }
    }

    public void load(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            pixels = (List<Pixel>) ois.readObject();
            dots = (List<Point>) ois.readObject();
        }
    }

    public void copyProperties(Pixel source) {
        copiedPixel = source;
    }

    public void applyProperties(Pixel target) {
        if (copiedPixel == null || target == null) return;
        target.setColor(copiedPixel.getColor());
        target.setShadow(copiedPixel.isShadowEnabled());
        target.setText(copiedPixel.getText());
    }

    public Pixel getCopiedShape() { return copiedPixel; }
}
