package app.youtube.parser.service;

public interface VideoParserService extends HtmlParser{

  String getVideoTitle();
  String getVideoPreviewUrl();
  String getVideoUploadDate();

}
