package com.aptech.proj4.dto;

import com.aptech.proj4.model.TeamMemberRole;

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
public class UserTeamDto {
    private long teamMemberId;
    private String teamId;
    private String userId;
    private TeamMemberRole teamMemberRole;

    private String teamName;
    private String teamDescription;
    private String teamCreatedAt;

}
