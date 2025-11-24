package com.pourymovie.provider;

import com.pourymovie.entity.ChunkUploadEntity;
import com.pourymovie.entity.UploadCenterEntity;
import com.pourymovie.service.ChunkUploadService;
import com.pourymovie.service.UploadCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class MinioCleanUpProvider {

  @Autowired
  private UploadCenterService uploadCenterService;

  @Autowired
  private ChunkUploadService chunkUploadService;

  @Scheduled(fixedRate = 1000 * 60 * 60 * 12) // every 12 hours
  public void handleCron() {
    // Clean up regular uploads
    List<UploadCenterEntity> pending = uploadCenterService.pendingUploads();
    for (UploadCenterEntity fileObj : pending) {
      try {
        uploadCenterService.removeUpload(fileObj.getFileKey());
        System.out.println("File " + fileObj.getFileKey() + " has been cleaned up.");
      } catch (Exception e) {
        System.err.println("Error removing file " + fileObj.getId());
      }
    }

    // Clean up expired chunk uploads
    List<ChunkUploadEntity> expiredChunks = chunkUploadService.getExpiredUploads();
    for (ChunkUploadEntity upload : expiredChunks) {
      try {
        chunkUploadService.deleteUploadRecord(upload.getUploadId());
        System.out.println("Chunk upload " + upload.getUploadId() + " has been cleaned up.");
      } catch (Exception e) {
        System.err.println("Error cleaning chunk upload " + upload.getId());
      }
    }
  }
}