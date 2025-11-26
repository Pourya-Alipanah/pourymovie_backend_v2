package com.pourymovie.provider;

import com.pourymovie.entity.UploadPartEntity;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Component
public class ChunkUploadProvider {
  @Autowired private S3Client s3Client;

  public String initiateUpload(String bucket, String objectName) {
    CreateMultipartUploadRequest request =
        CreateMultipartUploadRequest.builder().bucket(bucket).key(objectName).build();
    CreateMultipartUploadResponse response = s3Client.createMultipartUpload(request);

    return response.uploadId();
  }

  public String uploadPart(
      String uploadId, String fileName, int partNumber, String bucket, MultipartFile file)
      throws IOException {

    UploadPartRequest uploadPartRequest =
        UploadPartRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .uploadId(uploadId)
            .partNumber(partNumber)
            .contentLength(file.getSize())
            .build();

    try (InputStream chunkStream = file.getInputStream()) {
      UploadPartResponse response =
          s3Client.uploadPart(
              uploadPartRequest, RequestBody.fromInputStream(chunkStream, file.getSize()));
      return response.eTag();
    }
  }

  public void completeUpload(
      String uploadId, List<UploadPartEntity> parts, String fileName, String bucket) {
    List<CompletedPart> completedParts =
        parts.stream()
            .map(
                p ->
                    CompletedPart.builder().partNumber(p.getPartNumber()).eTag(p.getETag()).build())
            .collect(Collectors.toList());

    CompletedMultipartUpload completedUpload =
        CompletedMultipartUpload.builder().parts(completedParts).build();

    CompleteMultipartUploadRequest completeRequest =
        CompleteMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .uploadId(uploadId)
            .multipartUpload(completedUpload)
            .build();
    s3Client.completeMultipartUpload(completeRequest);
  }

  public void abortUpload(String uploadId, String bucket, String fileName) {
    AbortMultipartUploadRequest abortRequest =
        AbortMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .uploadId(uploadId)
            .build();
    s3Client.abortMultipartUpload(abortRequest);
  }
}
