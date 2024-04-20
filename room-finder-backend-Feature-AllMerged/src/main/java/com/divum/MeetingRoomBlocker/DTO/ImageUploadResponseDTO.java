package com.divum.MeetingRoomBlocker.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponseDTO {

    private boolean error;
    private String imageUrl;
    private String errorString;

}
