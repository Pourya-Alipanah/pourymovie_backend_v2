package com.pourymovie.service;

import com.pourymovie.dto.request.CreateMultipleVideoLinkDto;
import com.pourymovie.dto.request.CreateVideoLinkDto;
import com.pourymovie.dto.request.UpdateVideoLinkDto;
import com.pourymovie.dto.response.VideoLinkDto;
import com.pourymovie.entity.VideoLinkEntity;
import com.pourymovie.enums.UploadFromEntity;
import com.pourymovie.enums.UploadType;
import com.pourymovie.mapper.VideoLinkMapper;
import com.pourymovie.repository.VideoLinkRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VideoLinkService {
  @Autowired
  private VideoLinkRepository videoLinkRepository;

  @Autowired
  private VideoLinkMapper videoLinkMapper;

  @Autowired
  private UploadCenterService uploadCenterService;

  @Autowired
  private TitleService titleService;

  @Autowired
  private EpisodeService episodeService;

  @Transactional
  public VideoLinkDto create(CreateVideoLinkDto createVideoLinkDto) throws Exception {
    var videoLinkEntT = videoLinkMapper.toEntity(createVideoLinkDto);
    var bucketAndKeyPair = uploadCenterService.confirmUpload(
            createVideoLinkDto.url().key(),
            UploadFromEntity.VIDEO,
            UploadType.VIDEO
    );
    videoLinkEntT.setUrl(bucketAndKeyPair);

    if (createVideoLinkDto.episodeId() != null) {
      var episode = episodeService.getEpisodeById(createVideoLinkDto.episodeId());
      videoLinkEntT.setEpisode(episode);
    }

    if (createVideoLinkDto.titleId() != null) {
      var title = titleService.findById(createVideoLinkDto.titleId());
      videoLinkEntT.setTitle(title);
    }
    var savedEntity = videoLinkRepository.save(videoLinkEntT);

    return videoLinkMapper.toDto(savedEntity);
  }

  @Transactional
  public List<VideoLinkDto> createMultiple(CreateMultipleVideoLinkDto createMultipleVideoLinkDto) {
    List<VideoLinkDto> videoLinkDtoList = new ArrayList<>();
    for (CreateVideoLinkDto createVideoLinkDto : createMultipleVideoLinkDto.data()) {
      try {
        VideoLinkDto videoLinkDto = create(createVideoLinkDto);
        videoLinkDtoList.add(videoLinkDto);
      } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
      }
    }
    return videoLinkDtoList;
  }

  public VideoLinkEntity getById(Long id) throws Exception {
    var videoLink = videoLinkRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    var url = uploadCenterService.getDownloadUrlFromBucketAndKeyCombination(
            videoLink.getUrl()
    );
    videoLink.setUrl(url);
    return videoLink;
  }

  public List<VideoLinkDto> getByEpisodeId(Long episodeId) throws Exception {
    var videoLinks = videoLinkRepository.findAllByEpisodeId(episodeId);
    for (var videoLink : videoLinks) {
      var url = uploadCenterService.getDownloadUrlFromBucketAndKeyCombination(
              videoLink.getUrl()
      );
      videoLink.setUrl(url);
    }
    return videoLinkMapper.toDto(videoLinks);
  }

  public List<VideoLinkDto> getByTitleId(Long titleId) throws Exception {
    var videoLinks = videoLinkRepository.findAllByTitleId(titleId);
    for (var videoLink : videoLinks) {
      var url = uploadCenterService.getDownloadUrlFromBucketAndKeyCombination(
              videoLink.getUrl()
      );
      videoLink.setUrl(url);
    }
    return videoLinkMapper.toDto(videoLinks);
  }

  @Transactional
  public VideoLinkDto update(UpdateVideoLinkDto updateVideoLinkDto, Long id) throws Exception {
    var existing = getById(id);

    if (
            (existing.getEpisode() != null && updateVideoLinkDto.titleId() != null) ||
                    (existing.getTitle() != null && updateVideoLinkDto.episodeId() != null)
    ) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    if (updateVideoLinkDto.url() != null) {
      var bucketAndKeyPair = uploadCenterService.confirmUpload(
              updateVideoLinkDto.url().key(),
              UploadFromEntity.VIDEO,
              UploadType.VIDEO
      );
      existing.setUrl(bucketAndKeyPair);
    }
    videoLinkMapper.updateEntityFromDto(updateVideoLinkDto, existing);
    var savedEntity = videoLinkRepository.save(existing);
    var url = uploadCenterService.getDownloadUrlFromBucketAndKeyCombination(
            savedEntity.getUrl()
    );
    savedEntity.setUrl(url);
    return videoLinkMapper.toDto(savedEntity);
  }

  public void deleteById(Long id) {
    var existing = videoLinkRepository.existsById(id);
    if (!existing) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    videoLinkRepository.deleteById(id);
  }
}
