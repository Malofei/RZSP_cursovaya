package com.example.cursach;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
/**
 * Главный класс графического интерфейса (GUI) для приложения генератора случайных фигур.
 */
public class GUI extends Application {
    private static final Logger logger = LogManager.getLogger(GUI.class);
    private Canvas canvas;
    private Generator generator;
    /**
     * Отображает диалоговое окно с сообщением об ошибке.
     * @param message Текст сообщения об ошибке.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Точка входа в приложение. Запускает графический интерфейс.
     * @param primaryStage Основной Stage (окно) JavaFX приложения.
     */
    @Override
    public void start(Stage primaryStage) {
        initializeUI(primaryStage);
    }
    /**
     * Инициализирует и отображает элементы пользовательского интерфейса.
     * @param primaryStage Основной Stage (окно) JavaFX приложения.
     */
    private void initializeUI(Stage primaryStage) {
        List<CheckBox> figureCheckboxes = new ArrayList<>();
        List<TextField> numFigureFields = new ArrayList<>();
        String[] figureNames = {"Линия", "Окружность", "Квадрат", "Треугольник", "Парабола", "Трапеция"};
        List<Figure> figureTypes = new ArrayList<>();
        figureTypes.add(new Line(1.0));
        figureTypes.add(new Circle(1.0));
        figureTypes.add(new Rectangle(1.0));
        figureTypes.add(new Triangle(1.0));
        figureTypes.add(new Parabola(1.0));
        figureTypes.add(new Trapezoid(1.0));
        VBox figureSelection = new VBox(10);
        for (int i = 0; i < figureNames.length; i++) {
            final String figureName = figureNames[i];
            CheckBox checkbox = new CheckBox(figureName);
            TextField textField = new TextField("5");
            checkbox.setSelected(true);
            checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                logger.info("Состояние галочки \"{}\" изменено: {} -> {}", figureName, oldValue, newValue);
            });
            HBox row = new HBox(10, checkbox, textField);
            figureSelection.getChildren().addAll(row);
            figureCheckboxes.add(checkbox);
            numFigureFields.add(textField);
        }

        Label minXLabel = new Label("Min X:");
        TextField minXField = new TextField("50");
        Label maxXLabel = new Label("Max X:");
        TextField maxXField = new TextField("850");
        Label minYLabel = new Label("Min Y:");
        TextField minYField = new TextField("50");
        Label maxYLabel = new Label("Max Y:");
        TextField maxYField = new TextField("850");
        Label densityLabel = new Label("Кучность (0.0-1.5 ):");
        Slider densitySlider = new Slider(0, 1.5, 1);
        densitySlider.setShowTickMarks(true);
        densitySlider.setShowTickLabels(true);
        densitySlider.setMajorTickUnit(0.1);
        densitySlider.setBlockIncrement(0.1);
        Label gridSizeLabel = new Label("Масштаб (10-400):");
        Slider gridSizeSlider = new Slider(10, 400, 100);
        gridSizeSlider.setShowTickMarks(true);
        gridSizeSlider.setShowTickLabels(true);
        gridSizeSlider.setMajorTickUnit(10);
        gridSizeSlider.setSnapToTicks(true);
        gridSizeSlider.setBlockIncrement(10);

        CheckBox fillCheckBox = new CheckBox("Заливка фигур");
        fillCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Состояние галочки \"Заливка фигур\" изменено: {} -> {}", oldValue, newValue);
        });

        Button generateButton = new Button("Генерация");
        generateButton.setPrefWidth(150);
        generateButton.setPrefHeight(40);
        canvas = new Canvas(900, 900);

        GridPane settingsGrid = new GridPane();
        settingsGrid.setPadding(new Insets(10));
        settingsGrid.setHgap(5);
        settingsGrid.setVgap(5);

        settingsGrid.add(minXLabel, 0, 0);
        settingsGrid.add(minXField, 1, 0);
        settingsGrid.add(maxXLabel, 0, 1);
        settingsGrid.add(maxXField, 1, 1);
        settingsGrid.add(minYLabel, 0, 2);
        settingsGrid.add(minYField, 1, 2);
        settingsGrid.add(maxYLabel, 0, 3);
        settingsGrid.add(maxYField, 1, 3);
        settingsGrid.add(densityLabel, 0, 4);
        settingsGrid.add(densitySlider, 1, 4);
        settingsGrid.add(gridSizeLabel, 0, 5);
        settingsGrid.add(gridSizeSlider, 1, 5);
        settingsGrid.add(figureSelection, 0, 6, 2, 1);
        settingsGrid.add(generateButton, 0, 8, 2, 1);
        settingsGrid.add(fillCheckBox, 0, 7, 2, 1);

        minXField.textProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Min X", oldValue, newValue));
        maxXField.textProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Max X", oldValue, newValue));
        minYField.textProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Min Y", oldValue, newValue));
        maxYField.textProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Max Y", oldValue, newValue));

        for (int i = 0; i < numFigureFields.size(); i++) {
            int finalI = i;
            numFigureFields.get(i).textProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Количество " + figureNames[finalI], oldValue, newValue));
        }

        densitySlider.valueProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Кучность", oldValue.toString(), newValue.toString()));
        gridSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> logParameterChange("Масштаб", oldValue.toString(), newValue.toString()));

        generateButton.setOnAction(e -> handleGenerateButton(figureCheckboxes, numFigureFields, figureNames, figureTypes,
                minXField, maxXField, minYField, maxYField, densitySlider, gridSizeSlider, fillCheckBox, canvas));

        HBox root = new HBox(10, settingsGrid, canvas);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 1300, 900);
        primaryStage.setTitle("Генератор случайных рисунков");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Обрабатывает событие нажатия кнопки "Генерация", выполняя генерацию и отрисовку фигур.
     * @param figureCheckboxes Список флажков выбора фигур.
     * @param numFigureFields Список полей ввода количества фигур.
     * @param figureNames Массив имен фигур.
     * @param figureTypes Список типов фигур.
     * @param minXField Поле ввода минимального значения X.
     * @param maxXField Поле ввода максимального значения X.
     * @param minYField Поле ввода минимального значения Y.
     * @param maxYField Поле ввода максимального значения Y.
     * @param densitySlider Ползунок для настройки плотности фигур.
     * @param gridSizeSlider Ползунок для настройки размера сетки.
     * @param fillCheckBox Флажок для выбора заливки фигур.
     * @param canvas Холст для отрисовки.
     */
    private void handleGenerateButton(List<CheckBox> figureCheckboxes, List<TextField> numFigureFields, String[] figureNames, List<Figure> figureTypes,
                                      TextField minXField, TextField maxXField, TextField minYField, TextField maxYField, Slider densitySlider, Slider gridSizeSlider, CheckBox fillCheckBox, Canvas canvas) {
        logger.info("Кнопка генерации нажата");
        try {
            double minX = parseDoubleAndValidate(minXField.getText(), "Min X");
            double maxX = parseDoubleAndValidate(maxXField.getText(), "Max X");
            double minY = parseDoubleAndValidate(minYField.getText(), "Min Y");
            double maxY = parseDoubleAndValidate(maxYField.getText(), "Max Y");
            double density = 1.5 - densitySlider.getValue();
            double gridSize = gridSizeSlider.getValue();
            boolean fillFigures = fillCheckBox.isSelected();
            double canvasHeight = canvas.getHeight();

            double correctedMinY = canvasHeight - maxY;
            double correctedMaxY = canvasHeight - minY;
            logger.info("Входные параметры:");
            logger.info("Min X: " + minX);
            logger.info("Max X: " + maxX);
            logger.info("Min Y: " + minY);
            logger.info("Max Y: " + maxY);
            logger.info("Кучность: " + density);
            logger.info("Масштаб: " + gridSize);
            logger.info("Выбранные фигуры:");
            List<Figure> activeFigures = new ArrayList<>();
            List<Integer> numFiguresList = new ArrayList<>();
            int totalFigures = 0;
            for (int i = 0; i < figureCheckboxes.size(); i++) {
                if (figureCheckboxes.get(i).isSelected()) {
                    String numStr = numFigureFields.get(i).getText();
                    int numFiguresForThisType;
                    try {
                        numFiguresForThisType = Integer.parseInt(numStr);
                        if (numFiguresForThisType < 0) {
                            throw new NegativeInputException("Количество фигур должно быть неотрицательным.");
                        }
                        if (numFiguresForThisType > 5000) {
                            throw new TooManyFiguresException("Количество фигур должно быть не больше 5000.");
                        }
                    } catch (NumberFormatException ex) {
                        throw new InvalidInputException("Неверный ввод (используйте целочисленные значения)");
                    }
                    activeFigures.add(figureTypes.get(i));
                    numFiguresList.add(numFiguresForThisType);
                    totalFigures += numFiguresForThisType;
                    logger.info("Количество {}: {} ", figureNames[i], numFiguresForThisType);
                }
            }


            int[] numFigures = numFiguresList.stream().mapToInt(Integer::intValue).toArray();

            if (activeFigures.isEmpty()) {
                throw new InvalidInputException("Ошибка: фигуры не выбраны");
            }
            if (minX >= maxX || correctedMinY >= correctedMaxY) {
                throw new InvalidInputException("Минимальное значение не может быть больше или равно максимальному");
            }

            generator = new Generator(minX, maxX, correctedMinY, correctedMaxY, numFigures, density, gridSize, activeFigures, fillFigures);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawGrid(gc, gridSize);
            gc.setFill(Color.rgb(0, 255, 0, 0.1));
            gc.fillRect(minX, correctedMinY, maxX - minX, correctedMaxY - correctedMinY);

            generator.draw(gc);
            logger.info("Фигуры сгенерированы.");

        } catch (TooManyFiguresException | InvalidInputException ex) {
            logger.error("Ошибка генерации фигур");
            showErrorAlert(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Ошибка: " + ex.getMessage());
            showErrorAlert("Ошибка: " + ex.getMessage());
        }
    }
    /**
     * Регистрирует событие изменения параметра.
     * @param parameterName Имя параметра.
     * @param oldValue Старое значение параметра.
     * @param newValue Новое значение параметра.
     */

    private void logParameterChange(String parameterName, String oldValue, String newValue) {
        if (!oldValue.equals(newValue)) {
            logger.info("Параметр изменен: {}: {} -> {}", parameterName, oldValue, newValue);
        }
    }
    /**
     * Преобразует строку в число с плавающей точкой и проверяет корректность ввода.
     * @param text Строка для преобразования.
     * @param fieldName Имя поля для сообщений об ошибках.
     * @return Преобразованное значение с плавающей точкой.
     * @throws InvalidInputException Если ввод некорректен.
     */
    private double parseDoubleAndValidate(String text, String fieldName) throws InvalidInputException {
        if (text == null || text.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " поле не может быть пустым.");
        }
        try {
            double value = Double.parseDouble(text);
            if (value < 0 || value != Math.floor(value) || value > 900) {
                throw new InvalidInputException(fieldName + " значение должно быть неотрицательным целым числом(не привышающим 900).");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new InvalidInputException(fieldName + " значение должно быть числом.");
        }
    }
    /**
     * Рисует сетку на холсте.
     * @param gc Контекст графики для рисования.
     * @param gridSpacing Расстояние между линиями сетки.
     */
    private void drawGrid(GraphicsContext gc, double gridSpacing) {
        gc.setStroke(Color.LIGHTGRAY);
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        for (double x = gridSpacing; x < canvasWidth; x += gridSpacing) {
            gc.strokeLine(x, 0, x, canvasHeight);
        }

        for (double y = gridSpacing; y < canvasHeight; y += gridSpacing) {
            gc.strokeLine(0, y, canvasWidth, y);
        }
    }
    /**
     * Точка входа в приложение. Запускает графический интерфейс.
     */
    public static void main(String[] args) {
        launch(args);
    }
}