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
public class DocumentDto {
    private String id;
    private String description;
    private String files; // string for file name
    private String project_id;
    private String createAt;
}
