package com.pourymovie.provider;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.enums.PublicBucketNames;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MinioProvider {
  @Autowired
  private MinioClient minioClient;

  @Autowired
  private AppDefaults appDefaults;

  @PostConstruct
  public void onInit() {
    makeAllPublicBucketsPublic();
  }

  public void ensureBucket(String bucket) throws Exception {
    boolean exists = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(bucket).build()
    );
    if (!exists) {
      minioClient.makeBucket(
              MakeBucketArgs.builder()
                      .bucket(bucket)
                      .region("us-east-1")
                      .build()
      );
    }
  }

  public void makeBucketPublic(PublicBucketNames bucket) throws Exception {
    String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": {"AWS": ["*"]},
                      "Action": ["s3:GetObject"],
                      "Resource": ["arn:aws:s3:::%s/*"]
                    }
                  ]
                }
                """.formatted(bucket);

    minioClient.setBucketPolicy(
            SetBucketPolicyArgs.builder()
                    .bucket(bucket.getValue())
                    .config(policy)
                    .build()
    );
  }

  public void makeAllPublicBucketsPublic() {
    for (PublicBucketNames bucket : PublicBucketNames.values()) {
      try {
        ensureBucket(bucket.getValue());
        makeBucketPublic(bucket);
      } catch (Exception e) {
        System.out.println("Cannot make bucket public: " + e.getMessage());
      }
    }
  }

  public String getPublicUrl(String bucket, String objectName) {
    String protocol = appDefaults.isMinioSecure() ? "https" : "http";
    return protocol + "://" + appDefaults.getMinioUrl() + ":" + appDefaults.getMinioPort() + "/" + bucket + "/" + objectName;
  }

  public String generatePresignedUploadUrl(String bucket, String objectName, Optional<Integer> expires) throws Exception {
    ensureBucket(bucket);
    int expiry = 60 * expires.orElse(Integer.parseInt(appDefaults.getMinioExpirationInMinutes()));
    return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .method(Method.PUT)
                    .expiry(expiry)
                    .build()
    );
  }

  public String generatePresignedDownloadUrl(String bucket, String objectName, Integer expiry) throws Exception {
    return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(expiry * 60)
                    .build()
    );
  }

  public String generatePresignedDownloadUrl(String bucket, String objectName) throws Exception {
    int expiry = 60 * Integer.parseInt(appDefaults.getMinioExpirationInMinutes());
    return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(expiry)
                    .build()
    );
  }

  public boolean objectExists(String bucket, String objectName) {
    try {
      minioClient.statObject(
              StatObjectArgs.builder()
                      .bucket(bucket)
                      .object(objectName)
                      .build()
      );
      return true;
    } catch (Exception e) {
      if (e.getMessage().contains("Not found")) return false;
      throw new RuntimeException(e);
    }
  }

  public void uploadBuffer(String bucket, String objectName, byte[] buffer, String mimeType) throws Exception {
    ensureBucket(bucket);

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer)) {
      minioClient.putObject(
              PutObjectArgs.builder()
                      .bucket(bucket)
                      .object(objectName)
                      .stream(inputStream, buffer.length, -1)
                      .contentType(mimeType)
                      .build()
      );
    }
  }

  public void uploadStream(String bucket, String objectName, InputStream stream, long size, String mimeType) throws Exception {
    ensureBucket(bucket);

    minioClient.putObject(
            PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(stream, size, 20 * 1024 * 1024)
                    .contentType(mimeType)
                    .build()
    );
  }

  public void removeObject(String bucket, String objectName) throws Exception {
    minioClient.removeObject(
            RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build()
    );
  }

  public StatObjectResponse statObject(String bucket, String objectName) throws Exception {
    return minioClient.statObject(
            StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build()
    );
  }

  public String generatePresignedChunkUploadUrl(String bucket, String objectName, int partNumber, String uploadId, int expiryMinutes) throws Exception {
    ensureBucket(bucket);

    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("uploadId", uploadId);
    queryParams.put("partNumber", String.valueOf(partNumber));

    return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(expiryMinutes * 60)
                    .extraQueryParams(queryParams)
                    .build()
    );
  }

  public Map<String, String> initiateMultipartUpload(String bucket, String objectName) throws Exception {
    ensureBucket(bucket);

    // برای initiate از یک presigned URL با action خاص استفاده می‌کنیم
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("uploads", "");

    String url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .method(Method.POST)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(60 * 60) // 1 hour
                    .extraQueryParams(queryParams)
                    .build()
    );

    Map<String, String> result = new HashMap<>();
    result.put("uploadUrl", url);
    result.put("bucket", bucket);
    result.put("objectName", objectName);

    return result;
  }

  public String generateCompleteMultipartUrl(String bucket, String objectName, String uploadId, int expiryMinutes) throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("uploadId", uploadId);

    return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .method(Method.POST)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(expiryMinutes * 60)
                    .extraQueryParams(queryParams)
                    .build()
    );
  }

  public String generateAbortMultipartUrl(String bucket, String objectName, String uploadId, int expiryMinutes) throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("uploadId", uploadId);

    return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .method(Method.DELETE)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(expiryMinutes * 60)
                    .extraQueryParams(queryParams)
                    .build()
    );
  }
}
