package app.youtube.parser.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import app.youtube.parser.YoutubeHtmlPattern;
import app.youtube.parser.exception.NoMatchException;

public abstract class HtmlParserImpl implements HtmlParser {

  private final Matcher matcher = Pattern.compile("").matcher("");

  public void setPage(String page) {
    this.matcher.reset(page);
  }

  String getFirstMatch(YoutubeHtmlPattern youtubeHtmlPattern) {

    matcher.usePattern(youtubeHtmlPattern.getPattern());

    if (matcher.find()) {
      return matcher.group();
    }
    throw new NoMatchException("There is no match on pattern " + matcher.pattern());
  }
}
