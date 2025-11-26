package com.pourymovie.dto.response;

import com.pourymovie.entity.TitlePeopleEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class TitleDetailsDto extends TitleDto {
  private List<CommentDto> comments;
  private List<TitlePeopleEntity> people;
}
