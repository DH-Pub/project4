package com.aptech.proj4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneDto {
    private String id;
    private String name;
    private String description;
    private String from;
    private String to;
    private String projects_id; 
}
