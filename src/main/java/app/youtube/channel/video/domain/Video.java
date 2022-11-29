package app.youtube.channel.video.domain;

import app.youtube.month.MonthsYoutubeRU;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.CacheStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "videoUrl", cacheStrategy = CacheStrategy.LAZY)
public class Video {

  private Long id;
  private String title;
  private String videoUrl;
  private String previewUrl;
  private String uploadDate;
  private boolean isWatched;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  private LocalDate upload;

  public boolean isUploadInRangeDays(int range) {

    if (upload == null) {

      String[] date = this.getUploadDate().split(" ");
      try {
        int dayUpload = Integer.parseInt(date[0]);
        int monthUpload = MonthsYoutubeRU.numberOf(date[1]);
        int yearUpload = Integer.parseInt(date[2]);

        upload = LocalDate.of(yearUpload, monthUpload, dayUpload);
      } catch (Exception e) {
        return true;
      }
    }

    return upload.plusDays(range).isAfter(LocalDate.now());
  }

}
