package com.example.cursach;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Класс Generator отвечает за генерацию и отрисовку случайных фигур на холсте.
 */
public class Generator {
    private static final Logger logger = LogManager.getLogger(Generator.class);
    private final Random random = new Random();
    private final double minX, maxX, minY, maxY;
    private final int[] numFigures;
    private final double density;
    private final double gridSpacing;
    private final List<Figure> figureTypes;
    private final boolean fillFigures;

    /**
     * Конструктор класса Generator.
     * @param minX Минимальное значение координаты X.
     * @param maxX Максимальное значение координаты X.
     * @param minY Минимальное значение координаты Y.
     * @param maxY Максимальное значение координаты Y.
     * @param numFigures Массив, содержащий количество каждой фигуры.
     * @param density Плотность фигур.
     * @param gridSpacing Размер сетки.
     * @param figureTypes Список типов фигур.
     * @param fillFigures Флаг, указывающий, нужно ли заполнять фигуры цветом.
     */
    public Generator(double minX, double maxX, double minY, double maxY, int[] numFigures, double density, double gridSpacing, List<Figure> figureTypes, boolean fillFigures) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.numFigures = numFigures;
        this.density = density;
        this.gridSpacing = gridSpacing;
        this.figureTypes = figureTypes;
        this.fillFigures = fillFigures;
    }
    /**
     * Метод для отрисовки фигур на холсте.
     * @param gc Контекст графики.
     */
    public void draw(GraphicsContext gc) {
        logger.info("Начало генерации фигур.");
        int[] figuresDrawn = new int[numFigures.length];
        int totalFiguresToDraw = Arrays.stream(numFigures).sum();
        int figuresDrawnCount = 0;

        while (figuresDrawnCount < totalFiguresToDraw) {
            int figureTypeIndex = pickFigureType(figuresDrawn, numFigures);
            if (figureTypeIndex == -1) break;

            // Generate coordinates within the specified bounds, considering density
            double distanceFromCenter = random.nextDouble() * Math.sqrt(density);
            double angle = random.nextDouble() * 2 * Math.PI;
            double centerX = (maxX + minX) / 2;
            double centerY = (minY + maxY) / 2;

            double x = centerX + distanceFromCenter * Math.cos(angle) * (maxX - minX) / 2;
            double y = centerY + distanceFromCenter * Math.sin(angle) * (maxY - minY) / 2;

            //Keep generated point within specified bounds
            x = Math.max(minX, Math.min(maxX, x));
            y = Math.max(minY, Math.min(maxY, y));

            double scaleFactor = gridSpacing / 100;
            double originalLineWidth = gc.getLineWidth();
            try {
                Figure figure = figureTypes.get(figureTypeIndex).getClass().getDeclaredConstructor(double.class).newInstance(scaleFactor);
                figure.draw(gc, x, y, fillFigures);
                figuresDrawn[figureTypeIndex]++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            gc.setLineWidth(originalLineWidth);
            figuresDrawnCount++;
        }
        logger.info("Конец генерации фигур.");
    }
    /**
     * Выбирает случайный тип фигуры из доступных, учитывая количество уже нарисованных фигур каждого типа.
     *
     * @param figuresDrawn Массив, содержащий количество нарисованных фигур каждого типа.
     * @param numFigures   Массив, содержащий общее количество фигур каждого типа, которое необходимо нарисовать.
     * @return Индекс выбранного типа фигуры в массиве figureTypes (Generator), или -1, если все типы фигур уже нарисованы.
     */
    private int pickFigureType(int[] figuresDrawn, int[] numFigures) {
        List<Integer> availableTypes = new ArrayList<>();
        for (int i = 0; i < numFigures.length; i++) {
            if (figuresDrawn[i] < numFigures[i]) {
                availableTypes.add(i);
            }
        }

        if (availableTypes.isEmpty()) {
            return -1;
        }

        return availableTypes.get(random.nextInt(availableTypes.size()));
    }
}