package app.telegram.bot.config;

import app.telegram.bot.Bot;
import app.youtube.channel.service.ChannelService;
import app.youtube.channel.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config.properties")
public class ConfigTelegramBot {

  @Autowired
  Environment env;

  @Bean
  public Bot bot(ChannelService channelService, VideoService videoService) {
    return new Bot(
        env.getProperty("token"),
        env.getProperty("username"),
        env.getProperty("mychatid"),
        channelService,
        videoService
        );
  }

}
