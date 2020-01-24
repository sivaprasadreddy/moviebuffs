package com.sivalabs.moviebuffs.repository;

import com.sivalabs.moviebuffs.entity.CastMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastMemberRepository extends JpaRepository<CastMember, Long> {
}
