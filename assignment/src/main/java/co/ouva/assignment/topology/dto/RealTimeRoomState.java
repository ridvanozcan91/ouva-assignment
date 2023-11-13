package co.ouva.assignment.topology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeRoomState {
    private String roomId;
    private Double heartbeatSum = 0.0;
    private Integer heartBeatCount = 0;
    private Double heartBeatAverage = 0.0;
    private HeartbeatMessage lastHeartbeatMessage;
    private boolean isAnomaly = false;
    private boolean isDuplicate = false;

    public RealTimeRoomState aggregate(HeartbeatMessage heartbeatMessage) {
        this.isDuplicate = isDuplicateRecord(heartbeatMessage);
        if (!this.isDuplicate) {
            this.heartBeatAverage = this.heartbeatSum / this.heartBeatCount;
            this.isAnomaly = Double.parseDouble(heartbeatMessage.getRedSignal()) > this.heartBeatAverage;
            this.roomId = heartbeatMessage.getRoomId();
            this.lastHeartbeatMessage = heartbeatMessage;
            this.heartbeatSum += Double.parseDouble(heartbeatMessage.getRedSignal());
            this.heartBeatCount += 1;
        }
        return this;
    }

    private boolean isDuplicateRecord(HeartbeatMessage heartbeatMessage) {
        return this.lastHeartbeatMessage != null && this.lastHeartbeatMessage.getTime().compareTo(heartbeatMessage.getTime()) >= 0;
    }
}
