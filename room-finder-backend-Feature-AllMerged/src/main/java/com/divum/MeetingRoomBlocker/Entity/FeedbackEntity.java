package com.divum.MeetingRoomBlocker.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.sql.Timestamp;
@Entity  //projects ,concepts+puzzles ,advanced level programming,level 2 programming
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Timestamp createdAt;
    @LastModifiedDate
    private Timestamp modifiedAt;
    @ManyToOne
    private UserEntity userEntity;
    @OneToOne
    private MeetingEntity meetingEntity;
    private String feedback;
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;
    private boolean isDeleted;
    private boolean isSeen;
    private String randomUrl;
    private Timestamp submittedAt;
}
