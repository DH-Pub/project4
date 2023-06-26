package com.aptech.proj4.dto;

import com.aptech.proj4.enums.TeamMemberRole;

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
public class TeamMemberDetailDto {
    private long teamMemberId;
    private String teamId;
    private String addedBy;
    private TeamMemberRole teamMemberRole;
    private String addedAt;

    private String userId;
    private String email;
    private String username;
    private String pic;
}
