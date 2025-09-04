package com.chess.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility class for loading images from various sources
 */
public class ImageLoader {
    
    /**
     * Load image from file system path
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Failed to load image from file: " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load image from classpath resources
     */
    public static BufferedImage loadImageFromResources(String path) {
        try {
            var inputStream = ImageLoader.class.getResourceAsStream(path);
            if (inputStream == null) {
                System.err.println("Resource not found: " + path);
                return null;
            }
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to load image from resources: " + path);
            e.printStackTrace();
            return null;
        }
    }
}
