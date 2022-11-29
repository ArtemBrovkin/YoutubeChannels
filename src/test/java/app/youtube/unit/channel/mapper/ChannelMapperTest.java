package app.youtube.unit.channel.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import app.youtube.channel.domain.Channel;
import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.mapper.ChannelMapper;
import app.youtube.channel.video.domain.Video;
import app.youtube.channel.video.domain.dto.VideoDto;
import app.youtube.channel.video.domain.entity.VideoEntity;
import app.youtube.channel.video.mapper.VideoMapper;

@ExtendWith(MockitoExtension.class)
class ChannelMapperTest {

  @Mock
  VideoMapper videoMapper;

  @InjectMocks
  ChannelMapper mapper;

  @Test
  void plainToEntityTest() {
    //arrange
    Video video = Video.builder().build();
    Set<Video> videos = new HashSet<>();
    videos.add(video);

    Channel plain = Channel.builder().videos(videos).build();

    willReturn(VideoEntity.builder().build())
        .given(videoMapper)
        .toEntity(any(Video.class));

    //act
    ChannelEntity channelEntity = mapper.toEntity(plain);

    //assert
    assertThat(channelEntity).isNotNull();
    verify(videoMapper, times(1)).toEntity(any(Video.class));
  }

  @Test
  void dtoToEntityTest() {
    //arrange
    VideoDto video = VideoDto.builder().build();
    Set<VideoDto> videos = new HashSet<>();
    videos.add(video);

    ChannelDto dto = ChannelDto.builder().videos(videos).build();

    willReturn(VideoEntity.builder().build())
        .given(videoMapper)
        .toEntity(any(VideoDto.class));

    //act
    ChannelEntity channelEntity = mapper.toEntity(dto);

    //assert
    assertThat(channelEntity).isNotNull();
    verify(videoMapper, times(1)).toEntity(any(VideoDto.class));
  }

  @Test
  void entityToPlainTest() {
    //arrange
    VideoEntity video = new VideoEntity();
    Set<VideoEntity> videos = new HashSet<>();
    videos.add(video);

    ChannelEntity entity = ChannelEntity.builder().videos(videos).build();

    willReturn(Video.builder().build())
        .given(videoMapper)
        .toPlain(any(VideoEntity.class));

    //act
    Channel channel = mapper.toPlain(entity);

    //assert
    assertThat(channel).isNotNull();
    verify(videoMapper, times(1)).toPlain(any(VideoEntity.class));
  }

  @Test
  void dtoToPlainTest() {
    //arrange
    VideoDto video = new VideoDto();
    Set<VideoDto> videos = new HashSet<>();
    videos.add(video);

    ChannelDto dto = ChannelDto.builder().videos(videos).build();

    willReturn(Video.builder().build())
        .given(videoMapper)
        .toPlain(any(VideoDto.class));

    //act
    Channel channel = mapper.toPlain(dto);

    //assert
    assertThat(channel).isNotNull();
    verify(videoMapper, times(1)).toPlain(any(VideoDto.class));
  }

  @Test
  void entityToDtoTest() {
    //arrange
    VideoEntity video = new VideoEntity();
    Set<VideoEntity> videos = new HashSet<>();
    videos.add(video);

    ChannelEntity entity = ChannelEntity.builder().videos(videos).build();

    willReturn(VideoDto.builder().build())
        .given(videoMapper)
        .toDto(any(VideoEntity.class));

    //act
    ChannelDto channel = mapper.toDto(entity);

    //assert
    assertThat(channel).isNotNull();
    verify(videoMapper, times(1)).toDto(any(VideoEntity.class));
  }

  @Test
  void plainToDtoTest() {
    //arrange
    Video video = new Video();
    Set<Video> videos = new HashSet<>();
    videos.add(video);

    Channel entity = Channel.builder().videos(videos).build();

    willReturn(VideoDto.builder().build())
        .given(videoMapper)
        .toDto(any(Video.class));

    //act
    ChannelDto channel = mapper.toDto(entity);

    //assert
    assertThat(channel).isNotNull();
    verify(videoMapper, times(1)).toDto(any(Video.class));
  }
}