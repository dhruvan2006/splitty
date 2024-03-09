package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import commons.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{

    @Query("SELECT p FROM Participant p WHERE p.name LIKE %?1%")
    List<Group> findByPartialName(String groupName);

    List<Group> findByName(String groupName);
 }
