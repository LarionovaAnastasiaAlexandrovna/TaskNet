package edu.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

public class ImageUtils {

    public static byte[] getDefaultProfilePhoto() {
        try {
            return Files.readAllBytes(new ClassPathResource("static/default.png").getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить фото по умолчанию", e);
        }
    }
}
