package com.aptech.proj4.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class SubmitDto {
  private String id;
  private String taskId;
  private String note;
  private String attached;
  private String submittedAt;
}
