package app.youtube.unit.parser.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import app.youtube.parser.exception.NoMatchException;
import app.youtube.parser.service.ChannelParserServiceImpl;

class ChannelParserServiceImplTest {

  ChannelParserServiceImpl service = new ChannelParserServiceImpl();

  @Test
  void getChannelNameTestValid() {
    //arrange
    service.setPage("skfslkfjklsf Автор: 1337Автор 8999");
    //act
    String channelName = service.getChannelName();
    //assert
    assertThat(channelName).isEqualTo("1337Автор");
  }

  @Test
  void getChannelNameTestEmpty() {
    //arrange
    service.setPage("");
    //assert
    assertThatExceptionOfType(NoMatchException.class)
        .isThrownBy(() -> service.getChannelName());
  }

  @Test
  void getNextVideoRelativeUrlTestValid() {
    //arrange
    service.setPage("f fd fsdjf \"/watchasndf244f\" 4r340r ");
    //act
    String nextVideoRelativeUrl = service.getNextVideoRelativeUrl();
    //assert
    assertThat(nextVideoRelativeUrl).isEqualTo("/watchasndf244f");
  }

  @Test
  void getNextVideoRelativeUrlTestEmpty() {
    //arrange
    service.setPage("");
    //assert
    assertThatExceptionOfType(NoMatchException.class)
        .isThrownBy(() -> service.getNextVideoRelativeUrl());
  }
}