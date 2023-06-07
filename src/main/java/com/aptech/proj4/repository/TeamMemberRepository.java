package com.aptech.proj4.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.aptech.proj4.dto.TeamMemberDetailDto;
import com.aptech.proj4.model.Team;
import com.aptech.proj4.model.TeamMember;
import java.util.Optional;
import java.util.List;

public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {
    Optional<TeamMember> findById(long id);

    List<TeamMember> findByTeam(Team team);

    @Query("SELECT new com.aptech.proj4.dto."
            + "TeamMemberDetailDto(tm.id as teamMemberId, tm.team.id as teamId, tm.addedBy.id as addedBy, tm.role as teamMemberRole, tm.addedAt as addedAt, u.id as userId, u.email as email, u.username as username, u.pic as pic) "
            + "FROM TeamMember tm left join User u "
            + "ON tm.user.id = u.id "
            + "WHERE tm.team.id = :teamId")
    List<TeamMemberDetailDto> allMemberDetails(@Param("teamId") String teamId);

    
    @Query("SELECT new com.aptech.proj4.dto."
            + "TeamMemberDetailDto(tm.id as teamMemberId, tm.team.id as teamId, tm.addedBy.id as addedBy, tm.role as teamMemberRole, tm.addedAt as addedAt, u.id as userId, u.email as email, u.username as username, u.pic as pic) "
            + "FROM TeamMember tm left join User u "
            + "ON tm.user.id = u.id "
            + "WHERE tm.team.id = :teamId AND u.email = :email")
    Optional<TeamMemberDetailDto> getMemberDetailsByEmail(@Param("teamId") String teamId, @Param("email") String email);
}
