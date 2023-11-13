package co.ouva.assignment.topology;

import co.ouva.assignment.entity.HeartbeatAnomalyDto;
import co.ouva.assignment.repository.HeartBeatAnomalyRepository;
import co.ouva.assignment.topology.dto.HeartbeatMessage;
import co.ouva.assignment.topology.dto.RealTimeRoomState;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class RealTimeRoomAnomalyTopology {
    public static final String HEARTBEAT_MESSAGES = "heartbeat";
    private static final Serde<HeartbeatMessage> heartbeatMessageSerdes = new JsonSerde<>(HeartbeatMessage.class);
    private static final Serde<RealTimeRoomState> roomMovingAverageSerde = new JsonSerde<>(RealTimeRoomState.class);

    @Autowired
    public void process(StreamsBuilder streamsBuilder, HeartBeatAnomalyRepository repository) {
        streamsBuilder.stream(HEARTBEAT_MESSAGES, Consumed.with(Serdes.String(), heartbeatMessageSerdes))
                .groupByKey()
                .aggregate(RealTimeRoomState::new,
                        (roomId, heartbeatMessage, realTimeRoomState) -> {
                            RealTimeRoomState currentRoomState = realTimeRoomState.aggregate(heartbeatMessage);
                            if (!currentRoomState.isDuplicate() && currentRoomState.isAnomaly()) {
                                HeartbeatMessage anomaly = currentRoomState.getLastHeartbeatMessage();
                                HeartbeatAnomalyDto dto = HeartbeatAnomalyDto.builder()
                                        .redSignal(anomaly.getRedSignal())
                                        .time(anomaly.getTime())
                                        .roomId(anomaly.getRoomId())
                                        .build();
                                repository.save(dto.createHeartbeatAnomaly());
                            }
                            return currentRoomState;
                        },
                        Materialized.with(Serdes.String(), roomMovingAverageSerde));
    }
}
