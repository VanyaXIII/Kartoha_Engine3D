package drawing;

import graph.CanvasPanel;

/**
 * Интерфейс предоставляющий методы для отрисовки объекта
 */
public interface Drawable {
    /**
     * Метод, реализующий добавление объекта на канвас
     * @param canvas канвас, на котором нужном отрисовывать объект
     */
    void pushToCanvas(CanvasPanel canvas);

    /**
     * Метод, обновляющий графическую интерпретацию объекта
     */
    void updateDrawingInterpretation();

}
