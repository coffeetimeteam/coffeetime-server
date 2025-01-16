package coffeetime.domain;

import coffeetime.domain.type.CoffeeScoreType;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import coffeetime.domain.type.UploadStatusType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "coffee")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coffee {

	@jakarta.persistence.Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User userId;

	private LocalDate rememberDate;

	private LocalTime rememberTime;

	@Enumerated(EnumType.STRING)
	private LocationType locationType;

	@Enumerated(EnumType.STRING)
	private CoffeeType coffeeType;

	@Enumerated(EnumType.STRING)
	private SizeType sizeType;

	@Enumerated(EnumType.STRING)
	private TasteType tasteType;

	@Enumerated(EnumType.STRING)
	private PriceType priceType;

	@Enumerated(EnumType.STRING)
	private CoffeeScoreType coffeeScoreType;

	@OneToMany(targetEntity = Image.class, cascade = CascadeType.REMOVE)
	private List<Image> images;

	@Enumerated(EnumType.STRING)
	private UploadStatusType uploadStatus;

	@Builder
	private Coffee(
		final Long id,
		final User userId,
		final LocalDate rememberDate,
		final LocalTime rememberTime,
		final LocationType locationType,
		final CoffeeType coffeeType,
		final SizeType sizeType,
		final TasteType tasteType,
		final PriceType priceType,
		final CoffeeScoreType coffeeScoreType,
		final List<Image> images,
		final UploadStatusType uploadStatus
	) {
		this.id = id;
		this.userId = userId;
		this.rememberDate = rememberDate;
		this.rememberTime = rememberTime;
		this.locationType = locationType;
		this.coffeeType = coffeeType;
		this.sizeType = sizeType;
		this.tasteType = tasteType;
		this.priceType = priceType;
		this.coffeeScoreType = coffeeScoreType;
		this.images = images != null ? images : new ArrayList<>();
		this.uploadStatus = uploadStatus;
	}

	public static CoffeeBuilder builderWithoutImages() {
		return builder()
			.images(new ArrayList<>());
	}

	public static CoffeeBuilder builderWithImages(List<Image> images) {
		return builder()
			.images(images);
	}
}

