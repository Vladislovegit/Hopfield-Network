package imageProcessing;

import models.Pixel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class ImageProcessing {

    public static Integer[][] getImage(String path) {
        BufferedImage bf = ImageLoader.load(path);
        if (bf == null) {
            throw new IllegalArgumentException("Illegal path to image.");
        } else {
            Integer[][] res = new Integer[bf.getHeight()][bf.getWidth()];
            for (int i = 0; i < bf.getWidth(); i++) {
                for (int j = 0; j < bf.getHeight(); j++) {
                    res[i][j] = new Pixel(bf.getRGB(i, j)).getBrightness();
                }
            }
            return res;
        }
    }

    public static Integer[] getImageVector(Integer[][] image) {
        Integer[] res = new Integer[image.length * image.length];
        for (int i = 0; i < image.length; i++) {
            System.arraycopy(image[i], 0, res, i * image.length, image[0].length);
        }
        return res;
    }

    public static Integer[] getBipolarVector(Integer[][] image) {
        Integer[] res = new Integer[image.length * image.length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j] > 0) {
                    res[i * image.length + j] = 1;
                } else if (image[i][j] == 0) {
                    res[i * image.length + j] = -1;
                }
            }
        }
        return res;
    }

    public static Integer[] getBipolarVector(Integer[] image) {
        Integer[] res = new Integer[image.length];
        for (int i = 0; i < image.length; i++) {
            if (image[i] > 0) {
                res[i] = 1;
            } else if (image[i] == 0) {
                res[i] = -1;
            }
        }
        return res;
    }

    public static Integer[][] getImageValuesFromBipolarVector(Integer[] vector, int width, int height) {
        Integer[][] res = new Integer[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (vector[i * width + j] > 0) {
                    res[i][j] = 255;
                } else if (vector[i * width + j] < 0) {
                    res[i][j] = 0;
                }
            }
        }

        return res;
    }

    static public Integer[] applyNoise(Integer[] image, Integer percentageOfNoise) {
        if (percentageOfNoise < 0 || percentageOfNoise > 100) {
            throw new IllegalArgumentException("Illegal percentage of noise.");
        }

        Integer noiseCount = image.length * percentageOfNoise / 100;

        Random random = new Random();

        ArrayList<Integer> noiseIndexes = new ArrayList<>(noiseCount);
        for (int i = 0; i < noiseCount; i++) {
            int rnd = random.nextInt(image.length);
            if (!noiseIndexes.contains(rnd)) {
                noiseIndexes.add(rnd);
            } else {
                i--;
            }
        }

        Integer[] noiseImage = image.clone();

        for (int i = 0; i < noiseCount; i++) {
            if (noiseImage[noiseIndexes.get(i)] > 0) {
                noiseImage[noiseIndexes.get(i)] = 0;
            } else if (noiseImage[noiseIndexes.get(i)] == 0) {
                noiseImage[noiseIndexes.get(i)] = 255;
            }
        }
        return noiseImage;
    }
}
