package com.divum.MeetingRoomBlocker.Repository;

import com.divum.MeetingRoomBlocker.Entity.UserActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivityEntityRepository extends JpaRepository<UserActivityEntity,Long> {
    Optional<UserActivityEntity> findBySessionIdAndUserEnv(String sessionId, String userEnv);
    Optional<UserActivityEntity> findBySessionIdAndUserEnvAndId(String sessionId, String userEnv, Long id);
}

