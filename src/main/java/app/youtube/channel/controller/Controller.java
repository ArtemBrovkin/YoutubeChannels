package app.youtube.channel.controller;

import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.service.ChannelService;
import app.youtube.channel.video.domain.dto.VideoDto;
import app.youtube.channel.video.service.VideoService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class Controller {

  private final ChannelService channelService;
  private final VideoService videoService;

  @GetMapping("/hello")
  public String hello() {
    return "Greetings Traveler!";
  }

  @GetMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public ChannelEntity create(
      @RequestParam(name = "urlName") String channelUrlName,
      @RequestParam(name = "range", required = false, defaultValue = "30") int range
  ) {
    return channelService.createChannelWithVideosInRangeDays(channelUrlName, range);
  }

  @GetMapping("/update/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ChannelEntity update(
      @PathVariable Long id,
      @RequestParam(name = "range", required = false, defaultValue = "30") int range
  ) {
    return channelService.updateChannelInRangeDays(id, range);
  }

  @GetMapping("/{channel_id}/videos")
  @ResponseStatus(HttpStatus.OK)
  public Set<VideoDto> getUnseenVideosFromChannel(@PathVariable Long channel_id) {
    return channelService.getUnseenVideosFromChannel(channel_id);
  }

  @GetMapping("/{channel_id}/video/{video_id}")
  @ResponseStatus(HttpStatus.OK)
  public String setWatched(
      @PathVariable Long video_id,
      @RequestParam(name = "isWatched", required = true) boolean isWatched
      ) {
    videoService.setWatched(video_id, isWatched);
    return "Parameter \"isWatched\" in the video by id " + video_id + " was set to " + isWatched;
  }

  @GetMapping("/delete/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public String deleteChannel(@PathVariable Long id) {
    channelService.deleteById(id);
    return "Channel by id " + id + " was deleted successfully";
  }

  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public List<ChannelDto> findAll() {
    return channelService.findAll();
  }

}
