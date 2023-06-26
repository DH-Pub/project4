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
public class CommentDto {
  private String id;
  private String taskId;
  private String comment;
  private String createdBy;
  private String createAt;
}
