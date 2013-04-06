
/**
 * Rotate.java rotates an image 90 degrees CW.
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
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import static java.lang.System.out;

import javax.imageio.ImageIO;

/**
 * Rotate.java rotates an image 90 degrees CW.
 *
 * @author Tyler Stacey, tas384, 201033446
 */
public class Rotate {

    public String resultName;
    public String path;

    /**
     * Rotate accepts the path to the file as an argument and assigns it to the
     * class's path variable.
     *
     * @invariants NIL
     * 
     * @param path the path to the image file.
     */
    public Rotate(String path) {
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
     * Rotates the image by swapping pixel locations then using Graphics2D to 
     * flip the image vertically and adds "-rotate" postfix to denote the 
     * changed file.
     *
     * @throws IOException
     */
    public void convert() throws IOException {
        String filename = path;
        String suffix = getSuffix(filename);
        resultName = getBaseName(filename) + "-rotate." + suffix;

        out.println("Converting Image");
        BufferedImage bi = ImageIO.read(new File(filename));
        int type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();
        int width = bi.getWidth();
        int height = bi.getHeight();
        BufferedImage biFlip = new BufferedImage(height, width, type);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                biFlip.setRGB(height - 1 - j, width - 1 - i, bi.getRGB(i, j));
            }
        } 
        width = biFlip.getWidth();
        height = biFlip.getHeight();
        BufferedImage dimg = new BufferedImage(width, height, biFlip.getColorModel().getTransparency());  
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(biFlip, 0, 0, width, height, 0, height, width, 0, null);  
        g.dispose();

        out.println("Image Converted");
        ImageIO.write(dimg, suffix, new File(resultName));

    }
}
