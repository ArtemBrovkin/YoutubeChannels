package app.youtube.unit.channel.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.service.ChannelService;
import app.youtube.channel.video.domain.dto.VideoDto;
import app.youtube.channel.video.service.VideoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class ControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ChannelService channelService;

  @MockBean
  VideoService videoService;


  @Test
  void helloTest() throws Exception {
    mockMvc.perform(get("/channel/hello"))
        .andExpect(content().string("Greetings Traveler!"));
  }

  @Test
  void createTest() throws Exception {
    //arrange
    willReturn(ChannelEntity.builder().build())
        .given(channelService)
            .createChannelWithVideosInRangeDays(anyString(), anyInt());

    //assert
    mockMvc.perform(get("/channel/create?urlName=channel&range=30"))
        .andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    //arrange
    willReturn(ChannelEntity.builder().build())
            .given(channelService)
                .updateChannelInRangeDays(anyLong(), anyInt());

    //assert
    mockMvc.perform(get("/channel/update/7?range=30"))
        .andExpect(status().isOk());
  }

  @Test
  void getUnseenVideosFromChannelTest() throws Exception {
    //arrange
    HashSet<VideoDto> videos = new HashSet<>();
    videos.add(VideoDto.builder().title("title1").build());
    videos.add(VideoDto.builder().previewUrl("preview2").build());

    willReturn(videos)
        .given(channelService)
        .getUnseenVideosFromChannel(anyLong());

    //assert
    mockMvc.perform(get("/channel/444/videos"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(videos)));
  }

  @Test
  void setWatchedTest() throws Exception {

    mockMvc.perform(get("/channel/1/video/4?isWatched=true"))
        .andExpect(content().string("Parameter \"isWatched\" in the video by id 4 was set to true"));

    mockMvc.perform(get("/channel/55/video/777?isWatched=false"))
        .andExpect(content().string("Parameter \"isWatched\" in the video by id 777 was set to false"));
  }

  @Test
  void deleteChannelTest() throws Exception {

    mockMvc.perform(get("/channel/delete/1"))
        .andExpect(content().string("Channel by id 1 was deleted successfully"));

    mockMvc.perform(get("/channel/delete/555"))
        .andExpect(content().string("Channel by id 555 was deleted successfully"));
  }

  @Test
  void findAllTest() throws Exception {
    //arrange
    List<ChannelDto> list = new ArrayList<>();
    list.add(ChannelDto.builder().build());
    list.add(ChannelDto.builder().build());

    willReturn(list)
        .given(channelService)
        .findAll();

    //assert
    mockMvc.perform(get("/channel/all"))
        .andExpect(content().string(objectMapper.writeValueAsString(list)));
  }
}