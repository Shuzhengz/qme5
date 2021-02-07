package org.qme.client;

import org.qme.client.vis.LoadingBar;
import org.qme.client.vis.gui.*;
import org.qme.client.vis.gui.QButton;
import org.qme.client.vis.wn.GLFWInteraction;
import org.qme.client.vis.wn.Scrolling;
import org.qme.io.AudioFiles;
import org.qme.io.AudioPlayer;
import org.qme.utils.Performance;
import org.qme.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The "controller", so to speak, of all events. It also helps to validate
 * requests from the player or the game, because instead of acting on anything
 * directly, all UI components (should) send in a request to the global
 * Application instance. For when QME goes online at some later date (hopefully)
 * this model will serve better, because request objects can be sent to a remote
 * location more easily.
 * @author adamhutchings
 * @since 0.1.0
 */
public final class Application {

	private int frameCount;
	private int fps;
	private long lastSecond;

	public static QBox box;
	public static QLabel debugLabel;
	public static QLabel profilerLabel;

	public static final int RENDER_SCALE = 3;

	/**
	 * Make audio player
	 */
	public static AudioPlayer audioPlayer = new AudioPlayer(AudioFiles.menu);

  	/**
   	 * All mouse responders
	 */
	private static final ArrayList<MouseResponder> responders = new ArrayList<>();

	/**
	 * Add an object
	 */
	public static void registerMouseResponder(MouseResponder r) {
		responders.add(r);
	}

	/**
	 * Get the responder list
	 */
	public static ArrayList<MouseResponder> getResponders() {
		return responders;
	}

	/**
	 * The constructor is private. Only one instance allowed.
	 */
	private Application() {
		new World();
		QFont labelMono = new QFont(new Font(Font.MONOSPACED, Font.PLAIN, 12), true);
		QFont buttonFont = new QFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 18), true);

		// Debug Label
		debugLabel = new QLabel(labelMono, "...", 5, GLFWInteraction.windowSize() - (labelMono.getHeight() + 2), true);
		debugLabel.setVisible(false);

		// Profiler Label
		profilerLabel = new QLabel(labelMono, "...", 5, 5, false);
		profilerLabel.setVisible(false);

		// Update debug information
		Performance.updateValues();

		// Close loading bar
		LoadingBar.done();

		// Create test button
		QButton button = new QButton(buttonFont, "Le Test Button", GLFWInteraction.getSize() / 2, GLFWInteraction.getSize() / 2, 180, 70) {
			@Override
			protected void action() {
				this.setClickable(false);
			}
		};

	}

	/**
	 * The global instance of application.
	 */
	public static final Application app = new Application();
	
	/**
	 * Run the application forever (or until an exit request is sent)
	 */
	public void mainloop() {

		GUIManager.loadGUIs();
    
		while (GLFWInteraction.shouldBeOpen()) {

			Performance.beginFrame();

			Scrolling.moveWorld();

			GLFWInteraction.repaint();

			// Updates debug label each frame
			debugLabel.text = "Running game version v" + Performance.GAME_VERSION + "" +
					"\nJVM: " + Performance.JAVA_VERSION + " (Vendor: " + Performance.JAVA_VENDOR + ")" +
					"\nOperating System: " + Performance.OPERATING_SYSTEM + " (Arch: " + Performance.ARCH_TYPE + ") (Version: " + Performance.OPERATING_SYSTEM_VERSION + ")" +
					"\nGraphics: " + Performance.GPU_NAME + " " + Performance.GPU_VENDOR +
					"\nMemory: (Max: " + Runtime.getRuntime().totalMemory() / 1000000 + "mb) (Used: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000 + "mb)" +
					"\nProcessor: " + Performance.CPU +
					"\nFPS: " + fps + " (On: " + frameCount + ")";

			// Updates profiler data
			HashMap<String, Float> timings = Performance.getTimings();
			profilerLabel.text = "Profiler [Render] [Tick] [Other]" +
					"\nRender: " + timings.getOrDefault("render",0F) + "ms" +
					"\nTick: " + timings.getOrDefault("tick", 0F) + "ms" +
					"\nTotal: " + Performance.getTotal() + "ms";

			// Calculates fps
			if (System.currentTimeMillis() - lastSecond > 1000) {
				fps = frameCount;
				frameCount = 0;
				lastSecond = System.currentTimeMillis();
			} else {
				frameCount++;
			}

		
		}
		
	}

}
