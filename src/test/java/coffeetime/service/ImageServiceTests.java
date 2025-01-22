package coffeetime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import coffeetime.exception.CoffeeTimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class ImageServiceTests {

    @Autowired
    private ImageService imageService;

    @Test
    public void testUploadImage() {
        // given
        MockMultipartFile testFile = new MockMultipartFile(
            "image",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        // when
        List<String> uploadedKeys = imageService.uploadImages(List.of(testFile));

        // then
        assertThat(uploadedKeys).isNotEmpty();
        assertThat(uploadedKeys.get(0)).isNotNull();
    }

    @Test
    public void testUploadEmptyImage() {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile(
            "image",
            "empty.jpg",
            "image/jpeg",
            new byte[0]
        );

        // then
        assertThrows(CoffeeTimeException.class, () -> {
            imageService.uploadImages(List.of(emptyFile));
        });
    }

    @Test
    public void testGetImageBytes() {
        // given
        String testKey = "test-image-key";

        // when
        byte[] imageBytes = imageService.getImageBytes(testKey);

        // then
        assertThat(imageBytes).isNotNull();
    }
} 