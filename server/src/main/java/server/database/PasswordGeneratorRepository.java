package server.database;

import commons.PasswordGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordGeneratorRepository extends JpaRepository<PasswordGenerator, Long> {}
