package com.pourymovie.provider;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.enums.PublicBucketNames;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;

@Component
public class MinioProvider {
  @Autowired
  private S3Client minioClient;

  @Autowired
  private S3Presigner presigner;

  @Autowired
  private AppDefaults appDefaults;

  @PostConstruct
  public void onInit() {
    makeAllPublicBucketsPublic();
  }

  public void ensureBucket(String bucket) throws Exception {
    try {
      minioClient.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    } catch (NoSuchBucketException e) {
      minioClient.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
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

    minioClient.putBucketPolicy(
            PutBucketPolicyRequest.builder()
                    .bucket(bucket.getValue())
                    .policy(policy)
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

  public String generatePresignedDownloadUrl(String bucket, String objectName, Duration expiry) throws Exception {
    GetObjectRequest getReq = GetObjectRequest.builder()
            .bucket(bucket)
            .key(objectName)
            .build();
    GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
            .getObjectRequest(getReq)
            .signatureDuration(expiry)
            .build();
    PresignedGetObjectRequest presigned = presigner.presignGetObject(presignReq);
    return presigned.url().toExternalForm();
  }

  public String generatePresignedDownloadUrl(String bucket, String objectName) throws Exception {
    Duration expiry = Duration.parse("PT" + appDefaults.getMinioExpirationInMinutes() + "M");

    GetObjectRequest getReq = GetObjectRequest.builder()
            .bucket(bucket)
            .key(objectName)
            .build();
    GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
            .getObjectRequest(getReq)
            .signatureDuration(expiry)
            .build();
    PresignedGetObjectRequest presigned = presigner.presignGetObject(presignReq);
    return presigned.url().toExternalForm();
  }

  public boolean objectExists(String bucket, String objectName) {
    try {
      minioClient.headObject(HeadObjectRequest.builder().bucket(bucket).key(objectName).build());
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    }
  }

  public void uploadBuffer(String bucket, String objectName, byte[] buffer, String mimeType) throws Exception {
    ensureBucket(bucket);

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer)) {
      PutObjectRequest req = PutObjectRequest.builder()
              .bucket(bucket)
              .key(objectName)
              .contentType(mimeType)
              .build();
      minioClient.putObject(req, RequestBody.fromInputStream(inputStream, buffer.length));
    }
  }

  public void uploadStream(String bucket, String objectName, InputStream stream, long size, String mimeType) throws Exception {
    ensureBucket(bucket);

    PutObjectRequest req = PutObjectRequest.builder()
            .bucket(bucket)
            .key(objectName)
            .contentType(mimeType)
            .build();
    minioClient.putObject(req, RequestBody.fromInputStream(stream, size));
  }

  public void removeObject(String bucket, String objectName) throws Exception {
    minioClient.deleteObject(
            DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectName)
                    .build()
    );
  }

  public ObjectMetadata statObject(String bucket, String objectName) throws Exception {
    var response = minioClient.headObject(HeadObjectRequest.builder()
            .bucket(bucket)
            .key(objectName)
            .build()
    );
    return new ObjectMetadata(response.contentLength(), response.contentType());
  }

  public record ObjectMetadata(long size, String contentType) {}
}
