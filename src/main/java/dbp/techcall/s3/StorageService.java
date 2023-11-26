package dbp.techcall.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.databasemigrationservice.model.S3AccessDeniedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    private File convertMultiPartToFile(MultipartFile file) throws Exception {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new IOException("Error converting multipartFile to file", e);
        }
        return convFile;
    }

    private void validateFile(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }
        if (file.getSize() > 5242880) {
            throw new IllegalArgumentException("File size exceeds 5MB");
        }
    }

    private void validateObjectKey(String objectKey) throws Exception {
        if (objectKey.isEmpty()) {
            throw new IllegalArgumentException("Empty objectKey");
        }
        if (
                objectKey.endsWith("/") ||
                        objectKey.startsWith("/") ||
                        objectKey.startsWith("\\") ||
                        objectKey.endsWith("\\") ||
                        !objectKey.substring(objectKey.lastIndexOf("/") + 1).contains(".")
        ) {
            throw new IllegalArgumentException("Invalid ObjectKey format");
        }
    }

    public String uploadFile(MultipartFile file, String objectKey) throws Exception {
        validateFile(file);
        validateObjectKey(objectKey);

        File fileObj = convertMultiPartToFile(file);
        try {
            deleteFile(objectKey);

            Map<String, String> userMetadata = new HashMap<>();
            userMetadata.put("x-amz-meta-object-key", objectKey);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setUserMetadata(userMetadata);

            s3Client.putObject(new PutObjectRequest(bucketName, objectKey, fileObj).withMetadata(metadata));
            return objectKey;

        } catch (AmazonS3Exception e) {
            throw new S3AccessDeniedException("\nFailed to upload file to S3 with key " + objectKey + " " + e.getErrorCode() + e.getMessage());
        } finally {
            fileObj.delete();
        }
    }

    public String generatePresignedUrl(String objectKey) {

        boolean fileExist = s3Client.doesObjectExist(bucketName, objectKey);
        if (!fileExist) return "";

        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withExpiration(expiration);

            String url = s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();

            System.out.println("\nPresigned url from S3: " + url);
            if (url.isEmpty()) return "";
            return url;
        } catch (SdkClientException e) {
            throw new SdkClientException("\nFailed to generate presigned url from S3" + e.getMessage());
        }
    }

    public void deleteFile(String objectKey) {
        try {
            boolean fileExist = s3Client.doesObjectExist(bucketName, objectKey);
            if (!fileExist) return;

            s3Client.deleteObject(bucketName, objectKey);
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException("\nFailed to delete file from S3" + e.getMessage());
        }
    }


}
