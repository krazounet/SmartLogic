import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DrawTools
{
    public static void drawImageTransformed(BufferedImage source, BufferedImage image, int spotX, int spotY, double rotation, double zoom)
    {
        Graphics g = source.getGraphics();
        drawImageTransformed(g, image, spotX, spotY, rotation, zoom);
    }
    
    public static void drawImageTransformed(Graphics g, BufferedImage image, int spotX, int spotY, double rotation, double zoom)
    {
        if(zoom != 100)
        {
            BufferedImage cardResized = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            AffineTransform at = new AffineTransform();
            at.translate((image.getWidth() - image.getWidth() * zoom / 100) / 2, (image.getHeight() - image.getHeight() * zoom / 100) / 2);
            at.scale(zoom / 100, zoom / 100);
//			at.translate(-card.getWidth() / 2, -card.getHeight() / 2);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            cardResized = scaleOp.filter(image, cardResized);
            image = cardResized;
        }

        if(rotation != 0)
        {
            BufferedImage cardRotated = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(rotation), image.getWidth() / 2, image.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            op.filter(image, cardRotated);
            image = cardRotated;
        }

        drawImageCenter(g, image, spotX, spotY);
    }

    public static void drawImageCenter(Graphics g, BufferedImage image, double centerX, double centerY)
    {
        double coordX = centerX - (image.getWidth() / 2);
        double coordY = centerY - (image.getHeight() / 2);
        g.drawImage(image, (int)coordX, (int)coordY, null);
    }

    public static BufferedImage getImage(String filename)
    {
        BufferedImage imageToReturn = null;

        try
        {
            File imageF = new File(filename);
            imageToReturn = ImageIO.read(imageF);
        }
        catch(IOException x)
        {
            x.printStackTrace();
            System.out.println(filename);

        }

        return(imageToReturn);
    }

    public static void saveFile(BufferedImage imageToSave, String filename)
    {
        File f = new File(filename);
        try
        {
            ImageIO.write(imageToSave, "PNG", f);
        }
        catch(IOException xx)
        {
            xx.printStackTrace();
        }
    }

    public static void drawText(BufferedImage image, String text, int x, int y, String fontname, Color color, int size, double rotation)
    {
        Graphics g = image.getGraphics();
        Graphics2D g2d = (Graphics2D)g;
        Font font = new Font(fontname, Font.BOLD, size);
        g2d.setColor(color);
        g2d.setFont(font);
        Rectangle sizeText = getStringBounds(g2d, text);

        int maxSize = Math.max(sizeText.width, sizeText.height);
        BufferedImage imageVierge = new BufferedImage(maxSize * 2, maxSize * 2, image.getType());
        g = imageVierge.getGraphics();
        g2d = (Graphics2D)g;
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.drawString(text, maxSize - (sizeText.width / 2) - sizeText.x, maxSize - (sizeText.height / 2) - sizeText.y);

        drawImageTransformed(image.getGraphics(), imageVierge, x, y, rotation, 100);
    }

    public static Rectangle getStringBounds(Graphics g, String str)
    {
        Graphics2D g2 = (Graphics2D)g;
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0, 0);
    }
    public static void drawLine (BufferedImage img,int x1, int y1, int x2, int y2, Color color){
        Graphics g = img.getGraphics();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(12f));//épaisseur du trait
        g2d.drawLine(x1, y1, x2, y2);
    }
    public static BufferedImage Zoom (BufferedImage source, int zoom){
        int largeur = source.getWidth()*zoom/100;
        int hauteur = source.getHeight()*zoom/100;
        BufferedImage fond = new BufferedImage(largeur,hauteur,BufferedImage.TYPE_INT_ARGB);
        BufferedImage image_zoomee = new BufferedImage(largeur,hauteur,BufferedImage.TYPE_INT_ARGB);
        DrawTools.drawImageTransformed(fond.getGraphics(),source,largeur/2,hauteur/2,0,100);
        DrawTools.drawImageTransformed(image_zoomee.getGraphics(),fond,largeur/2,hauteur/2,0,zoom);
        return image_zoomee;
    }

}