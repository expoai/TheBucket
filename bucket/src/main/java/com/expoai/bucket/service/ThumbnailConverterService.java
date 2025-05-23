package com.expoai.bucket.service;

import com.expoai.bucket.atools.ByteArrayMultipartFile;
import com.expoai.bucket.enums.MediaCategory;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ThumbnailConverterService {

    private static final String targetedFormat = "jpg" ;

    public MultipartFile generateThumbnail(MultipartFile file, String name) throws IOException {
        String contentType = file.getContentType();

        MediaCategory mediaCategory = MediaCategory.fromContentType(contentType)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported file type: " + contentType));

        BufferedImage thumbnail;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes()) ;

        if (mediaCategory == MediaCategory.IMAGE) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream() ;
            Thumbnails.of(inputStream)
                    .size(200, 200) // Change size as needed
                    .outputFormat(targetedFormat)
                    .toOutputStream(outputStream);

            try (ByteArrayInputStream resultInputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                thumbnail = ImageIO.read(resultInputStream);
            }
        }
        else if (mediaCategory == MediaCategory.PDF) {
            PDDocument document = Loader.loadPDF(file.getBytes());
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 100, ImageType.RGB);

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    Thumbnails.of(bim)
                            .size(200, 200)
                            .outputFormat(targetedFormat)
                            .toOutputStream(outputStream);

                    try (ByteArrayInputStream resultInputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                        thumbnail = ImageIO.read(resultInputStream);
                    }
                }
        }
        else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, targetedFormat, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        return ByteArrayMultipartFile
                .builder()
                .name(name)
                .originalFilename(name)
                .contentType("image/" + targetedFormat)
                .content(imageBytes)
                .build() ;
    }


}
