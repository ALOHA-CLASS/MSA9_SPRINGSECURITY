package com.aloha.security_method.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtil {
    
    private static Map<String, MediaType> mediaType;

    static {
        mediaType = new HashMap<>();
        mediaType.put("JPG", MediaType.IMAGE_JPEG);
        mediaType.put("JPEG", MediaType.IMAGE_JPEG);
        mediaType.put("GIF", MediaType.IMAGE_GIF);
        mediaType.put("PNG", MediaType.IMAGE_PNG);
        mediaType.put("WEBP", MediaType.parseMediaType("image/webp") );
    }

    // 확장자를 컨텐츠 타입(MediaType)으로 매핑
    public static MediaType getMediaType(String ext) {
        return mediaType.get(ext.toUpperCase());
    }
}
