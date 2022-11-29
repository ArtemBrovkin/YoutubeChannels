package app.youtube.unit.channel.video.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;

import app.youtube.channel.video.domain.Video;
import app.youtube.channel.video.domain.entity.VideoEntity;
import app.youtube.channel.video.exception.NotFoundVideoById;
import app.youtube.channel.video.mapper.VideoMapper;
import app.youtube.channel.video.repository.VideoRepository;
import app.youtube.channel.video.service.VideoServiceImpl;
import app.youtube.html.service.HtmlService;
import app.youtube.parser.service.VideoParserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {

  @Mock
  VideoRepository videoRepository;
  @Mock
  HtmlService htmlService;
  @Mock
  VideoParserService parser;
  @Mock
  VideoMapper videoMapper;

  @InjectMocks
  VideoServiceImpl videoService;

  @Test
  void createVideo() {
    //arrange
    Video video = new Video();
    video.setTitle("title");
    video.setPreviewUrl("previewUrl");
    video.setUploadDate("uploadDate");

    willReturn("htmlPage")
        .given(htmlService)
        .getHtml(any(String.class));
    willReturn(video.getTitle())
        .given(parser)
        .getVideoTitle();
    willReturn(video.getPreviewUrl())
        .given(parser)
        .getVideoPreviewUrl();
    willReturn(video.getUploadDate())
        .given(parser)
        .getVideoUploadDate();

    //act
    String url = "url";
    Video createdVideo = videoService.createVideo(url);

    //assert
    assertThat(createdVideo.getTitle()).isEqualTo(video.getTitle());
    assertThat(createdVideo.getPreviewUrl()).isEqualTo(video.getPreviewUrl());
    assertThat(createdVideo.getUploadDate()).isEqualTo(video.getUploadDate());
    assertThat(createdVideo.getVideoUrl()).isEqualTo("https://www.youtube.com/" + url);
  }

  @Test
  void setWatched() {
    //arrange
    VideoEntity entity = VideoEntity.builder().build();
    willReturn(Optional.of(entity))
        .given(videoRepository)
        .findById(anyLong());

    //act
    videoService.setWatched(150L, true);

    //assert
    assertThat(entity.isWatched()).isTrue();
  }

  @Test
  void getById() {
    assertThatExceptionOfType(NotFoundVideoById.class)
        .isThrownBy(() -> videoService.getById(150L));
  }
}