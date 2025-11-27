package com.pourymovie.util;

import com.github.slugify.Slugify;

public class MinioUtils {

  public static String buildObjectName(String originalName) {
    String name = originalName.substring(0, originalName.lastIndexOf("."));

    String extension = originalName.substring(originalName.lastIndexOf("."));

    Slugify slg = new Slugify().withLowerCase(true);
    String safeName = slg.slugify(name);

    return System.currentTimeMillis() + "-" + safeName + extension;
  }
}
