package com.aptech.proj4.exception;

public class MilestoneNotFoundException extends RuntimeException {
    public MilestoneNotFoundException(String projectId) {
        super("Không tìm thấy milestone cho projectId: " + projectId);
    }
}
