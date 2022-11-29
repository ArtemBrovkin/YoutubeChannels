package app.youtube.channel.mapper;

import app.youtube.channel.domain.Channel;
import app.youtube.channel.video.mapper.VideoMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;

@RequiredArgsConstructor
public class ChannelMapper {

  private final VideoMapper videoMapper;

  public ChannelEntity toEntity(Channel channel) {
    return ChannelEntity.builder()
        .id(channel.getId())
        .urlName(channel.getUrlName())
        .title(channel.getTitle())
        .videos(channel.getVideos().stream()
            .map(videoMapper::toEntity)
            .collect(Collectors.toSet()))
        .build();
  }

  public ChannelEntity toEntity(ChannelDto channel) {
    return ChannelEntity.builder()
        .id(channel.getId())
        .urlName(channel.getUrlName())
        .title(channel.getTitle())
        .videos(channel.getVideos().stream()
            .map(videoMapper::toEntity)
            .collect(Collectors.toSet()))
        .build();
  }

  public Channel toPlain(ChannelEntity channel) {
    return Channel.builder()
        .id(channel.getId())
        .urlName(channel.getUrlName())
        .title(channel.getTitle())
        .videos(channel.getVideos().stream()
            .map(videoMapper::toPlain)
            .collect(Collectors.toSet()))
        .build();
  }

  public Channel toPlain(ChannelDto channel) {
    return Channel.builder()
        .id(channel.getId())
        .urlName(channel.getUrlName())
        .title(channel.getTitle())
        .videos(channel.getVideos().stream()
            .map(videoMapper::toPlain)
            .collect(Collectors.toSet()))
        .build();
  }

  public ChannelDto toDto(ChannelEntity channel) {
    return ChannelDto.builder()
        .id(channel.getId())
        .urlName(channel.getUrlName())
        .title(channel.getTitle())
        .videos(channel.getVideos().stream()
            .map(videoMapper::toDto)
            .collect(Collectors.toSet()))
        .build();
  }

  public ChannelDto toDto(Channel channel) {
    return ChannelDto.builder()
        .id(channel.getId())
        .urlName(channel.getUrlName())
        .title(channel.getTitle())
        .videos(channel.getVideos().stream()
            .map(videoMapper::toDto)
            .collect(Collectors.toSet()))
        .build();
  }
}
