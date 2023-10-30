/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomFonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author ndila
 */
public class CustomFonts {
    public Font komikz(float font_size){
        try{
            InputStream fontStream = getClass().getResourceAsStream("KOMIKZ 400.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(font_size);
            return customFont;
        }
        catch(IOException | FontFormatException e){
            e.printStackTrace();
        }
        return new Font("Arial", Font.PLAIN, 16);
    }
}
