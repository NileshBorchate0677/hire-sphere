package com.hiresphere.hiresphere.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing resume uploads to Cloudinary cloud storage.
 * If credentials are not provided (e.g. during local offline development),
 * isConfigured() will return false, triggering local disk fallback.
 */
@Service
@Slf4j
public class CloudinaryService {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    private Cloudinary cloudinary;
    private boolean configured = false;

    @PostConstruct
    public void init() {
        if (cloudName != null && !cloudName.trim().isBlank()
                && apiKey != null && !apiKey.trim().isBlank()
                && apiSecret != null && !apiSecret.trim().isBlank()) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", cloudName.trim());
            config.put("api_key", apiKey.trim());
            config.put("api_secret", apiSecret.trim());
            config.put("secure", "true");
            this.cloudinary = new Cloudinary(config);
            this.configured = true;
            log.info("[CLOUDINARY] Cloudinary initialized successfully for cloud: {}", cloudName.trim());
        } else {
            log.warn("[CLOUDINARY] Cloudinary credentials not configured. Resume storage will fallback to local disk storage.");
        }
    }

    public boolean isConfigured() {
        return configured;
    }

    /**
     * Uploads a candidate resume PDF to Cloudinary.
     *
     * @param file The uploaded MultipartFile
     * @param publicId Desired unique public_id in Cloudinary
     * @return Secure HTTPS URL of the uploaded resume PDF
     * @throws IOException If upload fails
     */
    public String uploadResume(MultipartFile file, String publicId) throws IOException {
        try {
            return uploadResumeWithTimeout(file, publicId, 8);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Cloudinary upload failed: " + e.getMessage(), e);
        }
    }

    /**
     * Uploads a candidate resume PDF to Cloudinary with a strict timeout.
     * Prevents requests from hanging indefinitely on network or cloud delays.
     *
     * @param file The uploaded MultipartFile
     * @param publicId Desired unique public_id in Cloudinary
     * @param timeoutSeconds Max duration in seconds to wait before timing out
     * @return Secure HTTPS URL of the uploaded resume PDF
     * @throws Exception If upload fails or times out
     */
    public String uploadResumeWithTimeout(MultipartFile file, String publicId, long timeoutSeconds) throws Exception {
        if (!configured || cloudinary == null) {
            throw new IllegalStateException("Cloudinary is not configured");
        }

        byte[] fileBytes = file.getBytes();
        Map<?, ?> uploadParams = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "hiresphere_resumes",
                "resource_type", "auto",
                "overwrite", true
        );

        java.util.concurrent.CompletableFuture<String> future = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try {
                Map<?, ?> result = cloudinary.uploader().upload(fileBytes, uploadParams);
                return (String) result.get("secure_url");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            String secureUrl = future.get(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS);
            log.info("[CLOUDINARY] Resume successfully uploaded within timeout: {}", secureUrl);
            return secureUrl;
        } catch (java.util.concurrent.TimeoutException e) {
            future.cancel(true);
            log.warn("[CLOUDINARY] Upload timed out after {}s for publicId {}", timeoutSeconds, publicId);
            throw new java.util.concurrent.TimeoutException("Cloudinary upload timed out after " + timeoutSeconds + " seconds");
        } catch (java.util.concurrent.ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw new RuntimeException(cause);
        }
    }

    /**
     * Deletes a file from Cloudinary (e.g. when updating resume).
     *
     * @param publicId Public ID to destroy
     */
    public void deleteFile(String publicId) {
        if (!configured || cloudinary == null || publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
        } catch (Exception e) {
            log.warn("[CLOUDINARY] Failed to delete file with publicId {}: {}", publicId, e.getMessage());
        }
    }
}
