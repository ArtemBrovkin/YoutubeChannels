package app.youtube.channel.domain.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import app.youtube.channel.video.domain.dto.VideoDto;

@Builder
@Getter
@Setter
public class ChannelDto {

  private Long id;
  private String urlName;
  private String title;
  private Set<VideoDto> videos;

}
