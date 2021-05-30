package com.sproutt.eussyaeussyaapi.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageResizer {
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 300;

    public static MultipartFile resizeImage(MultipartFile multipartFile, String contentType) throws IOException {
        if (isSizeOk(ImageIO.read(multipartFile.getInputStream()))) {
            return multipartFile;
        }

        Image originImage = ImageIO.read(multipartFile.getInputStream());
        Image resizedImage = originImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_FAST);

        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(resizedImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, contentType, byteArrayOutputStream);
        byteArrayOutputStream.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        MultipartFile resizedMultipartFile = new MockMultipartFile(multipartFile.getOriginalFilename(), byteArrayInputStream.readAllBytes());

        return resizedMultipartFile;
    }

    private static boolean isSizeOk(BufferedImage image) {
        return image.getHeight() == IMAGE_HEIGHT && image.getWidth() == IMAGE_WIDTH;
    }
}
