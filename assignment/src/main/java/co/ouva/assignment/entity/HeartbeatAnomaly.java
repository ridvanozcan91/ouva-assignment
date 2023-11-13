package co.ouva.assignment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class HeartbeatAnomaly {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String roomId;

    private String redSignal;

    private String time;

    protected HeartbeatAnomaly convertToEntity(HeartbeatAnomalyDto dto) {
        this.id = dto.getId();
        this.redSignal = dto.getRedSignal();
        this.roomId = dto.getRoomId();
        this.time = dto.getTime();
        return this;
    }
}
