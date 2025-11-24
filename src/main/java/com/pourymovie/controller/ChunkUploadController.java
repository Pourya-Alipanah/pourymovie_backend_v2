package com.pourymovie.controller;

import com.pourymovie.dto.request.InitiateChunkUploadDto;
import com.pourymovie.dto.response.ChunkUploadProgressResponse;
import com.pourymovie.service.ChunkUploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chunk-upload")
@Tag(name = "Chunk Upload", description = "Endpoints for resumable chunk upload")
public class ChunkUploadController {

  @Autowired
  private ChunkUploadService chunkUploadService;

  @PostMapping("/initiate")
  public Map<String, Object> initiate(@Valid @RequestBody InitiateChunkUploadDto dto) throws Exception {
    return chunkUploadService.initiateUpload(dto);
  }

  @GetMapping("/{uploadId}/chunk/{chunkNumber}/url")
  public Map<String, Object> getChunkUploadUrl(
          @PathVariable String uploadId,
          @PathVariable Integer chunkNumber
  ) throws Exception {
    return chunkUploadService.getChunkUploadUrl(uploadId, chunkNumber);
  }

  @PostMapping("/{uploadId}/chunk/{chunkNumber}/confirm")
  public ChunkUploadProgressResponse confirmChunk(
          @PathVariable String uploadId,
          @PathVariable Integer chunkNumber,
          @RequestParam String etag
  ) {
    return chunkUploadService.markChunkUploaded(uploadId, chunkNumber, etag);
  }

  @GetMapping("/{uploadId}/complete-url")
  public Map<String, Object> getCompleteUrl(@PathVariable String uploadId) throws Exception {
    return chunkUploadService.getCompleteUploadUrl(uploadId);
  }

  @PostMapping("/{uploadId}/finalize")
  public String finalize(@PathVariable String uploadId) {
    return chunkUploadService.finalizeUpload(uploadId);
  }

  @GetMapping("/{uploadId}/progress")
  public ChunkUploadProgressResponse getProgress(@PathVariable String uploadId) {
    return chunkUploadService.getProgress(uploadId);
  }

  @GetMapping("/{uploadId}/cancel-url")
  public Map<String, Object> getCancelUrl(@PathVariable String uploadId) throws Exception {
    return chunkUploadService.getCancelUploadUrl(uploadId);
  }

  @DeleteMapping("/{uploadId}")
  public void delete(@PathVariable String uploadId) {
    chunkUploadService.deleteUploadRecord(uploadId);
  }
}