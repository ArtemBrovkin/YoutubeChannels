package app.youtube.channel.repository;

import app.youtube.channel.domain.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {

  boolean existsByUrlName(String urlName);
  ChannelEntity getByUrlName(String urlName);

}
