package co.ouva.assignment.controller;

import co.ouva.assignment.controller.dto.AnomalyRecord;
import co.ouva.assignment.controller.dto.Room;
import co.ouva.assignment.usecase.GetHeartbeatAnomaliesByRoomId;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public record AnomalyController(
        GetHeartbeatAnomaliesByRoomId getHeartbeatAnomaliesByRoomId
) {

    @QueryMapping(name = "anomaliesByRoomId")
    public List<AnomalyRecord> getAnomalies(@Argument String roomId) {
        return getHeartbeatAnomaliesByRoomId.invoke(roomId).stream().map(
                dto -> new AnomalyRecord(
                        dto.getRedSignal(),
                        dto.getRoomId(),
                        dto.getTime(),
                        new Room(dto.getRoomId(), "")
                )
        ).toList();
    }

    @SchemaMapping(typeName = "AnomalyRecord", field = "room")
    public Room getRoom(AnomalyRecord anomalyRecord) {
        return new Room(anomalyRecord.room().id(), "My room");
    }
}
