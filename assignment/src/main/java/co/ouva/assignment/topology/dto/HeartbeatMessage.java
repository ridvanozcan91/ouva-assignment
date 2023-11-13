package co.ouva.assignment.topology.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeartbeatMessage {
    @JsonProperty("Red_Signal")
    private String redSignal;
    @JsonProperty("Time")
    private String time;
    @JsonProperty("room_id")
    private String roomId;
}
