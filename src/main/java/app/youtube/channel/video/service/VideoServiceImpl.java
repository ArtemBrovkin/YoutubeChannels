package app.youtube.channel.video.service;

import app.youtube.channel.video.domain.Video;
import app.youtube.channel.video.domain.entity.VideoEntity;
import app.youtube.channel.video.exception.NotFoundVideoById;
import app.youtube.channel.video.mapper.VideoMapper;
import app.youtube.channel.video.repository.VideoRepository;
import app.youtube.html.service.HtmlService;
import app.youtube.parser.service.VideoParserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

  private final VideoRepository videoRepository;
  private final HtmlService htmlService;
  private final VideoParserService parser;
  private final VideoMapper videoMapper;

  @Override
  public Video createVideo(String relativeUrl) {
    Video video = new Video();
    this.fillProperties(video, relativeUrl);
    return video;
  }

  @Override
  public void setWatched(Long id, boolean isWatched) {
    VideoEntity videoEntity = getEntityById(id);
    videoEntity.setWatched(isWatched);
    videoRepository.save(videoEntity);
  }

  private VideoEntity getEntityById(Long id) {
    return videoRepository.findById(id)
        .orElseThrow(() -> new NotFoundVideoById("There is no video by id " + id));
  }

  private void fillProperties(Video video, String relativeUrl) {

    String videoUrl = "https://www.youtube.com/" + relativeUrl;
    String htmlPage = htmlService.getHtml(videoUrl);

    parser.setPage(htmlPage);

    video.setTitle(parser.getVideoTitle());
    video.setPreviewUrl(parser.getVideoPreviewUrl());
    video.setUploadDate(parser.getVideoUploadDate());
    video.setVideoUrl(videoUrl);

  }

  @Override
  public Video getById(Long id) {
    return videoMapper.toPlain(getEntityById(id));
  }

}
