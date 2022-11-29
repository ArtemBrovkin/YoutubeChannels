package app.youtube.channel.config;

import app.youtube.channel.mapper.ChannelMapper;
import app.youtube.channel.repository.ChannelRepository;
import app.youtube.channel.service.ChannelService;
import app.youtube.channel.service.ChannelServiceImpl;
import app.youtube.channel.video.mapper.VideoMapper;
import app.youtube.channel.video.repository.VideoRepository;
import app.youtube.channel.video.service.VideoService;
import app.youtube.channel.video.service.VideoServiceImpl;
import app.youtube.html.service.HtmlService;
import app.youtube.parser.service.ChannelParserService;
import app.youtube.parser.service.ChannelParserServiceImpl;
import app.youtube.parser.service.VideoParserServiceImpl;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import app.youtube.parser.service.VideoParserService;

@Configuration
public class ConfigChannelApp {

  @Bean
  public VideoParserService videoParserService() {
    return new VideoParserServiceImpl();
  }

  @Bean
  public ChannelParserService channelParserService() {
    return new ChannelParserServiceImpl();
  }

  @Bean
  public VideoMapper videoMapper() {
    return new VideoMapper();
  }

  @Bean
  public ChannelMapper channelMapper(VideoMapper videoMapper) {
    return new ChannelMapper(videoMapper);
  }

  @Bean
  public Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Bean
  public ChannelService channelService(
      ChannelRepository channelRepository,
      HtmlService htmlService,
      ChannelParserService parser,
      VideoService videoService,
      ChannelMapper channelMapper,
      Validator validator
  ) {

    return new ChannelServiceImpl(
        channelRepository,
        htmlService,
        parser,
        videoService,
        channelMapper,
        validator
    );
  }

  @Bean
  public VideoService videoService(
      VideoRepository videoRepository,
      HtmlService htmlService,
      VideoParserService videoParserService,
      VideoMapper videoMapper
  ) {
    return new VideoServiceImpl(videoRepository, htmlService, videoParserService, videoMapper);
  }

}
