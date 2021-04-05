package exceptions;

/**
 * Исключение, используемое в случае попытки создания невозможного физического объекта
 */
public class ImpossibleObjectException extends Exception {
    /**
     * Конструктор исключения
     * @param message сообщение об ошибке
     */
    public ImpossibleObjectException(String message) {
        super(message);
    }
}
