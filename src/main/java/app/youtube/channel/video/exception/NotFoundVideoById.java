package app.youtube.channel.video.exception;

public class NotFoundVideoById extends RuntimeException{

  public NotFoundVideoById(String message) {
    super(message);
  }
}
