package com.otica.oticaapi.service.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Log4j2
@ControllerAdvice //intercepta as exceções
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{ //Esta classe base fornece um método para manipulação de
    //Exceções internas do Spring MVC.
    //Este método retorna um {@code ResponseEntity}

    @Autowired
    private MessageSource messageSource;

    @Override                                                     //Exceção a ser lançada anotado com {@Valid} quando falhar.
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Error.Field> fields = new ArrayList<>();

        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            /* FildError: Encapsula um erro de campo, ou seja, um motivo para rejeitar um determinado
            valor do campo. */

            String name = ((FieldError)error).getField();
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            log.error("Ocorreu o seguinte erro: " + mensagem + ", com o campo: " + name);

            fields.add(new Error.Field(name, mensagem));
        }

        Error error = new Error();
        error.setStatus(status.value());
        error.setDateTime(LocalDateTime.now());
        error.setTitle("Um ou mais campos estão inválidos");
        error.setFields(fields);

        return handleExceptionInternal(ex, error, headers, status, request);

        /* Um único local para customizar o corpo da resposta de todos os tipos de exceção.
        A implementação padrão define o {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
        request atributo e cria um {@link ResponseEntity} a partir do dado corpo, cabeçalhos e status.
        ex: a exceção
        body: o corpo da resposta
        headers: os cabeçalhos da resposta
        status: o status da resposta
        request: a solicitação atual */

    }
    //Anotação para lidar com exceções em classes de manipulador específicas.
    @ExceptionHandler(CustonException.class)
    public ResponseEntity<Object> handleBusiness(CustonException ex, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Error error = new Error();
        error.setStatus(status.value());
        error.setDateTime(LocalDateTime.now());
        error.setTitle(ex.getMessage());

        log.error("Ocorreu o seguinte erro: " + ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }
}
