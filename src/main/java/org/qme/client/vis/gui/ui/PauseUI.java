package org.qme.client.vis.gui.ui;

import org.qme.client.vis.gui.GUI;
import org.qme.client.vis.gui.GUIManager;
import org.qme.client.vis.gui.UIComponent;
import org.qme.client.vis.gui.comp.QButton;
import org.qme.client.vis.gui.comp.QFont;
import org.qme.client.vis.gui.comp.QLabel;
import org.qme.client.vis.wn.GLFWInteraction;
import org.qme.io.Logger;
import org.qme.io.Severity;
import org.qme.utils.OSType;

import java.awt.*;

/**
 * This UI shows pause information
 * @since 0.4.0
 * @author cameron
 */
public class PauseUI extends GUI {

    private static final int BUTTON_WIDTH = 300;

    public QLabel label;
    public QButton quit;
    public QButton back;
    public QButton options;

    /**
     * Creates a new PauseUI
     */
    public PauseUI() {

        QFont main = new QFont(new Font(Font.MONOSPACED, Font.PLAIN, 16), true);
        QFont title;
        if (OSType.mac()) {
            title = main; // Trash mac users break my prs :^)
        } else {
            title = new QFont(new Font(Font.MONOSPACED, Font.BOLD, 32), true);
        }

        int half_win_size = GLFWInteraction.getSize() / 2;
        label = new QLabel(title, "Game Paused", half_win_size - title.getWidth("Game Paused") / 2, half_win_size + 40, Color.WHITE);
        quit = new QButton(main, "Quit Game", new Rectangle(half_win_size - BUTTON_WIDTH / 2, half_win_size - 20, BUTTON_WIDTH, 40), Color.WHITE) {
            @Override
            protected void action() {
                if (isVisible()) {
                    Logger.log("Close game button pressed. Exiting.", Severity.NORMAL);
                    System.exit(0);
                }
            }
        };
        options = new QButton(main, "Options", new Rectangle(half_win_size - BUTTON_WIDTH / 2, half_win_size - 70, BUTTON_WIDTH, 40), Color.WHITE) {
            @Override
            protected void action() {
                if (isVisible()) {
                    hide();
                    GUIManager.optionsUI.show();
                }
            }
        };
        back = new QButton(main, "Return to Game", new Rectangle(half_win_size - BUTTON_WIDTH / 2, half_win_size - 120, BUTTON_WIDTH, 40), Color.WHITE) {
            @Override
            protected void action() {
                if (isVisible()) {
                    hide();
                }
            }
        };

        components = new UIComponent[] {
                label,
                back,
                options,
                quit
        };

        hide();

    }

}
