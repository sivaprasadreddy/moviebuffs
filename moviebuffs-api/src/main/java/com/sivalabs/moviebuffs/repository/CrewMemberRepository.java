package com.sivalabs.moviebuffs.repository;

import com.sivalabs.moviebuffs.entity.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
}
