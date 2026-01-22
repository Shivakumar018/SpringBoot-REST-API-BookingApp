package BookingApp.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import BookingApp.Dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handle(DataNotFoundException exception) {
		return ResponseEntity.status(404).body(new ErrorDto(exception.getMessage()));
	}

	@ExceptionHandler(DataExitsException.class)
	public ResponseEntity<Object> handle(DataExitsException exception) {
		return ResponseEntity.status(404).body(new ErrorDto(exception.getMessage()));
	}
}
