package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.TeamDto;
import com.aptech.proj4.dto.TeamMemberDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.Team;
import com.aptech.proj4.model.TeamMember;

public interface TeamService {
    TeamDto createTeam(TeamDto team, String creatingUser);

    TeamDto getTeam(String id);

    List<Team> getAllTeams();

    TeamDto updateTeam(TeamDto team);

    boolean deleteTeam(String id);

    TeamMemberDto addMember(TeamMemberDto teamMemberDto, String addingUserEmail);

    boolean removeMember(long id);

    List<UserDto> getAllMembersDetails(String teamId);

    TeamMemberDto getMember(String memberUserId, String teamId);

    List<TeamMember> getAllMembers(String teamId);

    TeamMemberDto changeMemberRole(TeamMemberDto teamMemberDto);
}
