package com.pourymovie.controller;

import com.pourymovie.dto.request.InitiateChunkUploadDto;
import com.pourymovie.dto.request.UploadChunkDto;
import com.pourymovie.dto.response.ChunkUploadDto;
import com.pourymovie.dto.response.UploadResultDto;
import com.pourymovie.dto.response.UploadedPartInfoDto;
import com.pourymovie.service.ChunkUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload-chunk")
@Tag(name = "Chunk Upload", description = "Endpoints for handling chunked file uploads")
public class ChunkUploadController {

  @Autowired
  private ChunkUploadService chunkUploadService;

  @PostMapping("/initiate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public ChunkUploadDto initiateChunkUpload(@Valid @RequestBody InitiateChunkUploadDto initiateChunkUploadDto) {
    return chunkUploadService.initiateChunkUpload(initiateChunkUploadDto);
  }

  @PostMapping(
          value = "/chunk",
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
                  schema = @Schema(implementation = UploadChunkDto.class)
          )
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  public void chunkUpload(
          @RequestPart("file") MultipartFile file,
          @Valid @ModelAttribute UploadChunkDto dto
  ) throws IOException {
    chunkUploadService.uploadPart(dto, file);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @GetMapping("/parts/{sessionId}")
  public List<UploadedPartInfoDto> getUploadedParts(@PathVariable String sessionId) {
    return chunkUploadService.listUploadedParts(sessionId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Required Role = Admin")
  @PostMapping("/complete/{sessionId}")
  public UploadResultDto completeChunkUpload(@PathVariable String sessionId) throws Exception {
    return chunkUploadService.completeUpload(sessionId);
  }
}
