package com.som.in.service;

import com.som.in.dto.TrackerRequest;
import com.som.in.dto.TrackerResponse;

import java.util.List;

public interface TrackerService {
    List<TrackerResponse> getEmailAnalytics(TrackerRequest request);

    void updateEmailAnalyticsInfo(String id);
}
