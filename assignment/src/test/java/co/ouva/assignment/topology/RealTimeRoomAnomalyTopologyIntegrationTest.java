package co.ouva.assignment.topology;

import co.ouva.assignment.entity.HeartbeatAnomalyDto;
import co.ouva.assignment.topology.dto.HeartbeatMessage;
import co.ouva.assignment.usecase.GetHeartbeatAnomaliesByRoomId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;

import static co.ouva.assignment.topology.RealTimeRoomAnomalyTopology.HEARTBEAT_MESSAGES;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(topics = {HEARTBEAT_MESSAGES})
@TestPropertySource(properties = {
        "spring.kafka.streams.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.streams.auto-startup=false",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:db;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username=sa",
        "spring.datasource.password=sa",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class RealTimeRoomAnomalyTopologyIntegrationTest {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GetHeartbeatAnomaliesByRoomId getHeartbeatAnomaliesByRoomId;

    @BeforeEach
    public void setUp() {
        streamsBuilderFactoryBean.start();
    }

    @AfterEach
    public void destroy() {
        requireNonNull(streamsBuilderFactoryBean.getKafkaStreams()).close();
        streamsBuilderFactoryBean.getKafkaStreams().cleanUp();
    }

    @Test
    void process() {
        List<HeartbeatMessage> heartbeatMessages = List.of(
                new HeartbeatMessage("1", "12:54", "room1"),
                new HeartbeatMessage("2", "12:55", "room1"),
                new HeartbeatMessage("1", "12:54", "room1"),
                new HeartbeatMessage("1", "12:56", "room1"),
                new HeartbeatMessage("2", "12:57", "room1")
        );
        heartbeatMessages.forEach(
                heartbeatMessage -> {
                    String heartbeatMessageJson;
                    try {
                        heartbeatMessageJson = objectMapper.writeValueAsString(heartbeatMessage);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    kafkaTemplate.send(HEARTBEAT_MESSAGES, heartbeatMessage.getRoomId(), heartbeatMessageJson);
                }
        );

        Awaitility.await().atMost(5, SECONDS)
                .pollDelay(Duration.ofSeconds(1))
                .ignoreExceptions()
                .until(
                        () -> getHeartbeatAnomaliesByRoomId.invoke("room1").size(),
                        equalTo(2)
                );
        List<HeartbeatAnomalyDto> room1Anomalies = getHeartbeatAnomaliesByRoomId.invoke("room1");
        assertEquals(2, room1Anomalies.size());
        assertEquals("12:55", room1Anomalies.get(0).getTime());
        assertEquals("12:57", room1Anomalies.get(1).getTime());
    }
}
