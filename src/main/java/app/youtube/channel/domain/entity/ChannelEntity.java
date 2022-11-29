package app.youtube.channel.domain.entity;

import app.youtube.channel.video.domain.entity.VideoEntity;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "channel")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ChannelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "url_name")
  @NotBlank
  private String urlName;

  @Column(name = "title")
  @NotBlank
  private String title;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "channel_video_join",
      joinColumns = @JoinColumn(name = "channel_id"),
      inverseJoinColumns = @JoinColumn(name = "video_id"))
  private Set<VideoEntity> videos;

}
