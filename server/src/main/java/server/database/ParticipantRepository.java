package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{

    @Query("SELECT p FROM Participant p WHERE p.name LIKE %?1%")
    List<Group> findByPartialName(String groupName);

    List<Group> findByName(String groupName);
 }
