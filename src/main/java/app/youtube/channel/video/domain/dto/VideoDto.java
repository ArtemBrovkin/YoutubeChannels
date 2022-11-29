package app.youtube.channel.video.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {

  private Long id;
  private String title;
  private String videoUrl;
  private String previewUrl;
  private String uploadDate;
  private boolean isWatched;

}
