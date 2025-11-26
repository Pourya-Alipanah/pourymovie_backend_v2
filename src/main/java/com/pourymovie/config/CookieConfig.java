package com.pourymovie.config;

import com.pourymovie.util.CookieUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Component
public class CookieConfig {

  public CookieConfig(Environment environment) {
    // treat "prod" as production profile; adjust name if different
    boolean isProd = environment.acceptsProfiles(Profiles.of("prod"));
    CookieUtils.setSecureCookies(isProd);
  }
}
