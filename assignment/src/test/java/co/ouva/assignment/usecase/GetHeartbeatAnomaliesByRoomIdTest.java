package co.ouva.assignment.usecase;

import co.ouva.assignment.entity.HeartbeatAnomaly;
import co.ouva.assignment.entity.HeartbeatAnomalyDto;
import co.ouva.assignment.repository.HeartBeatAnomalyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GetHeartbeatAnomaliesByRoomIdTest {

    @Test
    void invoke(@Mock HeartBeatAnomalyRepository repository) {
        GetHeartbeatAnomaliesByRoomId getHeartbeatAnomaliesByRoomId = new GetHeartbeatAnomaliesByRoomId(repository);
        HeartbeatAnomaly heartbeatAnomaly = HeartbeatAnomalyDto.builder()
                .roomId("1")
                .time("now")
                .redSignal("102")
                .build()
                .createHeartbeatAnomaly();
        Mockito.when(repository.findByRoomId("1")).thenReturn(
                List.of(heartbeatAnomaly)
        );

        List<HeartbeatAnomalyDto> anomalies = getHeartbeatAnomaliesByRoomId.invoke("1");
        assertEquals(1, anomalies.size());
        assertEquals(heartbeatAnomaly.getRoomId(), anomalies.get(0).getRoomId());
        assertEquals(heartbeatAnomaly.getTime(), anomalies.get(0).getTime());
        assertEquals(heartbeatAnomaly.getRedSignal(), anomalies.get(0).getRedSignal());
    }
}
