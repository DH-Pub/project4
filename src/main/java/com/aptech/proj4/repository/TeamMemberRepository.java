package com.aptech.proj4.repository;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.TeamMember;
import java.util.Optional;


public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {
    Optional<TeamMember> findById(long id);
}
