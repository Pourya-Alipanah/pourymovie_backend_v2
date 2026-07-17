package com.pourymovie.util;

import com.pourymovie.dto.response.IpInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ua_parser.Client;
import ua_parser.Parser;

public final class RequestMetadataUtils {

  private static final Parser UA_PARSER = new Parser();
  private static final RestClient REST_CLIENT = RestClient.create();

  public static String getIpAddress(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  public static IpInfoResponse getLocationFromIp(String ipAddress, String token) {
    if ("127.0.0.1".equals(ipAddress)
        || "0:0:0:0:0:0:0:1".equals(ipAddress)
        || "::1".equals(ipAddress)) {
      return new IpInfoResponse(
          ipAddress,
          "local",
          "localhost",
          "UNKNOWN",
          "0,0",
          "Unknown",
          "Unknown/Unknown",
          "Unknown");
    }

    try {
      return REST_CLIENT
          .get()
          .uri("https://ipinfo.io/lite/" + ipAddress)
          .header("Authorization", "Bearer " + token)
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .body(IpInfoResponse.class);
    } catch (Exception e) {
      return new IpInfoResponse(
          ipAddress,
          "Unknown",
          "Unknown",
          "UNKNOWN",
          "Unknown",
          "Unknown",
          "Unknown/Unknown",
          "Unknown");
    }
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
