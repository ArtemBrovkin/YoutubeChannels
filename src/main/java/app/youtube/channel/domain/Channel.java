package app.youtube.channel.domain;

import app.youtube.channel.video.domain.Video;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Channel {

  private Long id;
  private String urlName;
  private String title;
  private Set<Video> videos;

}
