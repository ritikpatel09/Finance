package Security.Exception;

import Security.Util.BaseApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<BaseApiResponse> handleInvalid(InvalidException ex){
        BaseApiResponse response = BaseApiResponse.ok(ex.getMessage(),0,"400");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseApiResponse> handleNotfound(NotFoundException ex){
        BaseApiResponse response = BaseApiResponse.ok(ex.getMessage(),0,"404");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseApiResponse> handleInvalid(CustomException ex){
        BaseApiResponse response = BaseApiResponse.ok(ex.getMessage(),0,"500");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
