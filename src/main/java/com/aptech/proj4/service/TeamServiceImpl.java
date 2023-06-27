package com.aptech.proj4.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.proj4.dto.TeamDto;
import com.aptech.proj4.dto.TeamMemberDetailDto;
import com.aptech.proj4.dto.TeamMemberDto;
import com.aptech.proj4.dto.UserTeamDto;
import com.aptech.proj4.enums.TeamMemberRole;
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
        teamRepository.save(team);

        teamDto.setId(team.getId()).setCreatedAt(team.getCreatedAt());
        User user = userRepository.findByEmail(creatingUser)
                .orElseThrow(() -> new NoSuchElementException("User does not exist"));

        // set creator as leader
        TeamMember teamMember = new TeamMember()
                .setTeam(team)
                .setUser(user)
                .setAddedBy(user)
                .setRole(TeamMemberRole.CREATOR);

        memberRepository.save(teamMember);

        return teamDto;
    }

    @Override
    public TeamDto getTeam(String id) {
        Optional<Team> team = Optional.ofNullable(teamRepository.findById(id).get());
        if (team.isPresent()) {
            return modelMapper.map(team.get(), TeamDto.class);
        }
        throw new RuntimeException("Team does not exist");
    }

    @Override
    public List<Team> getAllTeams() {
        List<Team> teams = (List<Team>) teamRepository.findAll();
        return teams;
    }

    @Override
    public TeamDto updateTeam(TeamDto teamDto) {
        Team updatedTeam = modelMapper.map(teamDto, Team.class);
        try {
            teamRepository.save(updatedTeam);
        } catch (Exception e) {
            return null;
        }
        return teamDto;
    }

    @Override
    public boolean deleteTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Team not found"));
        try {
            teamRepository.delete(team);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public TeamMemberDto addMember(TeamMemberDto teamMemberDto, String addingUserEmail) {
        Team team = teamRepository.findById(teamMemberDto.getTeamId())
                .orElseThrow(() -> new NoSuchElementException("Team not found"));
        List<TeamMember> members = memberRepository.findByTeam(team);

        // check if member is already added
        boolean memberInTeam = members.stream().filter(m -> m.getUser().getId().equals(teamMemberDto.getUserId()))
                .findFirst().isPresent();
        if (memberInTeam) {
            throw new RuntimeException("This member is already in the team");
        }

        User addingUser = userRepository.findByEmail(addingUserEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        User user = userRepository.findById(teamMemberDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        try {
            TeamMember teamMember = new TeamMember()
                    .setTeam(team)
                    .setUser(user)
                    .setAddedBy(addingUser)
                    .setRole(TeamMemberRole.valueOf(teamMemberDto.getRole()));
            memberRepository.save(teamMember);
            return teamMemberDto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean removeMember(long id) {
        TeamMember teamMember = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TeamMember not found"));
        if (teamMember.getRole().equals(TeamMemberRole.CREATOR)) {
            throw new RuntimeException("This member cannot be removed");
        }
        memberRepository.delete(teamMember);
        return true;
    }

    @Override
    public TeamMemberDto getMember(String memberUserId, String teamId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMember'");
    }

    @Override
    public List<TeamMemberDetailDto> getAllMembersDetails(String teamId) {
        List<TeamMemberDetailDto> members = memberRepository.allMemberDetails(teamId);
        return members;
    }

    @Override
    public TeamMemberDetailDto getMemberDetailByEmail(String teamId, String email) {
        TeamMemberDetailDto member = memberRepository.getMemberDetailsByEmail(teamId, email)
                .orElseThrow(() -> new NoSuchElementException("This user is not a member of the team"));
        return member;
    }

    @Override
    public List<UserTeamDto> getAllUserTeams(String userId) {
        List<UserTeamDto> teams = memberRepository.getAllUserTeams(userId);
        return teams;
    }

    @Override
    public TeamMemberDto changeMemberRole(TeamMemberDto teamMemberDto) {
        TeamMember teamMember = memberRepository.findById(teamMemberDto.getId())
                .orElseThrow(() -> new NoSuchElementException("TeamMember not found"));
        if (teamMember.getRole().equals(TeamMemberRole.CREATOR)) {
            throw new RuntimeException("This member role cannot be changed");
        }
        teamMember.setRole(TeamMemberRole.valueOf(teamMemberDto.getRole()));
        memberRepository.save(teamMember);
        return teamMemberDto;
    }
}
