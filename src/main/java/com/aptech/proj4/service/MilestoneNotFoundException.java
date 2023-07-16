package com.aptech.proj4.service;

public class MilestoneNotFoundException extends RuntimeException {
    public MilestoneNotFoundException(String projectId) {
        super("Không tìm thấy milestone cho projectId: " + projectId);
    }
}
