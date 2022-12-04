package app.youtube.parser;

import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public enum YoutubeHtmlPattern {
  CHANNEL_TITLE("(?<=Автор: ).+?(?= \\d+)"),
  NEXT_VIDEO_RELATIVE_URL("(?<=\")/watch[^\"]+(?=\")"),
  VIDEO_TITLE("(?<=<title>).+(?= - YouTube)"),
  PREVIEW_URL("(?<=\")https\\://i.ytimg.com/.+?(?=\\\\u)"),
//  UPLOAD_DATE("(?<=\"dateText\":\\{\"simpleText\":\").+?(?=\"}}},)");
UPLOAD_DATE("(?<=\"dateText\":\\{\"simpleText\":\").+?(?= г.)");
  YoutubeHtmlPattern(String regex) {
    this.pattern = Pattern.compile(regex);
  }

  private final Pattern pattern;
}

