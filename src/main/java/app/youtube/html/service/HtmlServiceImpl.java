package app.youtube.html.service;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import app.youtube.html.exception.HtmlConnectionException;

@Service
public class HtmlServiceImpl implements HtmlService {

  @Override
  public String getHtml(String url) {

    try {

      return Jsoup.connect(url)
          .header("Accept-Language", "ru-RU")
          .get()
          .outerHtml();

    } catch (Exception e) {
      throw new HtmlConnectionException("Connection problem by url: " + url);
    }

  }

}
