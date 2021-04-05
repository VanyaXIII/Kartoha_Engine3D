package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Обертка для двойной Map
 * @param <FirstKeyType> тип первого ключа
 * @param <SecondKeyType> тип второго ключа
 * @param <ElementType> тип элемента
 */
public final class TripleMap <FirstKeyType, SecondKeyType, ElementType>{

    private final Map<FirstKeyType, Map<SecondKeyType, ElementType>> map;

    {
        map = new HashMap<>();
    }

    /**
     * Метод, очищающий коллекцию
     */
    public void clear(){
        map.clear();
    }

    /**
     * Метод, реализующий получение элемента по двум ключам
     * @param firstKey первый ключ
     * @param secondKey второй ключ
     * @return Элемент, соответствующий ключам
     */
    public ElementType getElement(FirstKeyType firstKey, SecondKeyType secondKey){
        return map.get(firstKey).get(secondKey);
    }

    /**
     * Метод, реализующий добавление элемента по первому ключу
     * @param firstKey перывый ключ
     * @param secondKey второй ключ
     * @param element дообавляемый элемент
     */
    public void putByFirstKey(FirstKeyType firstKey, SecondKeyType secondKey, ElementType element){
        map.get(firstKey).put(secondKey, element);
    }

    /**
     * Метод, реализующий добавление нового первого ключа
     * @param firstKey добавляемый первый ключ
     */
    public void addFirstKey(FirstKeyType firstKey){
        map.put(firstKey, new HashMap<>());
    }
}
