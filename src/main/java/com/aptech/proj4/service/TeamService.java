package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.TeamDto;

public interface TeamService {
    TeamDto createTeam(TeamDto team, String creatingUser);
    TeamDto getTeam(String id);
    List<TeamDto> getAllTeams();
    TeamDto updateTeam(TeamDto team);
    boolean deleteTeam(TeamDto team);
}
