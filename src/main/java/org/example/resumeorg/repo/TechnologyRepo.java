package org.example.resumeorg.repo;

import org.example.resumeorg.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepo extends JpaRepository<Technology,Integer> {
}
