package co.ouva.assignment.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeartbeatAnomalyDto {
    private Long id;

    private String roomId;

    private String redSignal;

    private String time;

    public HeartbeatAnomaly createHeartbeatAnomaly() {
        return new HeartbeatAnomaly().convertToEntity(this);
    }

    public static HeartbeatAnomalyDto create(HeartbeatAnomaly anomaly) {
        return HeartbeatAnomalyDto.builder()
                .redSignal(anomaly.getRedSignal())
                .roomId(anomaly.getRoomId())
                .time(anomaly.getTime())
                .build();
    }
}
