package com.sproutt.eussyaeussyaapi.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageResizerTest {

    @Test
    @DisplayName("300 * 300 크기의 이미지가 아닐 경우 리사이징 처리가 되어 크기가 300 * 300로 바뀐다.")
    void imageResizingTest() throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("default_profile.jpeg");
        Image tmpImage = ImageIO.read(url);

        BufferedImage bufferedImage = new BufferedImage(tmpImage.getWidth(null), tmpImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(tmpImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);

        MultipartFile image = new MockMultipartFile("default_profile", "default_profile.jpeg", "multipart/form-data", byteArrayOutputStream.toByteArray());
        InputStream resizedImageInputStream = ImageResizer.resizeImage(image, "jpeg");
        BufferedImage resizedImage = ImageIO.read(resizedImageInputStream);

        assertThat(ImageIO.read(image.getInputStream()).getHeight()).isNotEqualTo(300);
        assertThat(ImageIO.read(image.getInputStream()).getWidth()).isNotEqualTo(300);
        assertThat(resizedImage.getHeight()).isEqualTo(300);
        assertThat(resizedImage.getWidth()).isEqualTo(300);
    }
}
