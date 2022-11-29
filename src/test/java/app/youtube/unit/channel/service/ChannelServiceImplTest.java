package app.youtube.unit.channel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import app.youtube.channel.domain.dto.ChannelDto;
import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.exception.ChannelExistence;
import app.youtube.channel.exception.ChannelUrlNameValidationException;
import app.youtube.channel.mapper.ChannelMapper;
import app.youtube.channel.repository.ChannelRepository;
import app.youtube.channel.service.ChannelServiceImpl;
import app.youtube.channel.video.service.VideoService;
import app.youtube.html.service.HtmlService;
import app.youtube.parser.service.ChannelParserService;
import java.util.Collections;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceImplTest {

  @Mock
  ChannelRepository channelRepository;
  @Mock
  HtmlService htmlService;
  @Mock
  ChannelParserService parser;
  @Mock
  VideoService videoService;
  @Mock
  ChannelMapper channelMapper;
  @Spy
  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @InjectMocks
  ChannelServiceImpl channelService;

  @Test
  void createChannelWithVideosInRangeDaysInvalid() {
    assertThatExceptionOfType(ChannelUrlNameValidationException.class)
        .isThrownBy(() -> channelService.createChannelWithVideosInRangeDays("", 14));
  }

  @Test
  void createChannelWithVideosInRangeDaysExist() {
    //arrange
    willReturn(true)
        .given(channelRepository)
        .existsByUrlName(any(String.class));

    willReturn(ChannelEntity.builder().title("title").build())
        .given(channelRepository)
            .getByUrlName(any(String.class));

    //assert
    assertThatExceptionOfType(ChannelExistence.class)
        .isThrownBy(() ->channelService.createChannelWithVideosInRangeDays("1111", 11));
  }

  @Test
  void updateChannelInRangeDaysException() {
    assertThatExceptionOfType(ChannelExistence.class)
        .isThrownBy(() -> channelService.updateChannelInRangeDays(150L, 11));
  }

  @Test
  void getUnseenVideosFromChannel() {
    assertThatExceptionOfType(ChannelExistence.class)
        .isThrownBy(() -> channelService.getUnseenVideosFromChannel(150L));
  }

  @Test
  void delete() {
    Long id = 150L;
    Long[] arg = new Long[1];
    //arrange
    willAnswer(a -> arg[0] = a.getArgument(0))
        .given(channelRepository)
        .deleteById(anyLong());

    //act
    channelService.deleteById(id);

    //assert
    assertThat(arg[0]).isEqualTo(id);
  }

  @Test
  void findAllChannelWithoutVideos() {
    //arrange
    willReturn(List.of())
        .given(channelRepository)
        .findAll();

    //act
    List<ChannelDto> allChannelExcludeVideos = channelService.findAllChannelsExcludeVideos();

    //assert
    assertThat(allChannelExcludeVideos).isEmpty();
  }

  @Test
  void findAll() {
    //arrange
    List<ChannelEntity> all = List.of(ChannelEntity.builder().build());
    willReturn(all)
        .given(channelRepository)
        .findAll();

    ChannelDto dto = ChannelDto.builder().build();
    willReturn(dto)
        .given(channelMapper)
        .toDto(any(ChannelEntity.class));

    //act
    List<ChannelDto> all1 = channelService.findAll();

    //assert
    assertThat(all1.size()).isEqualTo(1);
    assertThat(all1.get(0)).isSameAs(dto);
  }

  @Test
  void updateAll() {
    //arrange
    BDDMockito.willReturn(Collections.emptyList())
        .given(channelRepository)
            .findAll();
    //act
    channelService.updateAll(30);
    //assert
    Mockito.verify(channelRepository, times(1)).findAll();
  }

  @Test
  void validator() {
    ChannelEntity entity = ChannelEntity.builder().build();
    ChannelEntity entity2 = ChannelEntity.builder()
        .title("")
        .urlName("urlName")
        .build();
    ChannelEntity entity3 = ChannelEntity.builder()
        .title("title")
        .urlName("")
        .build();

    assertThat(validator.validate(entity).isEmpty()).isFalse();
    assertThat(validator.validate(entity2).isEmpty()).isFalse();
    assertThat(validator.validate(entity3).isEmpty()).isFalse();
  }
}