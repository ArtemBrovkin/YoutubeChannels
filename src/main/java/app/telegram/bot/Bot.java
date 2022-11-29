package app.telegram.bot;

import static app.telegram.bot.utils.DetectablePatterns.ADDCHANNEL;
import static app.telegram.bot.utils.DetectablePatterns.ALL;
import static app.telegram.bot.utils.DetectablePatterns.DELETE;
import static app.telegram.bot.utils.DetectablePatterns.FINDALL;
import static app.telegram.bot.utils.DetectablePatterns.GETHELP;
import static app.telegram.bot.utils.DetectablePatterns.GETPREVIEW;
import static app.telegram.bot.utils.DetectablePatterns.NOTHING;
import static app.telegram.bot.utils.DetectablePatterns.SECURE;
import static app.telegram.bot.utils.DetectablePatterns.SETISWATCHED;
import static app.telegram.bot.utils.DetectablePatterns.UPDATEALL;
import static app.telegram.bot.utils.DetectablePatterns.UPDATECHANNEL;

import app.telegram.bot.utils.DetectablePatterns;
import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.service.ChannelService;
import app.youtube.channel.video.domain.Video;
import app.youtube.channel.video.domain.dto.VideoDto;
import app.youtube.channel.video.service.VideoService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

  private final String token;
  private final String userName;
  private final String myChatId;
  private String chatId;
  private boolean isSecure = false;
  private final ChannelService channelService;
  private final VideoService videoService;

  private final Map<DetectablePatterns, Consumer<String>> operations
      = new EnumMap<>(DetectablePatterns.class);

  {
    operations.put(ALL, this::getAllUnseenVideos);
    operations.put(NOTHING, this::doNothing);
    operations.put(FINDALL, this::findall);
    operations.put(GETPREVIEW, this::getPreview);
    operations.put(GETHELP, this::getHelp);
    operations.put(SETISWATCHED, this::setIsWatched);
    operations.put(ADDCHANNEL, this::addChannel);
    operations.put(UPDATEALL, this::updateAll);
    operations.put(UPDATECHANNEL, this::updateChannel);
    operations.put(DELETE, this::deleteChannelById);
    operations.put(SECURE, this::secure);
  }

  @Override
  public String getBotUsername() {
    return this.userName;
  }

  @Override
  public String getBotToken() {
    return this.token;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() &&
        update.getMessage().hasText()
    ) {
      String msg = update.getMessage().getText();
      if (!isSecure) {
        this.chatId = Long.toString(update.getMessage().getChatId());
      }

      operations.get(DetectablePatterns.detect(msg))
          .accept(msg);
    }
  }

  private void sendImageByUrl(String imageUrl) {
    InputFile inputFile = new InputFile().setMedia(imageUrl);

    try {
      execute(
          SendPhoto.builder()
              .photo(inputFile)
              .chatId(chatId)
              .build()
      );
    } catch (Exception e) {
      System.out.println("sendImageByUrl EXCEPTION");
    }
  }

  private void sendText(String text) {
    try {
      execute(
          SendMessage.builder()
              .text(text)
              .chatId(chatId)
              .build()
      );
    } catch (Exception e) {
      System.out.println(text);
      System.out.println("sendText EXCEPTION");
    }
  }

  private void getAllUnseenVideos(String str) {

    List<ChannelDto> allChannels = channelService.findAll();

    if (allChannels == null || allChannels.isEmpty()) {
      this.sendText("There is no any channel");
      return;
    }

    StringBuilder text = new StringBuilder();
    for (ChannelDto channel : allChannels) {
      text.append(channel.getTitle()).append(":\n");
      for (VideoDto video : channel.getVideos()) {
        if (!video.isWatched()) {
          text.append("id: ").append(video.getId())
              .append(" title: ").append(video.getTitle())
              .append("\n\n");
        }
      }
      this.sendText(text.toString());
      text.setLength(0);
    }

  }

  private void findall(String str) {
    List<ChannelDto> all = channelService.findAll();
    StringBuilder text = new StringBuilder();
    for (ChannelDto channel : all) {
      text.append(channel.getId())
          .append(" ")
          .append(channel.getTitle())
          .append("\n");
    }
    this.sendText(text.toString());
  }

  private void getPreview(String str) {
    String videoId = str.replaceFirst("/p ", "");
    Video video = videoService.getById(Long.parseLong(videoId));
    this.sendImageByUrl(video.getPreviewUrl());
  }

  private void getHelp(String str) {
    this.sendText(
        """
            /all - all unseen videos\s
            /findall - all channels with ID\s
            /p [video_id] - video preview\s
            /w [video_id] [t/f] - set parameter "iswatched" to [t/f]\s
            /add [url_name_channel] [days] - add channel by url on days\s
            /up all [days] - update all by 7 days ago\s
            /up [channel_id] [days] - update channel on days\s
            /delete [channel_name] - delete channel"""
    );
  }

  private void setIsWatched(String str) {
    String[] split = str.split(" ");
    String videoId = split[1];
    String tOrF = split[2];

    videoService.setWatched(Long.parseLong(videoId), tOrF.equals("t"));
  }

  private void addChannel(String str) {
    String[] split = str.split(" ");
    String urlChannelName = split[1];
    String range = split[2];

    channelService.createChannelWithVideosInRangeDays(urlChannelName, Integer.parseInt(range));
    sendText("added");
  }

  private void updateAll(String str) {
    String[] split = str.split(" ");
    String range = split[2];

    channelService.updateAll(Integer.parseInt(range));
  }

  private void updateChannel(String str) {
    String[] split = str.split(" ");
    String id = split[1];
    String range = split[2];

    channelService.updateChannelInRangeDays(Long.valueOf(id), Integer.parseInt(range));
  }

  private void deleteChannelById(String str) {
    String[] split = str.split(" ");
    String channelId = split[1];

    channelService.deleteById(Long.parseLong(channelId));
  }

  private void secure(String str) {
    isSecure = !isSecure;
    if (isSecure) {
      chatId = myChatId;
    }
    this.sendText("secure is " + isSecure);
  }

  private void doNothing(String str) {
  }

}



