package com.pourymovie.provider;

import com.pourymovie.entity.UploadCenterEntity;
import com.pourymovie.entity.UploadSessionEntity;
import com.pourymovie.service.ChunkUploadService;
import com.pourymovie.service.UploadCenterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        System.out.println("File " + fileObj.getFileKey() + " has been cleaned up By Cron.");
      } catch (Exception e) {
        System.err.println("Cron Error removing file " + fileObj.getId() + ": " + e.getMessage());
      }
    }

    // Clean up expired chunk uploads
    List<UploadSessionEntity> expiredChunks = chunkUploadService.getExpiredUploads();
    for (UploadSessionEntity upload : expiredChunks) {
      try {
        chunkUploadService.abortUpload(upload.getSessionId());
        System.out.println("Chunk upload " + upload.getUploadId() + " has been cleaned up By Cron.");
      } catch (Exception e) {
        System.err.println("Cron Error cleaning chunk upload " + upload.getId() + ": " + e.getMessage());
      }
    }
  }
}