package com.pourymovie.persistence;

import com.pourymovie.enums.PersonRole;
import com.pourymovie.enums.VideoQuality;

public class PersonRoleConverter extends EnumConverter<PersonRole>{
  public PersonRoleConverter() {
    super(PersonRole.class);
  }
}
