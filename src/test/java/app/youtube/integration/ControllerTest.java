package app.youtube.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.repository.ChannelRepository;
import app.youtube.channel.video.domain.entity.VideoEntity;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
public class ControllerTest {

  @Container
  public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ChannelRepository channelRepository;

  @Test
  void hello() throws Exception {
    mockMvc.perform(get("/channel/hello"))
        .andExpect(content().string("Greetings Traveler!"));
  }

  @Test
  void createTestValid() throws Exception {
    mockMvc.perform(get("/channel/create?urlName=GohaMedia"))
        .andExpect(status().isCreated())
        .andExpect(elm -> assertThat(
            elm.getResponse().getContentAsString().contains("GohaMedia")).isTrue());
  }

  @Test
  void createTestInvalid() throws Exception {
    mockMvc.perform(get("/channel/create?urlName="))
        .andExpect(status().isBadRequest())
        .andExpect(elm -> assertThat(
            elm.getResponse().getContentAsString().contains("Your Channel URL Name is invalid!"))
            .isTrue());
  }

  @Test
  void updateTestValidButNotExist() throws Exception {
    mockMvc.perform(get("/channel/update/150?range=30"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Channel with such id: 150 don't exist"));
  }

  @Test
  void updateTestValidAndExistButThereIsNoOnYoutube() throws Exception {
    //arrange
    ChannelEntity entity = ChannelEntity.builder()
        .title("some title")
        .urlName("some url")
        .videos(new HashSet<>())
        .build();

    channelRepository.save(entity);

    //assert
    mockMvc.perform(get("/channel/update/1"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Channel with such urlName: " + entity.getUrlName()  + " don't exist"));
  }

  @Test
  void updateTestValidAndExist() throws Exception {
    //arrange
    String urlName = "GohaMedia";

    ChannelEntity entity = ChannelEntity.builder()
        .title("some title")
        .urlName(urlName)
        .videos(new HashSet<>())
        .build();

    ChannelEntity save = channelRepository.save(entity);

    //assert
    mockMvc.perform(get("/channel/update/" + save.getId()))
        .andExpect(status().isOk());

    assertThat(channelRepository.getByUrlName(urlName).getVideos()).isNotEmpty();
  }

  @Test
  void updateTestInvalid() throws Exception {
    mockMvc.perform(get("/channel/update/ttt"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Request is invalid"));
  }

  @Test
  void getUnseenVideosFromChannelTestValid() throws Exception {
    //arrange
    VideoEntity first_video = VideoEntity.builder()
        .title("first title")
        .videoUrl("video url 1")
        .uploadDate("upload date 1")
        .previewUrl("preview url 1")
        .build();

    VideoEntity second_video = VideoEntity.builder()
        .title("second title")
        .videoUrl("video url 2")
        .uploadDate("upload date 2")
        .previewUrl("preview url 2")
        .build();

    ChannelEntity entity = ChannelEntity.builder()
        .title("title")
        .urlName("url name")
        .videos(new HashSet<>(Set.of(first_video, second_video)))
        .build();

    channelRepository.save(entity);

    String[] id = new String[1];

    channelRepository.findAll().forEach(onlyOneChannel -> id[0] = String.valueOf(onlyOneChannel.getId()));
    //assert
    mockMvc.perform(get("/channel/" + id[0] + "/videos"))
        .andExpect(status().isOk())
        .andExpect(elm -> {
          String content = elm.getResponse().getContentAsString();

          assertThat(content.contains("first title")).isTrue();
          assertThat(content.contains("second title")).isTrue();
        });
  }

  @Test
  void getUnseenVideosFromChannelTestNotExist() throws Exception {
    mockMvc.perform(get("/channel/150/videos"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("There is not channel with id: 150"));
  }

  @Test
  void setWatchedTestValid() throws Exception {
    //arrange
    VideoEntity video = VideoEntity.builder()
        .isWatched(false)
        .videoUrl("video url")
        .uploadDate("upload date")
        .previewUrl("preview url")
        .title("title")
        .build();

    ChannelEntity channel = ChannelEntity.builder()
        .videos(new HashSet<>(Set.of(video)))
        .title("title")
        .urlName("url name")
        .build();

    channelRepository.save(channel);
    String[] id = new String[1];
    channelRepository.findAll().forEach(onlyOneChannel -> id[0] = String.valueOf(onlyOneChannel.getVideos().iterator().next().getId()));

    //assert
    mockMvc.perform(get("/channel/1/video/" + id[0] + "?isWatched=true"))
        .andExpect(status().isOk())
        .andExpect(
            content().string("Parameter \"isWatched\" in the video by id " + id[0] + " was set to true"));
  }

  @Test
  void setWatchedTestNotExist() throws Exception {
    //assert
    mockMvc.perform(get("/channel/150/video/100?isWatched=true"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Video ID is invalid"));
  }

  @Test
  void deleteChannel() throws Exception {
    //arrange
    ChannelEntity channel = ChannelEntity.builder()
        .videos(new HashSet<>())
        .title("title")
        .urlName("url name")
        .build();

    channelRepository.save(channel);
    String[] id = new String[1];
    channelRepository.findAll().forEach(onlyOneChannel -> id[0] = String.valueOf(onlyOneChannel.getId()));

    //assert
    mockMvc.perform(get("/channel/delete/" + id[0]))
        .andExpect(status().isAccepted())
        .andExpect(content().string("Channel by id " + id[0] + " was deleted successfully"));
  }

  @Test
  void findAll() throws Exception {
    mockMvc.perform(get("/channel/all"))
        .andExpect(status().isOk());
  }
}
