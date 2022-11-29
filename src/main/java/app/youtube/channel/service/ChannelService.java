package app.youtube.channel.service;

import java.util.List;
import java.util.Set;
import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.video.domain.dto.VideoDto;

public interface ChannelService {

  ChannelEntity createChannelWithVideosInRangeDays(String channelUrlName, int range);

  ChannelEntity updateChannelInRangeDays(Long id, int range);

  List<ChannelDto> findAll();

  Set<VideoDto> getUnseenVideosFromChannel(Long id);

  void deleteById(Long id);

  List<ChannelDto> findAllChannelsExcludeVideos();

  void updateAll(int days);
}
