package com.divum.MeetingRoomBlocker.Implementation.UserImplementation;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Repository.MeetingCategoryEntityRepository;
import com.divum.MeetingRoomBlocker.Service.UserService.MeetingCategoryEntityService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Builder
@RequiredArgsConstructor
public class MeetingCategoryEntityImplementation implements MeetingCategoryEntityService {

    private  final MeetingCategoryEntityRepository meetingCategoryEntityRepository;
    @Override
    public ResponseDTO getCategories() {
        try{
        List<String> categories=meetingCategoryEntityRepository.findAllCategories();
            if(categories.isEmpty()){
                throw new DataNotFoundException("Meetings not found");
            }
        return ResponseDTO.builder()
                .httpStatus(HttpStatus.OK.getReasonPhrase())
                .message("Categories returned successfully")
                .data(categories)
                .build();
        }catch(DataNotFoundException e){
            return  ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting  Categories not found")
                    .build();
        } catch (Exception ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("An error occurred while fetching upcoming meetings")
                    .build();
        }
    }
}
