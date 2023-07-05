package com.aptech.proj4.service;

import java.util.List;

import org.springframework.core.io.Resource;

import com.aptech.proj4.dto.SubmitDto;
import com.aptech.proj4.model.Submit;

public interface SubmitService {
  SubmitDto uploadSubmit(SubmitDto submitDto, String taskId);

  boolean deleteSubmit(String id);

  Resource loadSubmitFile(String id);

  String getSubmitFileUrl(String id);

  List<Submit> getAllSubmits();

  List<SubmitDto> getAllSubmitByTaskId(String taskId);
}
