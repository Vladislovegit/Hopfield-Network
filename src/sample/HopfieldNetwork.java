package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class HopfieldNetwork {
    private Integer[][] weights;

    public HopfieldNetwork(ArrayList<Integer[]> images) {
        Integer size = images.get(0).length;
        for (Integer[] image : images) {
            if (image.length != size) {
                throw new IllegalArgumentException("Images must be the same size.");
            }
        }
        weights = teach(images);
    }

    private Integer[][] teach(ArrayList<Integer[]> images) {
        Integer neuronsCount = images.get(0).length;

        Integer[][] weights = new Integer[neuronsCount][neuronsCount];

        for (int i = 0; i < neuronsCount; i++) {
            for (int j = 0; j < neuronsCount; j++) {
                if (i == j)
                    weights[i][j] = 0;
                else {
                    Integer sum = 0;
                    for (Integer[] image : images) {
                        sum += image[i] * image[j];
                    }
                    weights[i][j] = sum;
                }
            }
        }
        return weights;
    }

    public Integer[] recognize(Integer[] image) {
        if (weights.length != image.length) {
            throw new IllegalArgumentException("Illegal image's length.");
        }
        Integer length = image.length;

        Integer[] oldImage = image.clone();
        Integer[] newImage = new Integer[length];

        while (true) {
            for (int i = 0; i < length; i++) {
                Integer sum = 0;
                for (int j = 0; j < length; j++) {
                    sum += weights[i][j] * oldImage[j];
                }

                if (sum > 0)
                    newImage[i] = 1;
                else
                    newImage[i] = -1;
            }

            if (Arrays.equals(oldImage, newImage))
                break;
            else {
                Integer[] tmp = newImage;
                newImage = oldImage;
                oldImage = tmp;
            }
        }
        return newImage;
    }
}
