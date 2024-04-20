package com.divum.MeetingRoomBlocker.Repository;

import com.divum.MeetingRoomBlocker.Entity.FeedbackEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Timestamp;
import java.util.Optional;


@Repository
public interface FeedbackEntityRepository extends JpaRepository<FeedbackEntity, Long> {

    @Query("SELECT f FROM FeedbackEntity f WHERE CAST(f.submittedAt AS date) = CAST(:submittedDate AS date)")
    List<FeedbackEntity> findBySubmittedDate(Timestamp submittedDate);
    List<FeedbackEntity> findTop4ByOrderByModifiedAtDesc();

    Optional<FeedbackEntity> findByMeetingEntity(MeetingEntity meeting);

}
