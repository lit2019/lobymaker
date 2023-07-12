package in.shelfpay.lobymaker.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import jakarta.persistence.*;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Version
    @Column(name = "version")
    private int version = 0;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = ZonedDateTime.now();
    }
}