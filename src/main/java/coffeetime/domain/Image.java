package coffeetime.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import coffeetime.domain.type.StatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(targetEntity = Coffee.class)
	@JoinColumn(name = "coffee_id", referencedColumnName = "id")
	private Coffee coffeeId;

	private String imageUrl;

	@CreatedDate
	private LocalDateTime createAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt = null;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private StatusType status = StatusType.USABLE;

	@Builder(access = AccessLevel.PRIVATE)
	private Image(Long id, Coffee coffeeId, String imageUrl, LocalDateTime createAt,
		LocalDateTime deletedAt,
		StatusType status) {
		this.id = id;
		this.coffeeId = coffeeId;
		this.imageUrl = imageUrl;
		this.createAt = createAt;
		this.deletedAt = deletedAt;
		this.status = status;
	}

	public static Image uploadImage(
		Coffee coffeeId,
		String imageUrl,
		LocalDateTime createAt,
		LocalDateTime deletedAt,
		StatusType status
	) {
		return Image.builder()
			.coffeeId(coffeeId)
			.imageUrl(imageUrl)
			.createAt(createAt)
			.deletedAt(deletedAt)
			.status(status)
			.build();
	}
}

