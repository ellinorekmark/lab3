package com.example.laborationtre;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.nio.file.Files;



public class ShapesModel {

    ObjectProperty<Number> size = new SimpleObjectProperty<>(50);
    ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BLACK);
    ObjectProperty<ToolOption> tool = new SimpleObjectProperty<>();

    ObservableList<MyShape> shapeList = FXCollections.observableArrayList();
    ObservableList<Command> memoryList = FXCollections.observableArrayList();
    ObservableList<Command> reverseList = FXCollections.observableArrayList();



    public double x;
    public double y;


    public void addToStack(MyShape shape) {
        shapeList.add(shape);
        memoryList.add(()-> shapeList.add(shape));
        reverseList.clear();
    }

    public void createShape(double x, double y) {
        switch (tool.getValue()) {
            case LINE -> createLineStart(x, y);
            case CIRCLE -> createCircle(x, y);
            case SQUARE -> createSquare(x, y);
        }

    }

    private void createSquare(double x, double y) {
        addToStack(new Square(x - (size.get().doubleValue() / 2), y - (size.get().doubleValue() / 2), size.get().doubleValue(), color.get()));
    }

    private void createLineStart(double posX, double posY) {
        x = posX;
        y = posY;
    }

    public void finishLine(MouseEvent mouseEvent) {
        addToStack(new Line(x, y, mouseEvent.getX(), mouseEvent.getY(), size.get().doubleValue(), color.get()));
    }

    private void createCircle(double x, double y) {
        addToStack(new MyCircle(x - (size.get().doubleValue() / 2), y - (size.get().doubleValue() / 2), size.get().doubleValue(), color.getValue()));
    }

    public void tryEditShape(double x, double y) {
        for (int i = 0; i < shapeList.size(); i++) {
            MyShape shape = shapeList.get(i);
            if (shape.compare(x, y)) {
                MyShape newShape = shape.copy(shape);
                newShape.setSize(size.get().doubleValue());
                newShape.setColor(color.get());
                shapeList.set(i, newShape);
                int finalI = i;
                memoryList.add(() -> shapeList.set(finalI, newShape));
                reverseList.clear();
            }
        }
    }




    public void saveToFile(java.nio.file.Path file) {

        StringBuilder string = new StringBuilder("""
                <?xml version="1.0" standalone="no"?>
                <svg width="500" height="500" version="1.1" xmlns="http://www.w3.org/2000/svg">
                """);
        for (MyShape shape : shapeList) {
            string.append(shape.toSVG()).append("\n");
        }
        string.append("</svg>");

        try {
            Files.writeString(file, string.toString());
        } catch (IOException e) {
            System.out.println("didn't work");
        }

    }


    public enum ToolOption {
        LINE {
            @Override
            public String toString() {
                return "Line";
            }
        },

        CIRCLE {
            @Override
            public String toString() {
                return "Circle";
            }
        },
        SQUARE {
            @Override
            public String toString() {
                return "Square";
            }
        }
    }

}




