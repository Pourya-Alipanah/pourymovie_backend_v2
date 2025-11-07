package com.pourymovie.service;

import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.entity.TitleEntity;
import com.pourymovie.mapper.TitleMapper;
import com.pourymovie.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TitleService {

  @Autowired
  private TitleRepository titleRepository;

  @Autowired
  private TitleMapper titleMapper;

  public TitleDetailsDto findBySlug(String slug) {
    return titleMapper.toDetailsDto(titleRepository.findBySlug(slug).orElseThrow());
  }

  public Page<TitleDto> findAll(Pageable pageable) {
    return titleMapper.toDtoPage(titleRepository.findAll(pageable));
  }

  public void deleteById(Long id) {
    titleRepository.deleteById(id);
  }
}
