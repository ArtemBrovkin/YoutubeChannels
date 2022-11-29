package app.youtube.exception;

import app.youtube.channel.exception.ChannelExistence;
import app.youtube.channel.exception.ChannelUrlNameValidationException;
import app.youtube.channel.video.exception.NotFoundVideoById;
import app.youtube.html.exception.HtmlConnectionException;
import app.youtube.parser.exception.NoMatchException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class Handler {

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String throwable(Throwable throwable, HttpServletRequest request) {

    log.error("Internal Server Error on request: " + request.getRequestURL() + request.getQueryString());

    return "Internal Server Error";
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String missingServletRequestParameterException(Throwable throwable, HttpServletRequest request) {

    log.error("MissingServletRequestParameterException on request: " + request.getRequestURL() + request.getQueryString());

    return "Request is invalid";
  }

  @ExceptionHandler(NoMatchException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String noMatchException(Throwable throwable) {

    log.error(NestedExceptionUtils.getMostSpecificCause(throwable).getMessage());

    return "Something went wrong. Try again.";

  }

  @ExceptionHandler(HtmlConnectionException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String htmlNotFoundException(Throwable throwable) {

    log.error(NestedExceptionUtils.getMostSpecificCause(throwable).getMessage());

    return "Url is invalid";
  }

  @ExceptionHandler(NotFoundVideoById.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String notFoundVideoById(Throwable throwable) {

    log.error(NestedExceptionUtils.getMostSpecificCause(throwable).getMessage());

    return "Video ID is invalid";
  }

  @ExceptionHandler(ChannelExistence.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String channelExistence(Throwable throwable) {

    log.error(NestedExceptionUtils.getMostSpecificCause(throwable).getMessage());

    return throwable.getMessage();
  }

  @ExceptionHandler(ChannelUrlNameValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String channelUrlNameValidationException(Throwable throwable) {

    log.error(NestedExceptionUtils.getMostSpecificCause(throwable).getMessage());

    return "Your Channel URL Name is invalid!";
  }

  @ExceptionHandler(TransactionSystemException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String transactionSystemException(Throwable throwable, HttpServletRequest request) {

    log.error("Server error in DAO layer by request: " + request.getRequestURL() + request.getQueryString());

    return "Unexpected error";
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String methodArgumentTypeMismatchException(Throwable throwable, HttpServletRequest request) {

    log.error("MethodArgumentTypeMismatchException on request: " + request.getRequestURL() + request.getQueryString());

    return "Request is invalid";
  }

}
