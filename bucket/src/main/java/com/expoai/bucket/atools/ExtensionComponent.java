package com.expoai.bucket.atools;

import org.springframework.stereotype.Component;

@Component
public class ExtensionComponent {

    public String changeExtension(String filename, String newExtension) {
        return changeExtension(filename, null, newExtension);
    }

    public String changeExtension(String filename, String addSuffixe, String newExtension) {
        if (filename == null || newExtension == null) {
            throw new IllegalArgumentException("Filename and extension must not be null");
        }

        if(addSuffixe == null)
            addSuffixe = "";

        // Ensure the new extension starts with a dot
        if (!newExtension.startsWith(".")) {
            newExtension = "." + newExtension;
        }

        int dotIndex = filename.lastIndexOf('.');
        int sepIndex = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));

        // Dot must be after the last file separator to be considered an extension
        if (dotIndex > sepIndex) {
            return filename.substring(0, dotIndex) + addSuffixe + newExtension;
        } else {
            return filename + addSuffixe + newExtension;
        }
    }

}
