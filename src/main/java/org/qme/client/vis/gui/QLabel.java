package org.qme.client.vis.gui;

import org.qme.client.vis.Renderable;
import org.qme.client.vis.wn.WindowManager;

/**
 * Class for creating labels
 * @author cameron
 * @since 0.3.0
 */
public class QLabel implements Renderable {

    public QFont font;
    public String text;
    public int x;
    public int y;

    /**
     * Creates a new label
     * @param font the QFont to use for the label
     * @param text the label text
     * @param x the label's x position
     * @param y the label's y position
     */
    public QLabel(QFont font, String text, int x, int y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;

        WindowManager.addObject(this);
    }

    @Override
    public void draw() {
        font.drawText(text, x, y);
    }
}
