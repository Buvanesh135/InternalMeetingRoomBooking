package com.divum.MeetingRoomBlocker.Repository;

import com.divum.MeetingRoomBlocker.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoomEntityRepository extends JpaRepository<RoomEntity,Long> {
    Optional<RoomEntity> findByName(String name);

    RoomEntity findDistinctByName(String name);
}

