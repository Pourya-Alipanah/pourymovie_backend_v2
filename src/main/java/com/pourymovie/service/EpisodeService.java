package com.pourymovie.service;

import com.pourymovie.dto.request.CreateEpisodeDto;
import com.pourymovie.dto.request.UpdateEpisodeDto;
import com.pourymovie.dto.response.EpisodeDto;
import com.pourymovie.entity.EpisodeEntity;
import com.pourymovie.mapper.EpisodeMapper;
import com.pourymovie.repository.EpisodeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EpisodeService {
  @Autowired
  private EpisodeRepository episodeRepository;

  @Autowired
  private EpisodeMapper episodeMapper;

  @Autowired
  private SeasonService seasonService;

  @Transactional
  public EpisodeDto create(CreateEpisodeDto createEpisodeDto) {
    var episodeEntity = episodeMapper.toEntity(createEpisodeDto);
    var seasonEntity = seasonService.getSeasonById(createEpisodeDto.seasonId());
    episodeEntity.setSeason(seasonEntity);

    return episodeMapper.toDto(episodeRepository.save(episodeEntity));
  }

  public List<EpisodeDto> getAllEpisodesBySeasonId(Long seasonId) {
    var episodeEntities = episodeRepository.findAllBySeasonIdOrderByEpisodeNumberAsc(seasonId);
    return episodeMapper.toDto(episodeEntities);
  }

  public EpisodeEntity getEpisodeById(Long id) {
    return episodeRepository.findById(id).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
    );
  }

  @Transactional
  public EpisodeDto update(Long id , UpdateEpisodeDto updateEpisodeDto) {
    var episodeEntity = getEpisodeById(id);
    episodeMapper.updateEntityFromDto(updateEpisodeDto, episodeEntity);
    var updatedEntity = episodeRepository.save(episodeEntity);
    return episodeMapper.toDto(updatedEntity);
  }

  public void deleteById(Long id) {
    boolean isExist = episodeRepository.existsById(id);
    if (!isExist) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    episodeRepository.deleteById(id);
  }
}
