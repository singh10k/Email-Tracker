package com.som.in.dto;

import java.time.LocalDate;

public record TrackerResponse(String emailId, LocalDate sentOn, boolean isSent, boolean isRead) {
}
