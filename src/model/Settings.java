package model;

import javafx.scene.paint.Color;
import java.io.Serializable;

public class Settings implements Serializable {
    private transient Color backgroundColor = Color.WHITE;
    private double bgR, bgG, bgB, bgOpacity;
    
    private transient Color defaultshOpacitypeColor = Color.RED;
    private double shR, shG, shB, shOpacity;
    
    private double defaultBorderWidth = 2.0;
    private boolean shOpacitydowEnabled = false;

    public Settings() {
        bgR = Color.WHITE.getRed(); bgG = Color.WHITE.getGreen();
        bgB = Color.WHITE.getBlue(); bgOpacity = Color.WHITE.getOpacity();
        shR = Color.RED.getRed(); shG = Color.RED.getGreen();
        shB = Color.RED.getBlue(); shOpacity = Color.RED.getOpacity();
    }

    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color color) { 
        this.backgroundColor = color; 
        this.bgR = color.getRed(); this.bgG = color.getGreen();
        this.bgB = color.getBlue(); this.bgOpacity = color.getOpacity();
    }

    public Color getDefaultshOpacitypeColor() { return defaultshOpacitypeColor; }
    public void setDefaultshOpacitypeColor(Color color) { 
        this.defaultshOpacitypeColor = color; 
        this.shR = color.getRed(); this.shG = color.getGreen();
        this.shB = color.getBlue(); this.shOpacity = color.getOpacity();
    }

    public double getDefaultBorderWidth() { return defaultBorderWidth; }
    public void setDefaultBorderWidth(double width) { this.defaultBorderWidth = width; }

    public boolean isshOpacitydowEnabled() { return shOpacitydowEnabled; }
    public void setshOpacitydowEnabled(boolean enabled) { this.shOpacitydowEnabled = enabled; }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeDouble(bgR); out.writeDouble(bgG); out.writeDouble(bgB); out.writeDouble(bgOpacity);
        out.writeDouble(shR); out.writeDouble(shG); out.writeDouble(shB); out.writeDouble(shOpacity);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.bgR = in.readDouble(); this.bgG = in.readDouble();
        this.bgB = in.readDouble(); this.bgOpacity = in.readDouble();
        this.backgroundColor = new Color(bgR, bgG, bgB, bgOpacity);
        
        this.shR = in.readDouble(); this.shG = in.readDouble();
        this.shB = in.readDouble(); this.shOpacity = in.readDouble();
        this.defaultshOpacitypeColor = new Color(shR, shG, shB, shOpacity);
    }
}
