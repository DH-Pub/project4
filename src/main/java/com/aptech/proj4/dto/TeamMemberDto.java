package com.aptech.proj4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDto {
    private long id;
    private String user;
    private String addedBy;
    private String team;
    private String role;
    private String addedAt;
}
