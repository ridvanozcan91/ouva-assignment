package co.ouva.assignment.usecase;

import co.ouva.assignment.entity.HeartbeatAnomaly;
import co.ouva.assignment.entity.HeartbeatAnomalyDto;
import co.ouva.assignment.repository.HeartBeatAnomalyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record GetHeartbeatAnomaliesByRoomId(
        HeartBeatAnomalyRepository repository
) {

    public List<HeartbeatAnomalyDto> invoke(String roomId) {
        List<HeartbeatAnomaly> anomalies = repository.findByRoomId(roomId);
        return anomalies.stream().map(HeartbeatAnomalyDto::create).toList();
    }
}
