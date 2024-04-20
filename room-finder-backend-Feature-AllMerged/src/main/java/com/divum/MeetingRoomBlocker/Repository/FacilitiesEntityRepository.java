package com.divum.MeetingRoomBlocker.Repository;

import com.divum.MeetingRoomBlocker.DTO.FacilityDTO;
import com.divum.MeetingRoomBlocker.Entity.FacilitiesEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FacilitiesEntityRepository extends JpaRepository<FacilitiesEntity,Long> {

    @Query(value = "SELECT facility_name as facilityName , icon_url as iconUrl from  facilities_entity ",nativeQuery = true)
    List<FacilityDTO> findAllFacilities();
    Optional<FacilitiesEntity> findByFacilityName(String facilityName);

}
