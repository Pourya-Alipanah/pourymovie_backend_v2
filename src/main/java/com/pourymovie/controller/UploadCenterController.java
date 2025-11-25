package com.pourymovie.controller;

import com.pourymovie.dto.request.UploadBufferDto;
import com.pourymovie.dto.request.UploadStreamDto;
import com.pourymovie.dto.response.UploadResultDto;
import com.pourymovie.service.UploadCenterService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload-center")
@Tag(name = "Upload Center" , description = "Endpoints for managing upload center operations")
public class UploadCenterController {
  @Autowired
  private UploadCenterService uploadCenterService;

  @PostMapping(
          value = "/buffer" ,
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
                  schema = @Schema(implementation = UploadBufferDto.class)
          )
  )
  public UploadResultDto withBuffer(
          @RequestPart("file") MultipartFile file,
          @Valid @ModelAttribute UploadBufferDto dto
  ) throws Exception {
    return uploadCenterService.withBuffer(file , dto.bucket());
  }

  @PostMapping(
          value = "/stream" ,
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
                  schema = @Schema(implementation = UploadStreamDto.class)
          )
  )
  public UploadResultDto uploadStream(
          @RequestPart("file") MultipartFile file,
          @Valid @ModelAttribute UploadStreamDto dto
  ) throws Exception {
    return uploadCenterService.withStream(file, dto.bucket());
  }
}
