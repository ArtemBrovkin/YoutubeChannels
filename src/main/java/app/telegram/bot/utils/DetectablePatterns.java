package app.telegram.bot.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DetectablePatterns {
  ALL("/all"),
  FINDALL("/findall"),
  GETPREVIEW("/p \\d+"),
  GETHELP("/help"),
  SETISWATCHED("/w \\d+ [t|f]"),
  ADDCHANNEL("/add \\S+ \\d+"),
  UPDATEALL("/up all \\d+"),
  UPDATECHANNEL("/up \\S+ \\d+"),
  DELETE("/delete \\d+"),
  SECURE("secure"),
  NOTHING("");

  private final String pattern;

  public static DetectablePatterns detect(String str) {
    for (DetectablePatterns value : DetectablePatterns.values()) {
      if (str.matches(value.pattern)) return value;
    }
    return NOTHING;
  }
}
