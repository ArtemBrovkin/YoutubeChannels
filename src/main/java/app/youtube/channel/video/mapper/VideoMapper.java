package app.youtube.channel.video.mapper;

import app.youtube.channel.video.domain.Video;
import app.youtube.channel.video.domain.dto.VideoDto;
import app.youtube.channel.video.domain.entity.VideoEntity;

public class VideoMapper {

  public VideoEntity toEntity(Video video) {
    return VideoEntity.builder()
        .id(video.getId())
        .title(video.getTitle())
        .previewUrl(video.getPreviewUrl())
        .uploadDate(video.getUploadDate())
        .videoUrl(video.getVideoUrl())
        .isWatched(video.isWatched())
        .build();
  }

  public VideoEntity toEntity(VideoDto video) {
    return VideoEntity.builder()
        .id(video.getId())
        .title(video.getTitle())
        .previewUrl(video.getPreviewUrl())
        .uploadDate(video.getUploadDate())
        .videoUrl(video.getVideoUrl())
        .isWatched(video.isWatched())
        .build();
  }

  public VideoDto toDto(Video video) {
    return VideoDto.builder()
        .id(video.getId())
        .title(video.getTitle())
        .previewUrl(video.getPreviewUrl())
        .uploadDate(video.getUploadDate())
        .videoUrl(video.getVideoUrl())
        .isWatched(video.isWatched())
        .build();
  }

  public VideoDto toDto(VideoEntity video) {
    return VideoDto.builder()
        .id(video.getId())
        .title(video.getTitle())
        .previewUrl(video.getPreviewUrl())
        .uploadDate(video.getUploadDate())
        .videoUrl(video.getVideoUrl())
        .isWatched(video.isWatched())
        .build();
  }

  public Video toPlain(VideoEntity video) {
    return Video.builder()
        .id(video.getId())
        .title(video.getTitle())
        .previewUrl(video.getPreviewUrl())
        .uploadDate(video.getUploadDate())
        .videoUrl(video.getVideoUrl())
        .isWatched(video.isWatched())
        .build();
  }

  public Video toPlain(VideoDto video) {
    return Video.builder()
        .id(video.getId())
        .title(video.getTitle())
        .previewUrl(video.getPreviewUrl())
        .uploadDate(video.getUploadDate())
        .videoUrl(video.getVideoUrl())
        .isWatched(video.isWatched())
        .build();
  }
}
