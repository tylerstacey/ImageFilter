
/**
 * Resize.java changes the size of the image based on a percentage input.
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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import static java.lang.System.out;

import javax.imageio.ImageIO;

/**
 * Resize.java changes the size of the image based on a percentage input.
 *
 * @author Tyler Stacey, tas384, 201033446
 */
public class Resize {

    public String resultName;
    public String path;
    public double scale;
    public int IMG_HEIGHT, IMG_WIDTH;

    /**
     * Resize accepts the path to the file as an argument and assigns it to the
     * class's path variable and accepts the scale as a percentage to what the
     * image will be converted.
     *
     * @invariants NIL
     * 
     * @param path the path to the image file.
     * @param s the scale.
     */
    public Resize(String path, double s) {
        this.scale = s / 100;
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
     * Converts the image usinf Graphics2D and Affine transformation and outputs 
     * a new file with the "-resize" postfix to denote the changed file.
     *
     * @throws IOException
     */
    public void convert() throws IOException {
        String filename = path;
        String suffix = getSuffix(filename);
        resultName = getBaseName(filename) + "-resize." + suffix;

        out.println("Converting Image");
        BufferedImage bi = ImageIO.read(new File(filename));
        int type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();

        IMG_HEIGHT = new Double(bi.getHeight() * scale).intValue();
        IMG_WIDTH = new Double(bi.getWidth() * scale).intValue();

        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(bi, AffineTransform.getScaleInstance(scale, scale), null);

        out.println("Image Converted");
        ImageIO.write(resizedImage, suffix, new File(resultName));

    }
}