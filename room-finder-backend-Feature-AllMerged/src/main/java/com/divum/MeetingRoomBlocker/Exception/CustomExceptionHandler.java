package com.divum.MeetingRoomBlocker.Exception;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = InvalidEmailException.class)
    public ResponseEntity<?> handleInvalidEmailException(InvalidEmailException invalidEmailException){
        return new ResponseEntity<>(ErrorWriter(invalidEmailException.getMessage(),HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException invalidTokenException){
        return new ResponseEntity<>(ErrorWriter(invalidTokenException.getMessage(),HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = DataDecryptionException.class)
    public ResponseEntity<?> handleDataDecryptionException(DataDecryptionException dataDecryptionException){
        return new ResponseEntity<>(ErrorWriter(dataDecryptionException.getMessage(),HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<?> handleInvalidDataException(InvalidDataException invalidDataException){
        return new ResponseEntity<>(ErrorWriter(invalidDataException.getMessage(),HttpStatus.FORBIDDEN),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<?>  HandleDataNotFoundException(DataNotFoundException dataNotFoundException)
    {
        return new ResponseEntity<>(ErrorWriter(dataNotFoundException.getMessage(), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = LossOfDataException.class)
    public ResponseEntity<?>  HandleLossOfDataException(LossOfDataException lossOfDataException)
    {
        return new ResponseEntity<>(ErrorWriter(lossOfDataException.getMessage(), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CloudStorageException.class)
    public ResponseEntity<?> handleCloudStorageException(CloudStorageException cloudStorageException){
        return new ResponseEntity<>(ErrorWriter(cloudStorageException.getMessage(), HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicateItemError.class)
    public ResponseEntity<?> handleDuplicateItemError(DuplicateItemError duplicateItemError){
        return new ResponseEntity<>(ErrorWriter(duplicateItemError.getMessage(),HttpStatus.CONFLICT),HttpStatus.CONFLICT);
    }


    private ResponseDTO ErrorWriter(String message,HttpStatus httpStatus){
        return ResponseDTO.builder()
                .data(null)
                .message(message)
                .httpStatus(httpStatus.getReasonPhrase())
                .build();

    }

}
