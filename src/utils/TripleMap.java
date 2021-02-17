package utils;

import java.util.HashMap;
import java.util.Map;

public final class TripleMap <FirstKeyType, SecondKeyType, ElementType>{

    private final Map<FirstKeyType, Map<SecondKeyType, ElementType>> map;

    {
        map = new HashMap<>();
    }

    public void clear(){
        map.clear();
    }

    public ElementType getElement(FirstKeyType firstKey, SecondKeyType secondKey){
        return map.get(firstKey).get(secondKey);
    }

    public void putByFirstKey(FirstKeyType firstKey, SecondKeyType secondKey, ElementType element){
        map.get(firstKey).put(secondKey, element);
    }

    public void addFirstKey(FirstKeyType firstKey){
        map.put(firstKey, new HashMap<>());
    }
}
