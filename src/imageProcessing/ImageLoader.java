package imageProcessing;

import models.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(new File("images/src/" + path + ".bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage load(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(Integer[] res, String fileName, BufferedImage image) {
        BufferedImage resImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                resImage.setRGB(i, j, new Pixel(res[i * image.getWidth() + j],
                        res[i * image.getWidth() + j],
                        res[i * image.getWidth() + j])
                        .toInteger()
                );
            }
        }

        File outputFile = new File(fileName + ".bmp");
        try {
            ImageIO.write(resImage, "bmp", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(Integer[][] res, String fileName, BufferedImage image) {
        save(res, res, res, fileName, image);
    }

    public static void save(Integer[][] red, Integer[][] green, Integer[][] blue, String fileName, BufferedImage image) {
        BufferedImage resImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                resImage.setRGB(j, i, new Pixel(red[i][j], blue[i][j], green[i][j]).toInteger());
            }
        }

        File outputFile = new File(fileName + ".bmp");
        try {
            ImageIO.write(resImage, "bmp", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(String fileName, BufferedImage image) {
        File outputFile = new File(fileName + ".bmp");
        try {
            ImageIO.write(image, "bmp", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
