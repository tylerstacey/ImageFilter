
/**
 * Green.java converts rgb to green scale
 * 
 * Copyright (C) 2013  Tyler Stacey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 */

package imagefilter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.IOException;

import static java.lang.System.out;

import javax.imageio.ImageIO;

/**
 * Green.java converts rgb to green scale
 *
 * @author Tyler Stacey, tas384, 201033446
 */
public class Green {

    public String resultName;
    public String path;

    /**
     * Green accepts the path to the file as an argument and assigns it to the
     * class's path variable.
     *
     * @invariants NIL
     * 
     * @param path the path to the image file.
     */
    public Green(String path) {
        this.path = path;
    }

    /**
     * getBaseName gets the name of the original file, file type not included
     * 
     * @pre name != null
     * @post name.substring(0, last).length() > 0
     * 
     * @param name the path to the image file.
     * @return substring containing the filename.
     */
    public static String getBaseName(String name) {
        //Ensure the name is not null
        assert(name != null);
        
        int last = name.lastIndexOf('.');
        if (last == -1) {
            return name;
        }
        assert(name.substring(0, last).length() > 0);
        return name.substring(0, last);
    }

    /**
     * getSuffix gets the file type.
     *
     * @pre name != null
     * @post "jpeg".equals(name.substring(last + 1)) || 
             "jpg".equals(name.substring(last + 1)) || 
             "png".equals(name.substring(last + 1))
     * 
     * @param name name of the file
     * @return the filetype as a string
     */
    public static String getSuffix(String name) {
        //Ensure the name is not null
        assert(name != null);
        
        int last = name.lastIndexOf('.');
        if (last == -1) {
            return "";
        }
        
        assert ("jpeg".equals(name.substring(last + 1)) || 
                "jpg".equals(name.substring(last + 1)) || 
                "png".equals(name.substring(last + 1)));
        
        return name.substring(last + 1);
    }

    /**
     * Converts the image pixel by pixel and outputs a new file with the "-green"
     * postfix to denote the changed file.
     *
     * @throws IOException
     */
    public void convert() throws IOException {
        String filename = path;
        String suffix = getSuffix(filename);
        resultName = getBaseName(filename) + "-green." + suffix;

        BufferedImage bi = ImageIO.read(new File(filename));
        int w = bi.getWidth();
        int h = bi.getHeight();
        WritableRaster raster = bi.getRaster();
        int[] samples = new int[3]; // RGB samples
        out.println("Converting Image");
        // convert image to green scale
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                raster.getPixel(x, y, samples);
                int avg = (samples[0] + samples[1] + samples[2]) / 3;
                if (avg < 63) {
                    samples[0] = 0;
                    samples[1] = 255;
                    samples[2] = 0;
                } else if (avg >= 63 && avg < 127) {
                    samples[0] = 63;
                    samples[1] = 255;
                    samples[2] = 63;
                } else if (avg >= 127 && avg < 191) {
                    samples[0] = 127;
                    samples[1] = 255;
                    samples[2] = 127;
                } else if (avg >= 191 && avg < 255) {
                    samples[0] = 191;
                    samples[1] = 255;
                    samples[2] = 191;
                } else if (avg == 255) {
                    samples[0] = 255;
                    samples[1] = 255;
                    samples[2] = 255;
                }
                raster.setPixel(x, y, samples);

            }
        }
        out.println("Image Converted");
        ImageIO.write(bi, suffix, new File(resultName));
    }
}
