package com.pourymovie.dto.request;

import java.util.List;

public record SendEmailDto(String from, List<String> to, String subject, String html) {}
