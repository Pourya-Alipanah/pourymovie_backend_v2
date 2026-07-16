package com.pourymovie.util;

import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

public final class RequestMetadataUtils {

  private static final Parser UA_PARSER = new Parser();

  public static String getIpAddress(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  public static DeviceInfo getDeviceInfo(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    if (userAgent == null || userAgent.isEmpty()) {
      return new DeviceInfo("Unknown Device", "Unknown OS");
    }

    Client client = UA_PARSER.parse(userAgent);
    String device =
        client.device.family.equals("Other") ? client.userAgent.family : client.device.family;
    String os = client.os.family;

    return new DeviceInfo(device, os);
  }

  public record DeviceInfo(String device, String os) {}
}
