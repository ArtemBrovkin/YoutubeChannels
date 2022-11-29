package app.youtube.unit.parser.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import app.youtube.parser.exception.NoMatchException;
import app.youtube.parser.service.VideoParserServiceImpl;

class VideoParserServiceImplTest {

  VideoParserServiceImpl service = new VideoParserServiceImpl();

  //  VIDEO_TITLE("(?<=<title>).+(?= - YouTube)"),
  //  PREVIEW_URL("(?<=\")https:\\/\\/i\\.ytimg\\.com\\/.+?(?=\\\\u)"),
  //  UPLOAD_DATE("(?<=\"dateText\":\\{\"simpleText\":\").+?(?=\"}}},)");

  @Test
  void getVideoTitleTestValid() {
    //arrange
    service.setPage("<title>123d_asd555 - YouTube");
    //act
    String videoTitle = service.getVideoTitle();
    //assert
    assertThat(videoTitle).isEqualTo("123d_asd555");
  }

  @Test
  void getVideoTitleTestEmpty() {
    //arrange
    service.setPage("");
    //assert
    assertThatExceptionOfType(NoMatchException.class)
        .isThrownBy(() -> service.getVideoTitle());
  }

  @Test
  void getVideoPreviewUrlTestValid() {
    //arrange
    String s1 = "\"https://i.ytimg.com/asd_123_bbb\\u";
    service.setPage(s1);
    //act
    String videoPreviewUrl = service.getVideoPreviewUrl();
    //assert
    assertThat(videoPreviewUrl).isEqualTo("https://i.ytimg.com/asd_123_bbb");
  }

  @Test
  void getVideoPreviewUrlTestEmpty() {
    //arrange
    service.setPage("");
    //assert
    assertThatExceptionOfType(NoMatchException.class)
        .isThrownBy(() -> service.getVideoPreviewUrl());
  }

  @Test
  void getVideoUploadDateTestValid() {
    //arrange
    service.setPage("\"dateText\":{\"simpleText\":\"_afsf22_fGGG\"}}},");
    //act
    String videoUploadDate = service.getVideoUploadDate();
    //assert
    assertThat(videoUploadDate).isEqualTo("_afsf22_fGGG");
  }

  @Test
  void getVideoUploadDateTestEmpty() {
    //arrange
    service.setPage("");
    //assert
    assertThatExceptionOfType(NoMatchException.class)
        .isThrownBy(() -> service.getVideoUploadDate());
  }
}