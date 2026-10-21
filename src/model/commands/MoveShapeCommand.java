package model.commands;

import model.Model;
import model.shapes.Shape;

public class MoveShapeCommand implements Command {
    private Model model;
    private Shape shape;
    private double oldX, oldY;
    private double newX, newY;

    public MoveShapeCommand(Model model, Shape shape, double newX, double newY) {
        this.model = model;
        this.shape = shape;
        this.oldX = shape.getX();
        this.oldY = shape.getY();
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public void execute() {
        shape.move(newX - oldX, newY - oldY);
    }

    @Override
    public void undo() {
        shape.move(oldX - newX, oldY - newY);
    }
}
