package server.database;

import commons.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface GroupRepository extends JpaRepository<Group, Long>{
    @Query("SELECT g FROM Group g WHERE g.groupName LIKE %?1%")
    List<Group> findByGroupNameContaining(String groupName);

    List<Group> findByGroupName(String groupName);


}
