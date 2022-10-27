package com.example.laborationtre;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.Stack;




public class ShapesModel {


    static public Stack<Shape> shapeStack = new Stack<>();
    static public Stack<Shape> shapeUndoStack = new Stack<>();

    static public Stack<ChangedShape> editHistoryStack = new Stack<>();
    static public Stack<ChangedShape> redoHistoryStack = new Stack<>();

    static public double lineStartX;
    static public double lineStartY;

    public static Color shapeColor;
    public static double shapeSize;
    public static ToolOption shapeTool;

    public static void addToStack(Shape shape) {
        shapeStack.add(shape);
        shapeUndoStack.clear();
        redoHistoryStack.clear();

    }
    public static void createShape(MouseEvent mouseEvent){
        switch(shapeTool){
            case LINE -> createLineStart(mouseEvent);
            case CIRCLE -> createCircle(mouseEvent);
            case SQUARE -> createSquare(mouseEvent);
        }

    }
    private static void createSquare(MouseEvent mouseEvent) {
        addToStack(new Square(mouseEvent.getX() - (shapeSize / 2), mouseEvent.getY() - (shapeSize / 2), shapeSize, shapeColor));
    }

    private static void createLineStart(MouseEvent mouseEvent) {
        lineStartX = mouseEvent.getX();
        lineStartY = mouseEvent.getY();
    }

    public static void finishLine(MouseEvent mouseEvent){
        addToStack(new Line(lineStartX, lineStartY, mouseEvent.getX(), mouseEvent.getY(), shapeSize, shapeColor));
    }


    private static void createCircle(MouseEvent mouseEvent) {
        addToStack(new Circle(mouseEvent.getX() - (shapeSize / 2), mouseEvent.getY() - (shapeSize / 2), shapeSize, shapeColor));
    }

    public static void tryEditShape(MouseEvent mouseEvent) {

        for (int i = 0; i < shapeStack.size(); i++) {

            if (shapeStack.get(i).getClass().equals(Circle.class)) {
                if (compareCircleAndMouseEvent((Circle)shapeStack.get(i), mouseEvent)) {
                    editCircle((Circle)shapeStack.get(i), i);
                }
            } else if (shapeStack.get(i).getClass().equals(Square.class)) {
                if (compareSquareAndMouseEvent((Square)shapeStack.get(i), mouseEvent)) {
                    editSquare((Square) shapeStack.get(i), i);
                }
            } else if (shapeStack.get(i).getClass().equals(Line.class)) {
                if (compareLineAndMouseEvent((Line)shapeStack.get(i), mouseEvent)) {
                    editLine((Line) shapeStack.get(i), i);
                }
            }

        }
    }

    public static boolean compareCircleAndMouseEvent(Circle circleShape, MouseEvent mouseEvent){
        double startX = circleShape.getCenterX()+circleShape.getRadius();
        double endX = circleShape.getCenterX()-circleShape.getRadius();
        double startY = circleShape.getCenterY()+circleShape.getRadius();
        double endY = circleShape.getCenterY()-circleShape.getRadius();
        return mouseEvent.getX() > endX && mouseEvent.getX() < startX && mouseEvent.getY() > endY && mouseEvent.getY() < startY;

    }



    public static void editCircle(Shape shape, int i) {
        shape.setFill(shapeColor);
        Circle temp = (Circle) shape;
        temp.setRadius(shapeSize);
        shapeStack.set(i, temp);
    }


    public static boolean compareSquareAndMouseEvent(Square shape, MouseEvent mouseEvent){
        double startX = shape.positionX;
        double endX = shape.positionX+shape.size;
        double startY = shape.positionY;
        double endY = shape.positionY+shape.size;
        return mouseEvent.getX() < endX && mouseEvent.getX() > startX && mouseEvent.getY() < endY && mouseEvent.getY() > startY;
    }

    public static boolean compareLineAndMouseEvent(Line shape, MouseEvent mouseEvent) { //basically checks a rectangle around the line, close enough.
        double rightEdge;
        double leftEdge;
        double top;
        double bottom;

        if (shape.startX-shape.endX<0){
            rightEdge = shape.endX;
            leftEdge = shape.startX;
        }
        else{
            rightEdge = shape.startX;
            leftEdge = shape.endX;
        }

        if (shape.startY-shape.endY<0){
            top = shape.endY;
            bottom = shape.startY;
        }
        else{
            top = shape.startY;
            bottom = shape.endY;
        }
        return mouseEvent.getX() > leftEdge && mouseEvent.getX() < rightEdge && mouseEvent.getY() > bottom && mouseEvent.getY() < top;
    }


    public static void editCircle(Circle shape, int i) {
        shape.setFill(shapeColor);
        shape.setRadius(shapeSize);
        shapeStack.set(i, shape);
    }
    public static void editSquare(Square shape, int i) {
        shape.color = shapeColor;
        shape.size = shapeSize;
        shapeStack.set(i,shape);
    }
    public static void editLine(Line shape, int i) {
        shape.color = shapeColor;
        shape.width = shapeSize;
        shapeStack.set(i, shape);
    }


    public enum ToolOption {
        LINE,
        FREEDRAW,
        CIRCLE,
        SQUARE
    }



}




