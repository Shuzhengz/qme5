package org.qme.client.vis.tex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32;
import org.qme.io.Logger;
import org.qme.io.Severity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.json.*;

/**
 * Class responsible for loading textures from files
 * @author cameron
 * @since 0.2.0
 */
public class TextureManager {

    private static final String TEXTURE_RESOURCES = "/textures/";
    private static final String MISSING_TEXTURE = "/textures/tiles/missing.png";
    private static final HashMap<String, Integer> textures = new HashMap<>();

    /**
     * Initialising TextureManager will load textures in the resources directory
     * Inorder to avoid loading textures twice this should only be initialised once
     */
    public TextureManager() {
        ArrayList<String> toLoad = new ArrayList<>();
        toLoad.add("tiles/missing.png");
        toLoad.add("tiles/forest.png");
        toLoad.add("tiles/plains.png");
        toLoad.add("tiles/fertile-plains.png");
        toLoad.add("tiles/mountain.png");
        toLoad.add("tiles/ocean.png");
        toLoad.add("tiles/sea.png");
        toLoad.add("tiles/high-mountain.png");
        toLoad.add("tiles/desert.png");
        toLoad.add("tiles/jungle.png");
        toLoad.add("tiles/tundra.png");
        toLoad.add("items/coal.png");
        toLoad.add("items/fruit.png");
        toLoad.add("items/gold-ore.png");
        toLoad.add("items/grape-vine.png");
        toLoad.add("items/grass.png");
        toLoad.add("items/leaf.png");
        toLoad.add("items/salt.png");
        toLoad.add("items/sea-weed.png");
        toLoad.add("items/tall-grass.png");
        toLoad.add("tiles/hover.png");
        toLoad.add("items/snow.png");
        toLoad.add("items/rock.png");
        toLoad.add("items/sand.png");
        toLoad.add("items/sand-stone.png");
        toLoad.add("items/grapes.png");
        toLoad.add("items/lumber.png");
        toLoad.add("items/cactus.png");
        toLoad.add("items/wheat.png");
        toLoad.add("items/seagull.png");
        toLoad.add("misc/box.png");
        toLoad.add("misc/button.png");
        for (String texture : toLoad) {
            registerTexture(texture, loadTextureFromImage(Objects.requireNonNull(loadImageResource
                    (TEXTURE_RESOURCES + texture))));
        }
    }

    /**
     * Registers a texture
     * @param name the name of the texture
     * @param id the id of the texture
     */
    public static void registerTexture(String name, int id) {
        if (textures.containsKey(name) || textures.containsValue(id)) {
            Logger.log("Failed to load texture " + name + " id " + id + " because a texture with the a common identifier exists.", Severity.WARNING);
            return;
        }
        Logger.log("Loaded texture: " + name, Severity.NORMAL);
        textures.put(name, id);
    }

    /**
     * Gets a texture by name
     * @param name the name of the texture
     * @return the texture id or null if the texture could not be found
     */
    public static Integer getTexture(String name) {
        return textures.getOrDefault(name, null);
    }

    /**
     * Loads a opengl texture from an image
     * @param image Image file to load
     * @return Id of the loaded texture
     */
    public static int loadTextureFromImage(BufferedImage image) {

        GL32.glEnable(GL32.GL_TEXTURE_2D);

        int height = image.getHeight();
        int width = image.getWidth();

        int area = height * width;

        int[] pixelBuf = new int[area];
        image.getRGB(0, 0, width, height, pixelBuf, 0, width);
        ByteBuffer buf = BufferUtils.createByteBuffer(area * 4);

        tryLoadTextureFromImage(height, width, pixelBuf, buf);

        return loadTextureFromBuffer(buf, width, height);
    }

    public static void tryLoadTextureFromImage(int height, int width, int[] pixelBuf, ByteBuffer buf) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int pixel = pixelBuf[y * width + x];

                // Get RGB
                buf.put((byte) ((pixel >> 16) & 0xFF));
                buf.put((byte) ((pixel >> 8) & 0xFF));
                buf.put((byte) (pixel & 0xFF));

                // Get alpha
                buf.put((byte) ((pixel >> 24) & 0xFF));

            }
        }

        buf.flip();
    }

    /**
     * Loads texture from byte buffer
     * @param buffer the byte buffer to load
     * @return id of texture
     */
    public static int loadTextureFromBuffer(ByteBuffer buffer, int width, int height) {

        // The id generated by GL
        int id = GL32.glGenTextures();

        GL32.glBindTexture(GL32.GL_TEXTURE_2D, id);

        GL32.glTexImage2D(
                GL32.GL_TEXTURE_2D, 0, GL32.GL_RGBA,
                width, height,
                0, GL32.GL_RGBA, GL32.GL_UNSIGNED_BYTE, buffer
        );

        GL32.glActiveTexture(GL32.GL_TEXTURE0);

        GL32.glGenerateMipmap(GL32.GL_TEXTURE_2D);

        return id;
    }

    /**
     * Gets an image from resources
     * @param path path of the file
     * @return an image
     */
    private static BufferedImage loadImageResource(String path) {

        // If the texture doesn't exist try to load fallback texture
        if (TextureManager.class.getResource(path) == null) {
            Logger.log("Could not find texture " + path, Severity.ERROR);

            // Attempt to load fallback texture so the application can at least start

            if (TextureManager.class.getResource(MISSING_TEXTURE) == null) {
                Logger.log("Could not find fallback texture", Severity.FATAL);
                return null;
            }

            try {
                return ImageIO.read(TextureManager.class.getResource(MISSING_TEXTURE));
            } catch (IOException e) {
                Logger.log("Could not load fallback texture", Severity.FATAL);
                e.printStackTrace();
                return null;
            }

        }

        try {
            return ImageIO.read(TextureManager.class.getResource(path));
        } catch (IOException e) {
            // Some unforeseen error has occurred. Don't try to load fallback texture
            Logger.log("Could not load texture " + path, Severity.FATAL);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads an atlas for a texture
     * @param resource the location of the atlas json file
     * @return a list of regions for the texture
     */
    public static HashMap<String, Rectangle> loadAtlas(String resource) {
        HashMap<String, Rectangle> toReturn = new HashMap<>();
        JSONObject regions = null;

        InputStream stream =TextureManager.class.getResourceAsStream(resource);
        regions = new JSONObject(new JSONTokener(stream));

        if (regions == null) {
            return toReturn;
        }

        for (String object : regions.keySet()) {
            JSONObject region = regions.getJSONObject(object);
            Rectangle rectangle = new Rectangle(region.getInt("x"), region.getInt("y"), region.getInt("width"), region.getInt("height"));
            toReturn.put(object, rectangle);
        }

        return toReturn;
    }

}
