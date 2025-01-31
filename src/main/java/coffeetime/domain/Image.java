package coffeetime.domain;

import coffeetime.domain.type.StatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE image SET status = 'DELETED' WHERE id = ?")
@SQLRestriction("status <> 'DELETED'")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coffee_id", referencedColumnName = "id")
	private Coffee coffee;

	@Column(nullable = false, unique = true)
	private String url;

	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt = null;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private StatusType status = StatusType.ACTIVE;

	@Builder
	private Image(Long id, Coffee coffee, String url, LocalDateTime createdAt,
		LocalDateTime deletedAt,
		StatusType status) {
		this.id = id;
		this.coffee = coffee;
		this.url = url;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.status = status;
	}

	public static List<Image> saveImage(Coffee coffee, List<String> urls) {
		return urls.stream()
			.map(url -> Image.builder()
				.coffee(coffee)
				.url(url)
				.createdAt(LocalDateTime.now())
				.status(StatusType.ACTIVE)
				.build())
			.collect(Collectors.toList());
	}
}

