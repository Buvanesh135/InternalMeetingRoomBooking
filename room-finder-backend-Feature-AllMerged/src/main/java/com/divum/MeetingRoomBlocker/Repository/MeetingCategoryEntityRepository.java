package com.divum.MeetingRoomBlocker.Repository;

import com.divum.MeetingRoomBlocker.Entity.MeetingCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MeetingCategoryEntityRepository extends JpaRepository<MeetingCategoryEntity,Long> {
    @Query(value = "SELECT * FROM meeting_category_entity WHERE meeting_category_entity.category_name = :categoryName", nativeQuery = true)
    MeetingCategoryEntity findMeetingCategoryEntities(@Param("categoryName") String categoryName);
    @Query(value = "SELECT  category_name FROM meeting_category_entity ", nativeQuery = true)
    List<String> findAllCategories();
}



