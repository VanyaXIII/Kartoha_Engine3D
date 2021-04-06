package primitives;

import geometry.objects.Shape;

import java.io.IOException;

/**
 * Доступные примитивы(многогранники)
 */
public enum Primitive {

    CUBE("primitives/cube.json"),
    PYRAMID("primitives/pyramid.json"),
    OCTAHEDRON("primitives/octahedron.json"),
    TETRAHEDRON("primitives/tetrahedron.json");


    private final String path;

    /**
     * Конструктор по пути к JSON файлу, в котором хранится информация
     * @param path путь к файлу
     */
    Primitive(String path) {
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
