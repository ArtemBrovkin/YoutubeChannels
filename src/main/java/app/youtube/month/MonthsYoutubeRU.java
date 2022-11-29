package app.youtube.month;

public enum MonthsYoutubeRU {

  January("янв."),
  February("февр."),
  March("мар."),
  April("апр."),
  May("мая"),
  June("июн."),
  July("июл."),
  August("авг."),
  September("сент."),
  October("окт."),
  November("нояб."),
  December("дек.");

  MonthsYoutubeRU(String monthLikeInYoutube) {
    this.monthLikeInYoutube = monthLikeInYoutube;
  }

  private final String monthLikeInYoutube;

  public static int numberOf(String monthLikeInYoutube) {
    for (MonthsYoutubeRU value : MonthsYoutubeRU.values()) {
      if (value.monthLikeInYoutube.equals(monthLikeInYoutube)) return value.ordinal() + 1;
    }
    return -1;
  }

}
