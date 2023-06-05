package com.aptech.proj4.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.TeamDto;
import com.aptech.proj4.dto.TeamMemberDto;
import com.aptech.proj4.model.Team;
import com.aptech.proj4.model.TeamMember;
import com.aptech.proj4.model.User;
import com.aptech.proj4.repository.TeamMemberRepository;
import com.aptech.proj4.repository.TeamRepository;
import com.aptech.proj4.repository.UserRepository;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TeamDto createTeam(TeamDto teamDto, String creatingUser) {
        Team team = new Team()
                .setId(Long.toString(System.currentTimeMillis()))
                .setTeamName(teamDto.getTeamName())
                .setDescription(teamDto.getDescription());

        User user = userRepository.findByEmail(creatingUser)
                .orElseThrow(() -> new NoSuchElementException("User does not exist"));

        TeamMember teamMember = new TeamMember();

        return teamDto;
    }

    @Override
    public TeamDto getTeam(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTeam'");
    }

    @Override
    public List<TeamDto> getAllTeams() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTeams'");
    }

    @Override
    public TeamDto updateTeam(TeamDto team) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTeam'");
    }

    @Override
    public boolean deleteTeam(TeamDto team) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTeam'");
    }

    @Override
    public TeamMemberDto addMember(TeamMemberDto teamMemberDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMember'");
    }

    @Override
    public TeamMemberDto removeMember(TeamMemberDto teamMemberDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeMember'");
    }

}
