package drawing;

import graph.CanvasPanel;

public interface Drawable {

    void pushToCanvas(CanvasPanel canvas);
    void updateDrawingInterpretation();

}
