package app.youtube.parser.service;

public interface ChannelParserService extends HtmlParser{

  String getChannelName();
  String getNextVideoRelativeUrl();

}
