package physics;

import limiters.Collisional;


/**Интерфейс, предоставляющий метод для обработки коллизии между двумя объектами {@link limiters.Collisional}
 */
public interface Collider<FirstThingType extends Collisional, SecondThingType extends Collisional>{

    void collide(FirstThingType firstThing, SecondThingType secondThing);

}
