package app.youtube.unit.channel.repository;

import static org.assertj.core.api.Assertions.assertThat;

import app.youtube.channel.domain.entity.ChannelEntity;
import app.youtube.channel.repository.ChannelRepository;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
class ChannelRepositoryTest {

  @Container
  public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }

  @Autowired
  ChannelRepository channelRepository;

  @Test
  void existsByUrlNameNotExistTest() {
    assertThat(channelRepository.existsByUrlName("111")).isFalse();
  }

  @Test
  void existsByUrlNameExistTest() {
    //arrange
    String urlName = "urlName123";

    ChannelEntity entity = ChannelEntity.builder()
        .urlName(urlName)
        .title("title")
        .videos(new HashSet<>())
        .build();

    channelRepository.save(entity);

    //assert
    assertThat(channelRepository.existsByUrlName(urlName)).isTrue();
  }

  @Test
  void getByUrlNameTestNotExist() {
    //arrange
    String urlName = "urlName123";

    //act
    ChannelEntity channel = channelRepository.getByUrlName(urlName);

    //assert
    assertThat(channel).isNull();
  }

  @Test
  void getByUrlNameTestDoExist() {
    //arrange
    String urlName = "urlName123";

    ChannelEntity entity = ChannelEntity.builder()
        .urlName(urlName)
        .title("title")
        .videos(new HashSet<>())
        .build();

    channelRepository.save(entity);

    //act
    ChannelEntity channel = channelRepository.getByUrlName(urlName);

    //assert
    assertThat(channel).isNotNull();
  }

}