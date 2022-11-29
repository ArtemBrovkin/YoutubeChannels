package app.youtube.parser.service;

import static app.youtube.parser.YoutubeHtmlPattern.CHANNEL_TITLE;
import static app.youtube.parser.YoutubeHtmlPattern.NEXT_VIDEO_RELATIVE_URL;

public class ChannelParserServiceImpl extends HtmlParserImpl implements ChannelParserService {

  @Override
  public String getChannelName() {
    return getFirstMatch(CHANNEL_TITLE);
  }

  @Override
  public String getNextVideoRelativeUrl() {
      return getFirstMatch(NEXT_VIDEO_RELATIVE_URL);
  }
}
