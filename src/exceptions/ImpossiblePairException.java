package exceptions;

/**
 * Исключение, используемое в случае попытки создания невозможной пары объектов <br>
 * (см {@link physics.CollisionalPair} и {@link geometry.intersections.IntersectionalPair})
 */
public class ImpossiblePairException extends Exception{
    /**
     * Конструктор исключения
     * @param message сообщение об ошибке
     */
    public ImpossiblePairException(String message){
        super(message);
    }
}
