package com.sproutt.eussyaeussyaapi.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageResizer {
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 300;

    public static InputStream resizeImage(MultipartFile multipartFile, String contentType) throws IOException {
        if (isSizeOk(ImageIO.read(multipartFile.getInputStream()))) {
            return multipartFile.getInputStream();
        }

        BufferedImage originImage = ImageIO.read(multipartFile.getInputStream());
        Image resizedImage = originImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_AREA_AVERAGING);

        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(resizedImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, contentType, byteArrayOutputStream);

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private static boolean isSizeOk(BufferedImage image) {
        return image.getHeight() == IMAGE_HEIGHT && image.getWidth() == IMAGE_WIDTH;
    }
}
