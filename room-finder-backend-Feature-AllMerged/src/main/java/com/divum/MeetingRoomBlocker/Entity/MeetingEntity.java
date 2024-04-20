package com.divum.MeetingRoomBlocker.Entity;

import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import jakarta.persistence.*;
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
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp modifiedAt;

    private String meetingName;

    @ManyToOne
    private MeetingCategoryEntity meetingCategoryEntity;

    private String description;

    @ManyToOne
    private RoomEntity roomEntity;

    private Timestamp startTime;

    private Timestamp endTime;

    @ManyToOne
    private UserEntity host;

    @ManyToMany
    private List<UserEntity> guestList;

    private MeetingStatusEntity status;

    private boolean isDeleted;

    @Column(unique = true)
    private String originalMeetingId;

    private String reason;

}
