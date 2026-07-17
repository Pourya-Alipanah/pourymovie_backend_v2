package com.pourymovie.dto.response;

public record IpInfoResponse(
    String ip,
    String asn,
    String as_name,
    String as_domain,
    String country_code,
    String country,
    String continent_code,
    String continent) {}
