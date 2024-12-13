package com.example.cursach;
/**
 * Класс Errors представляет собой базовый класс для всех пользовательских исключений в приложении.
 */
public class Errors extends Exception {
    /**
     * Конструктор класса Errors.
     * @param message Сообщение об ошибке.
     */
    public Errors(String message) {
        super(message);
    }
}
/**
 * Класс InvalidInputException представляет собой исключение, возникающее при вводе неверных данных пользователем.
 */
class InvalidInputException extends Errors {
    /**
     * Конструктор класса InvalidInputException.
     * @param message Сообщение об ошибке.
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
/**
 * Класс TooManyFiguresException представляет собой исключение, возникающее при попытке сгенерировать слишком большое количество фигур.
 */
class TooManyFiguresException extends Errors {
    /**
     * Конструктор класса TooManyFiguresException.
     * @param message Сообщение об ошибке.
     */
    public TooManyFiguresException(String message) {
        super(message);
    }
}
/**
 * Класс NegativeInputException представляет собой исключение, возникающее при попытке использовать отрицательные значения в качестве входных данных.
 */
class NegativeInputException extends Exception {
    /**
     * Конструктор класса NegativeInputException.
     * @param message Сообщение об ошибке.
     */
    public NegativeInputException(String message) {
        super(message);
    }
}