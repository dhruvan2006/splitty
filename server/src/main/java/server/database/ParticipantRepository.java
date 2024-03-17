package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import commons.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{

    @Query("SELECT p FROM Participant p WHERE p.userName LIKE %?1%")
    List<Participant> findByPartialName(String substring);

    List<Participant> findByUserName(String name);
 }
