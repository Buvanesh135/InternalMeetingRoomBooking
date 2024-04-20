package com.divum.MeetingRoomBlocker.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @JsonIgnore
    private Timestamp createdAt;

    @LastModifiedDate
    @JsonIgnore
    private Timestamp modifiedAt;

    @Column(unique = true)
    private String name;

    @PositiveOrZero
    private Integer minCapacity;

    @PositiveOrZero
    private Integer maxCapacity;

    @ManyToMany
    private List<FacilitiesEntity> facilitiesEntityList;

    private String imageUrl;

    @JsonIgnore
    private boolean isDeleted;

    @JsonIgnore
    private boolean isAvailable;


}
