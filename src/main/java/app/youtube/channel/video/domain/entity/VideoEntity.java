package app.youtube.channel.video.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  @NotBlank
  private String title;

  @Column(name = "video_url")
  @NotBlank
  private String videoUrl;

  @Column(name = "preview_url")
  @NotBlank
  private String previewUrl;

  @Column(name = "upload_date")
  @NotBlank
  private String uploadDate;

  @Column(name = "is_watched")
  private boolean isWatched;

}
