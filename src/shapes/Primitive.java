package shapes;

import geometry.objects.Shape;

import java.io.IOException;

public enum Primitive {

    CUBE("src\\shapes\\assets\\cube.json"),
    PYRAMID("src\\shapes\\assets\\pyramid.json"),
    OCTAHEDRON("src\\shapes\\assets\\octahedron.json"),
    TETRAHEDRON("src\\shapes\\assets\\tetrahedron.json");


    private final String path;

    Primitive(String path){
        this.path = path;
    }

    public Shape get() throws IOException {
        return (Shape) new JsonReader(path).read(Shape.class);
    }
}
