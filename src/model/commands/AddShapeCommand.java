package model.commands;

import model.Model;
import model.shapes.Shape;

public class AddShapeCommand implements Command {
    private Model model;
    private Shape shape;

    public AddShapeCommand(Model model, Shape shape) {
        this.model = model;
        this.shape = shape;
    }

    @Override
    public void execute() {
        model.addElement(shape);
    }

    @Override
    public void undo() {
        model.removeElement(shape);
    }
}
