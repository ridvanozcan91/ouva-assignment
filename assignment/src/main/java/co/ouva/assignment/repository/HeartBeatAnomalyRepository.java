package co.ouva.assignment.repository;

import co.ouva.assignment.entity.HeartbeatAnomaly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartBeatAnomalyRepository extends JpaRepository<HeartbeatAnomaly, Long> {

    List<HeartbeatAnomaly> findByRoomId(String roomId);
}
