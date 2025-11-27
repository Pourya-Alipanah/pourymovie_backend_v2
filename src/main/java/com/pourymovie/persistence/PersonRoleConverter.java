package com.pourymovie.persistence;

import com.pourymovie.enums.PersonRole;

public class PersonRoleConverter extends EnumConverter<PersonRole>{
  public PersonRoleConverter() {
    super(PersonRole.class);
  }
}
