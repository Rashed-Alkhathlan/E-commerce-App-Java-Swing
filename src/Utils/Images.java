package Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Images {
    private Images() {}

    public static ImageIcon getImage(String imgName, int width, int height) {
        ImageIcon imgIcon = new ImageIcon("src/Resources/" + imgName + ".png");
        Image scaledImg = imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public static ImageIcon getJPGImage(String imgName, int width, int height) {
        ImageIcon imgIcon = new ImageIcon("src/Resources/" + imgName + ".jpg");
        Image scaledImg = imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public static ImageIcon getJPGImage(String imgName) {
        return new ImageIcon("src/Resources/" + imgName + ".jpg");
    }

    public static ImageIcon getImage(String imgName) {
        return new ImageIcon("src/Resources/" + imgName + ".png");
    }

    public static ImageIcon scaleImage(ImageIcon imgIcon, int width, int height) {
        Image scaledImg = imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public static ImageIcon byteArrayToImageIcon(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return new ImageIcon(ImageIO.read(bais));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] imageIconToByteArray(ImageIcon icon) {
        byte[] imageBytes;
        Image image = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            baos.flush();
            imageBytes = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageBytes;
    }
}
