package com.divum.MeetingRoomBlocker.DTO;

import com.divum.MeetingRoomBlocker.Entity.FacilitiesEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomEntityDTO {

    private String name;
    private Integer maxCapacity;
    private Integer minCapacity;
    private List<String> facilitiesEntityList;
    private MultipartFile roomImage;

}
