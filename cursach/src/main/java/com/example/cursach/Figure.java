package com.example.cursach;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;
/**
 * Абстрактный класс Figure, представляющий геометрическую фигуру.
 */

abstract class Figure {
    protected final Random random = new Random();
    protected Color color;
    protected double scaleFactor;
    /**
     * Конструктор класса Figure.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Figure(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        this.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    /**
     * Абстрактный метод для отрисовки фигуры.
     * @param gc Контекст графики.
     * @param x Координата X центра фигуры.
     * @param y Координата Y центра фигуры.
     * @param fill Флаг, указывающий, нужно ли заполнять фигуру цветом.
     */
    public abstract void draw(GraphicsContext gc, double x, double y, boolean fill);
}
/**
 * Класс Line, представляющий отрезок линии.
 */
class Line extends Figure {
    private double lineWidth = 3;
    private double length;
    /**
     * Конструктор класса Line.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Line(double scaleFactor) {
        super(scaleFactor);
        this.length = (random.nextDouble() * 100 + 50) * scaleFactor;
    }

    @Override
    /**
     * Рисует линию на холсте. Линия имеет случайную длину и угол наклона.
     * @param gc Контекст графики для рисования.
     * @param x Координата X центра линии.
     * @param y Координата Y центра линии.
     * @param fill Флаг, указывающий, нужно ли заполнять фигуру (не используется для линии).
     */
    public void draw(GraphicsContext gc, double x, double y, boolean fill) {
        double originalLineWidth = gc.getLineWidth();
        gc.setLineWidth(lineWidth);
        double angle = random.nextDouble() * 2 * Math.PI;
        gc.setStroke(color);
        gc.strokeLine(x - length / 2 * Math.cos(angle), y - length / 2 * Math.sin(angle),
                x + length / 2 * Math.cos(angle), y + length / 2 * Math.sin(angle));
        gc.setLineWidth(originalLineWidth);
    }
}
/**
 * Класс Circle, представляющий окружность.
 */
class Circle extends Figure {
    /**
     * Конструктор класса Circle.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Circle(double scaleFactor) {
        super(scaleFactor);
    }

    @Override
    /**
     * Рисует окружность на холсте. Радиус окружности выбирается случайным образом.
     * @param gc Контекст графики для рисования.
     * @param x Координата X центра окружности.
     * @param y Координата Y центра окружности.
     * @param fill Флаг, указывающий, нужно ли заполнять окружность цветом.
     */
    public void draw(GraphicsContext gc, double x, double y, boolean fill) {
        double radius = (random.nextDouble() * 50 + 10) * scaleFactor;
        gc.setStroke(color);
        gc.setLineWidth(1);
        if (fill) {
            gc.setFill(color);
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        } else {
            gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }
}
/**
 * Класс Rectangle, представляющий прямоугольник.
 */
class Rectangle extends Figure {
    /**
     * Конструктор класса Rectangle.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Rectangle(double scaleFactor) {
        super(scaleFactor);
    }

    @Override
    /**
     * Рисует прямоугольник на холсте. Ширина и высота прямоугольника выбираются случайным образом.
     * @param gc Контекст графики для рисования.
     * @param x Координата X центра прямоугольника.
     * @param y Координата Y центра прямоугольника.
     * @param fill Флаг, указывающий, нужно ли заполнять прямоугольник цветом.
     */
    public void draw(GraphicsContext gc, double x, double y, boolean fill) {
        double width = (random.nextDouble() * 50 + 20) * scaleFactor;
        double height = (random.nextDouble() * 50 + 20) * scaleFactor;
        gc.setStroke(color);
        gc.setLineWidth(1);
        gc.setFill(color);
        if (fill) {
            gc.fillRect(x - width / 2, y - height / 2, width, height);
        } else {
            gc.strokeRect(x - width / 2, y - height / 2, width, height);
        }
    }
}
/**
 * Класс Triangle, представляющий треугольник.
 */
class Triangle extends Figure {
    /**
     * Конструктор класса Triangle.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Triangle(double scaleFactor) {
        super(scaleFactor);
    }

    @Override
    /**
     * Рисует треугольник на холсте. Размер треугольника выбирается случайным образом.
     * @param gc Контекст графики для рисования.
     * @param x Координата X центра треугольника.
     * @param y Координата Y центра треугольника.
     * @param fill Флаг, указывающий, нужно ли заполнять треугольник цветом.
     */
    public void draw(GraphicsContext gc, double x, double y, boolean fill) {
        double size = (random.nextDouble() * 40 + 20) * scaleFactor;

        // Calculate centroid
        double centerX = x;
        double centerY = y - size / 6; // Adjusted for vertical flip

        // Calculate vertices relative to the centroid, flipped vertically
        double[] xPoints = {centerX, centerX - size / 2, centerX + size / 2};
        double[] yPoints = {centerY - size / 2, centerY + size / 2, centerY + size / 2}; //Flipped y-coordinates

        gc.setStroke(color);
        gc.setLineWidth(1);
        gc.setFill(color);
        if (fill) {
            gc.fillPolygon(xPoints, yPoints, 3);
        } else {
            gc.strokePolygon(xPoints, yPoints, 3);
        }
    }
}

/**
 * Класс Parabola, представляющий параболу.
 */
class Parabola extends Figure {
    private double lineWidth = 2;
    private double width;
    private double heightMultiplier = 2.5;
    /**
     * Конструктор класса Parabola.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Parabola(double scaleFactor) {
        super(scaleFactor);
        this.width = (random.nextDouble() * 150 + 50) * scaleFactor;
    }

    @Override
    /**
     * Рисует параболу на холсте. Ширина параболы выбирается случайным образом.
     * @param gc Контекст графики для рисования.
     * @param x Координата X центра параболы.
     * @param y Координата Y центра параболы.
     * @param fill Флаг, указывающий, нужно ли заполнять параболу (не используется для параболы).
     */
    public void draw(GraphicsContext gc, double x, double y, boolean fill) {
        double originalLineWidth = gc.getLineWidth();
        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        int numSegments = 50;
        double leftwardShift = width / 3;
        double[] xPoints = new double[numSegments + 1];
        double[] yPoints = new double[numSegments + 1];
        double verticalShift = width * heightMultiplier * 0.000000001;

        for (int i = 0; i <= numSegments; i++) {
            double t = (double) i / numSegments;
            xPoints[i] = x - width / 2 + t * width - leftwardShift; // Added leftward shift
            yPoints[i] = y - verticalShift - width * heightMultiplier * (t - 0.5) * (t - 0.5);
        }

        for (int i = 0; i <= numSegments; i++) {
            double t = (double) i / numSegments;
            xPoints[i] = x - width / 2 + t * width;
            yPoints[i] = y - verticalShift - width * heightMultiplier * (t - 0.5) * (t - 0.5);
        }
        for (int i = 0; i < numSegments; i++) {
            gc.strokeLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }
        gc.setLineWidth(originalLineWidth);
    }
}
/**
 * Класс Trapezoid, представляющий трапецию.
 */
class Trapezoid extends Figure {
    /**
     * Конструктор класса Trapezoid.
     * @param scaleFactor Масштабирующий фактор.
     */
    public Trapezoid(double scaleFactor) {
        super(scaleFactor);
    }

    @Override
    /**
     * Рисует трапецию на холсте. Размеры трапеции выбираются случайным образом.
     * @param gc Контекст графики для рисования.
     * @param x Координата X центра трапеции.
     * @param y Координата Y центра трапеции.
     * @param fill Флаг, указывающий, нужно ли заполнять трапецию цветом.
     */
    public void draw(GraphicsContext gc, double x, double y, boolean fill) {
        double widthTop = (random.nextDouble() * 30 + 10) * scaleFactor;
        double widthBottom = (random.nextDouble() * 50 + 30) * scaleFactor;
        double height = (random.nextDouble() * 40 + 20) * scaleFactor;

        double shiftPercentage = 0.2;
        // Calculate the centroid
        double centerX = x;
        double centerY = y + height / 3.0 - (height * shiftPercentage / 2.0);

        // Calculate vertices relative to the centroid
        double[] xPoints = {centerX - widthTop / 2, centerX + widthTop / 2, centerX + widthBottom / 2, centerX - widthBottom / 2};
        double[] yPoints = {centerY - height / 2, centerY - height / 2, centerY + height / 2, centerY + height / 2};

        gc.setStroke(color);
        gc.setLineWidth(1);
        gc.setFill(color);
        if (fill) {
            gc.fillPolygon(xPoints, yPoints, 4);
        } else {
            gc.strokePolygon(xPoints, yPoints, 4);
        }
    }
}