package app.youtube.channel.service;

import app.youtube.channel.domain.Channel;
import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.exception.ChannelExistence;
import app.youtube.channel.exception.ChannelUrlNameValidationException;
import app.youtube.channel.mapper.ChannelMapper;
import app.youtube.channel.repository.ChannelRepository;
import app.youtube.channel.video.domain.Video;
import app.youtube.channel.video.domain.dto.VideoDto;
import app.youtube.channel.video.service.VideoService;
import app.youtube.html.exception.HtmlConnectionException;
import app.youtube.html.service.HtmlService;
import app.youtube.parser.exception.NoMatchException;
import app.youtube.parser.service.ChannelParserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

  private final ChannelRepository channelRepository;
  private final HtmlService htmlService;
  private final ChannelParserService parser;
  private final VideoService videoService;
  private final ChannelMapper channelMapper;
  private final Validator validator;
  private final List<UnaryOperator<String>> typesOfUrlVideosPage =
      List.of(
          this::getUrlOfVideosPage1,
          this::getUrlOfVideosPage2,
          this::getUrlOfVideosPage3
      );

  @Override
  public ChannelEntity createChannelWithVideosInRangeDays(String channelUrlName, int range) {

    validateUrlName(channelUrlName);

    if (channelRepository.existsByUrlName(channelUrlName)) {
      throw new ChannelExistence(
          "Channel " + channelRepository.getByUrlName(channelUrlName).getTitle()
              + " already exist");
    }

    setChannelPageToParserByUrlName(channelUrlName);
    String channelName = parser.getChannelName();
    Set<Video> videos = getVideosInRange(range);

    Channel channel = Channel.builder()
        .urlName(channelUrlName)
        .title(channelName)
        .videos(videos)
        .build();

    return channelRepository.save(validateAndGetEntity(channel));
  }

  private Set<Video> getVideosInRange(int range) {

    Set<Video> videos = new HashSet<>();
    Video video;

    try {

      while (
          (video = videoService.createVideo(parser.getNextVideoRelativeUrl()))
              .isUploadInRangeDays(range)
      ) {
        videos.add(video);
      }

    } catch (NoMatchException ignored) {
      //  Videos are over
    }

    return videos;
  }

  private void setChannelPageToParserByUrlName(String channelUrlName) {
    String html = getHtmlPage(channelUrlName);
    parser.setPage(html);
  }

  private String getHtmlPage(String channelUrlName) {
    String urlOfVideosPage;
    String html;

    for (UnaryOperator<String> u : typesOfUrlVideosPage) {
      urlOfVideosPage = u.apply(channelUrlName);
      html = this.getHtml(urlOfVideosPage);

      if (!html.isBlank()) {
        return html;
      }
    }
    throw new ChannelExistence("Channel with such urlName: " + channelUrlName + " don't exist");
  }

  private String getHtml(String url) {
    try {
      return htmlService.getHtml(url);
    } catch (HtmlConnectionException e) {
      return "";
    }
  }

  private ChannelEntity validateAndGetEntity(Channel channel) {
    ChannelEntity entity = channelMapper.toEntity(channel);
    Set<ConstraintViolation<ChannelEntity>> validate = validator.validate(entity);

    if (validate.isEmpty()) return entity;
    else throw new ConstraintViolationException(validate);
  }

  @Override
  public ChannelEntity updateChannelInRangeDays(Long id, int range) {

    ChannelEntity entity = getEntityByIdOrElseThrow(id);

    setChannelPageToParserByUrlName(entity.getUrlName());
    Set<Video> videos = getVideosInRange(range);

    return channelRepository.save(updateVideos(entity, videos));
  }

  private ChannelEntity getEntityByIdOrElseThrow(Long id) {
    Optional<ChannelEntity> byId = channelRepository.findById(id);
    if (byId.isEmpty()) {
      throw new ChannelExistence("Channel with such id: " + id + " don't exist");
    }
    return byId.get();
  }

  private ChannelEntity updateVideos(ChannelEntity entity, Set<Video> videos) {
    Channel channel = channelMapper.toPlain(entity);
    channel.getVideos().addAll(videos);
    return channelMapper.toEntity(channel);
  }

  @Override
  public Set<VideoDto> getUnseenVideosFromChannel(Long id) {
    ChannelEntity channelEntity = channelRepository.findById(id)
        .orElseThrow(() -> new ChannelExistence("There is not channel with id: " + id));

    return channelMapper.toDto(channelEntity)
        .getVideos().stream()
        .filter(video -> !video.isWatched())
        .collect(Collectors.toSet());
  }

  @Override
  public void deleteById(Long id) {
    channelRepository.deleteById(id);
  }

  @Override
  public List<ChannelDto> findAllChannelsExcludeVideos() {
    return channelRepository.findAll()
        .stream()
        .map(channelMapper::toDto)
        .peek(channel -> channel.setVideos(null))
        .toList();
  }

  @Override
  public List<ChannelDto> findAll() {
    return channelRepository.findAll()
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  public void updateAll(int range) {
    channelRepository.findAll()
        .forEach(channel -> updateChannelInRangeDays(channel.getId(), range));
  }

  private void validateUrlName(String urlName) {
    if (urlName.isBlank()) {
      throw new ChannelUrlNameValidationException(
          "Channel Url Name: \"" + urlName + "\" is invalid!");
    }
  }

  private String getUrlOfVideosPage1(String urlName) {
    return "https://www.youtube.com/@" + urlName + "/videos";
  }

  private String getUrlOfVideosPage2(String urlName) {
    return "https://www.youtube.com/c/" + urlName + "/videos";
  }

  private String getUrlOfVideosPage3(String urlName) {
    return "https://www.youtube.com/channel/" + urlName + "/videos";
  }

}
