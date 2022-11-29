package app.youtube.unit.html.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import app.youtube.html.exception.HtmlConnectionException;
import app.youtube.html.service.HtmlServiceImpl;
import org.junit.jupiter.api.Test;

class HtmlServiceImplTest {

  HtmlServiceImpl service = new HtmlServiceImpl();

  @Test
  void getHtmlTestValid() {
    //arrange
    String validUrl = "https://www.yandex.ru";
    //act
    String html = service.getHtml(validUrl);
    //assert
    assertThat(html).isNotBlank();
  }

  @Test
  void getHtmlTestInvalid() {
    //arrange
    String invalidUrl = "yandex.ru";
    //assert
    assertThatExceptionOfType(HtmlConnectionException.class)
        .isThrownBy(() -> service.getHtml(invalidUrl));
  }

  @Test
  void getHtmlTestEmpty() {
    //arrange
    String emptyUrl = "";
    //assert
    assertThatExceptionOfType(HtmlConnectionException.class)
        .isThrownBy(() -> service.getHtml(emptyUrl));
  }
}