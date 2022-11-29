package app.youtube.channel.video.service;

import app.youtube.channel.video.domain.Video;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoService {

  Video createVideo(String relativeUrl);
  void setWatched(Long id, boolean isWatched);
  Video getById(Long id);
}
