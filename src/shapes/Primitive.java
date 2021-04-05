package shapes;

import geometry.objects.Shape;

import java.io.IOException;

/**
 * Доступные примитивы(многогранники)
 */
public enum Primitive {

    CUBE("src\\shapes\\assets\\cube.json"),
    PYRAMID("src\\shapes\\assets\\pyramid.json"),
    OCTAHEDRON("src\\shapes\\assets\\octahedron.json"),
    TETRAHEDRON("src\\shapes\\assets\\tetrahedron.json");


    private final String path;

    /**
     * Конструктор по пути к JSON файлу, в котором хранится информация
     * @param path путь к файлу
     */
    Primitive(String path){
        this.path = path;
    }

    /**
     * @return Форма, образующая данный примитив
     * @throws IOException исключение в случае проблем с открытием файла
     */
    public Shape get() throws IOException {
        return (Shape) new JsonReader(path).read(Shape.class);
    }
}
