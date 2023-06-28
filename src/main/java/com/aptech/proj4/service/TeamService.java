package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.TeamDto;
import com.aptech.proj4.dto.TeamMemberDetailDto;
import com.aptech.proj4.dto.TeamMemberDto;
import com.aptech.proj4.dto.UserTeamDto;
import com.aptech.proj4.model.Team;
import com.aptech.proj4.model.TeamMember;

public interface TeamService {
    TeamDto createTeam(TeamDto team, String creatingUser);

    TeamDto getTeam(String id);

    List<Team> getAllTeams();

    List<UserTeamDto> getAllUserTeams(String userId);

    TeamDto updateTeam(TeamDto team);

    boolean deleteTeam(String id);

    TeamMemberDto addMember(TeamMemberDto teamMemberDto, String addingUserEmail);

    boolean removeMember(long id);

    TeamMember getMember(long id);

    /**
     * Return the list of members and their details
     * 
     * @param teamId
     */
    List<TeamMemberDetailDto> getAllMembersDetails(String teamId);

    /**
     * Return the details of the members of the team using the given team id and
     * email of the member
     * 
     * @param teamId
     * @param email
     * 
     */
    TeamMemberDetailDto getMemberDetailByEmail(String teamId, String email);

    TeamMemberDto changeMemberRole(TeamMemberDto teamMemberDto);
}
