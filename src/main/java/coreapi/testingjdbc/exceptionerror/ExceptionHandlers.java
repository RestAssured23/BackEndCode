package coreapi.testingjdbc.exceptionerror;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandlers {
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public ResponseEntity<ErrorResponse> handleDuplicateKeyViolation(DuplicateKeyViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setEmail("Email is already exist in DB");
  //      errorResponse.setMobile("Mobile is already exist");
        return ResponseEntity.badRequest().body(errorResponse);
    }

}

/*public class ExceptionHandlers {
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyViolation(DuplicateKeyViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        String[] fieldNames = ex.getFieldNames();

        for (String fieldName : fieldNames) {
            if ("email".equals(fieldName)) {
                errorResponse.addErrorMessage("Email is already in use");
            } else if ("mobile".equals(fieldName)) {
                errorResponse.addErrorMessage("Mobile is already in use");
            }
            // Add more conditions for other fields if needed
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }
}*/
