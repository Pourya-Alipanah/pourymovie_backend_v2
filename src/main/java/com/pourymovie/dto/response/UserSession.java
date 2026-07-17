package com.pourymovie.dto.response;

public record UserSession(
    String jti,
    String ipAddress,
    String deviceName,
    String os,
    IpInfoResponse locationInfo,
    long loginTime) {}
