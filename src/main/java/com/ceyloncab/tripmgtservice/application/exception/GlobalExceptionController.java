package com.ceyloncab.tripmgtservice.application.exception;

import com.ceyloncab.tripmgtservice.application.controller.BaseController;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.tripmgtservice.domain.exception.DomainException;
import com.ceyloncab.tripmgtservice.domain.exception.IgnorableException;
import com.ceyloncab.tripmgtservice.domain.utils.Constants;
import com.ceyloncab.tripmgtservice.external.exception.ExternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController extends BaseController {
    @ExceptionHandler(value = {DomainException.class,ExternalException.class})
    public ResponseEntity<Object> genericException(RuntimeException exception) {
        ResponseHeader response = createResponse(exception);
        return getResponseEntity(response.getResponseCode(),response);
    }

    @ExceptionHandler(value = {IgnorableException.class})
    public void ignorableException(IgnorableException exception) {
        log.error(exception.getMessage());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> unhandledException(RuntimeException exception) {
        ResponseHeader response = new ResponseHeader();
        response.setCode(Constants.UNHANDLED_ERROR_CODE);
        response.setMessage(exception.getMessage());
        response.setResponseCode("500");
        return getResponseEntity(response.getResponseCode(),response);
    }
    private ResponseHeader createResponse(RuntimeException exception){
        ResponseHeader response = new ResponseHeader(Constants.ResponseData.INTERNAL_SERVER_ERROR);
        if(exception instanceof DomainException){
            DomainException de = (DomainException) exception;
            response.setCode(de.getCode());
            response.setMessage(de.getMessage());
            response.setResponseCode(de.getResponseCode());
        } else if (exception instanceof ExternalException) {
            ExternalException ext = (ExternalException) exception;
            response.setCode(ext.getCode());
            response.setMessage(ext.getMessage());
            response.setResponseCode(ext.getResponseCode());
        }
        return response;
    }
}
