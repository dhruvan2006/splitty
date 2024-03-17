package server.database;

import commons.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface GroupRepository extends JpaRepository<Groups, Long>{
    @Query("SELECT g FROM Groups g WHERE g.groupName LIKE %?1%")
    List<Groups> findByGroupNameContaining(String groupName);

    List<Groups> findByGroupName(String groupName);
}
