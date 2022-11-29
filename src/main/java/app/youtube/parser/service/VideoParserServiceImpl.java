package app.youtube.parser.service;

import static app.youtube.parser.YoutubeHtmlPattern.PREVIEW_URL;
import static app.youtube.parser.YoutubeHtmlPattern.UPLOAD_DATE;
import static app.youtube.parser.YoutubeHtmlPattern.VIDEO_TITLE;

public class VideoParserServiceImpl extends HtmlParserImpl implements VideoParserService{

  @Override
  public String getVideoTitle() {
    return getFirstMatch(VIDEO_TITLE);
  }

  @Override
  public String getVideoPreviewUrl() {
    return getFirstMatch(PREVIEW_URL);
  }

  @Override
  public String getVideoUploadDate() {
    return getFirstMatch(UPLOAD_DATE);
  }
}
