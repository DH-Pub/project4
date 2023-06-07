package com.aptech.proj4.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.proj4.dto.TeamDto;
import com.aptech.proj4.dto.TeamMemberDto;
import com.aptech.proj4.dto.UserDto;
import com.aptech.proj4.model.TeamMember;
import com.aptech.proj4.model.TeamMemberRole;
import com.aptech.proj4.model.UserRole;
import com.aptech.proj4.service.TeamService;
import com.aptech.proj4.service.UserService;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    TeamService teamService;
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody TeamDto teamDto, Authentication authentication) {
        try {
            return ResponseEntity.ok(teamService.createTeam(teamDto, authentication.getPrincipal().toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeam(@PathVariable String id, Authentication authentication) {
        try {
            // check if user is in the team
            List<UserDto> teamMembers = teamService.getAllMembersDetails(id);
            String user = authentication.getPrincipal().toString();
            boolean inTeam = teamMembers.stream().filter(m -> m.getEmail().equals(user)).findFirst().isPresent();
            if (inTeam) {
                return ResponseEntity.ok(teamService.getTeam(id));
            }

            // else for main and admins
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isMainOrAdmin = authorities.stream()
                    .anyMatch(role -> role.getAuthority().equals(UserRole.MAIN.toString())
                            || role.getAuthority().equals(UserRole.ADMIN.toString()));
            if (isMainOrAdmin) {
                return ResponseEntity.ok(teamService.getTeam(id));
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("You do not have permission to see this.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MAIN', 'ADMIN')")
    public ResponseEntity<?> getAllTeams() {
        try {
            return ResponseEntity.ok(teamService.getAllTeams());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTeam(Authentication authentication, @RequestBody TeamDto teamDto) {
        try {
            // check if user is in the team
            List<TeamMember> members = teamService.getAllMembers(teamDto.getId());
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            Optional<TeamMember> member = members.stream().filter(m -> m.getUser().getId().equals(user.getId()))
                    .findFirst();
            if (member.isPresent() && member.get().getRole().equals(TeamMemberRole.CREATOR)) {
                return ResponseEntity.ok(teamService.updateTeam(teamDto));
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("You do not have permission to see this.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable String id, Authentication authentication) {
        try {
            // check if user is in the team
            List<TeamMember> members = teamService.getAllMembers(id);
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            Optional<TeamMember> member = members.stream().filter(m -> m.getUser().getId().equals(user.getId()))
                    .findFirst();
            if (member.isPresent()) {
                // check if user has CREATOR priviledge
                TeamMemberRole memberRole = member.get().getRole();
                if (memberRole.equals(TeamMemberRole.CREATOR)) {
                    return ResponseEntity.ok(teamService.deleteTeam(id));
                }
            }

            // else for main and admins
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isMainOrAdmin = authorities.stream()
                    .anyMatch(role -> role.getAuthority().equals(UserRole.MAIN.toString())
                            || role.getAuthority().equals(UserRole.ADMIN.toString()));
            if (isMainOrAdmin) {
                return ResponseEntity.ok(teamService.deleteTeam(id));
            }

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("You do not have permission.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/all-members-details")
    public ResponseEntity<?> getAllMembersDetails(Authentication authentication, @PathVariable String id) {
        try {
            // check if user is in the team
            List<UserDto> teamMembers = teamService.getAllMembersDetails(id);
            String user = authentication.getPrincipal().toString();
            Optional<UserDto> member = teamMembers.stream().filter(m -> m.getEmail().equals(user)).findFirst();
            if (member.isPresent()) {
                return ResponseEntity.ok(teamMembers);
            }

            // else for main and admins
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isMainOrAdmin = authorities.stream()
                    .anyMatch(role -> role.getAuthority().equals(UserRole.MAIN.toString())
                            || role.getAuthority().equals(UserRole.ADMIN.toString()));
            if (isMainOrAdmin) {
                return ResponseEntity.ok(teamMembers);
            }

            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("You do not have permission.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/add-member")
    public ResponseEntity<?> addMember(Authentication authentication, @RequestBody TeamMemberDto teamMemberDto) {
        try {
            return ResponseEntity.ok(teamService.addMember(teamMemberDto, authentication.getPrincipal().toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove-member/{id}")
    public ResponseEntity<?> removeMember(Authentication authentication, @RequestBody TeamMemberDto teamMemberDto) {
        try {
            // check if user is in the team
            List<TeamMember> members = teamService.getAllMembers(teamMemberDto.getTeamId());
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            Optional<TeamMember> member = members.stream().filter(m -> m.getUser().getId().equals(user.getId()))
                    .findFirst();
            if (member.isPresent()) {
                TeamMemberRole memberRole = member.get().getRole();
                if (memberRole.equals(TeamMemberRole.CREATOR)
                        || memberRole.equals(TeamMemberRole.ADMINISTRATOR)) {
                    return ResponseEntity.ok(teamService.removeMember(teamMemberDto.getId()));
                }
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("You do not have permission.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-member-role")
    public ResponseEntity<?> changeMemberROle(Authentication authentication, @RequestBody TeamMemberDto teamMemberDto) {
        try {
            // check if user is in the team
            List<TeamMember> members = teamService.getAllMembers(teamMemberDto.getTeamId());
            UserDto user = userService.findUserByEmail(authentication.getPrincipal().toString());
            Optional<TeamMember> member = members.stream().filter(m -> m.getUser().getId().equals(user.getId()))
                    .findFirst();
            if (member.isPresent()) {
                TeamMemberRole memberRole = member.get().getRole();
                if (memberRole.equals(TeamMemberRole.CREATOR)
                        || memberRole.equals(TeamMemberRole.ADMINISTRATOR)) {
                    return ResponseEntity.ok(teamService.changeMemberRole(teamMemberDto));
                }
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("You do not have permission.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
