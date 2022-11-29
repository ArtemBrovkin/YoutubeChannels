package app.youtube.channel.exception;

public class ChannelExistence extends RuntimeException{

  public ChannelExistence(String message) {
    super(message);
  }
}
