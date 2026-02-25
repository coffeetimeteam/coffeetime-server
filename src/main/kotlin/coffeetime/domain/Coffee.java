package coffeetime.domain;

import coffeetime.enums.CoffeeType;
import coffeetime.enums.LocationType;
import coffeetime.enums.PriceType;
import coffeetime.enums.SizeType;
import coffeetime.enums.TasteType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
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

	@Column(columnDefinition = "BINARY(16)", nullable = false)
	private UUID memberId;

	@Column(nullable = false)
	private LocalDate rememberDate;

	@Column(nullable = false, columnDefinition = "TIME(0)")
	private LocalTime rememberTime;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private LocationType locationType;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private CoffeeType coffeeType;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private SizeType sizeType;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private TasteType tasteType;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private PriceType priceType;

	@Column(nullable = false)
	private Integer coffeeScore;

	@OneToMany(mappedBy = "coffee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images;

	@Builder
	private Coffee(
		final Long id,
		final UUID memberId,
		final LocalDate rememberDate,
		final LocalTime rememberTime,
		final LocationType locationType,
		final CoffeeType coffeeType,
		final SizeType sizeType,
		final TasteType tasteType,
		final PriceType priceType,
		final Integer coffeeScore,
		final List<Image> images
	) {
		this.id = id;
		this.memberId = memberId;
		this.rememberDate = rememberDate;
		this.rememberTime = rememberTime;
		this.locationType = locationType;
		this.coffeeType = coffeeType;
		this.sizeType = sizeType;
		this.tasteType = tasteType;
		this.priceType = priceType;
		this.coffeeScore = coffeeScore;
		this.images = images;
	}

	public static Coffee create(
		final UUID memberId,
		final LocalDate rememberDate,
		final LocalTime rememberTime,
		final LocationType locationType,
		final CoffeeType coffeeType,
		final SizeType sizeType,
		final TasteType tasteType,
		final PriceType priceType,
		final Integer coffeeScore,
		final List<Image> images
	) {
		return Coffee.builder()
			.memberId(memberId)
			.rememberDate(rememberDate)
			.rememberTime(rememberTime)
			.locationType(locationType)
			.coffeeType(coffeeType)
			.sizeType(sizeType)
			.tasteType(tasteType)
			.priceType(priceType)
			.coffeeScore(coffeeScore)
			.images(images)
			.build();
	}

	public UUID getMemberId() {
		return memberId;
	}

	public LocalDate getRememberDate() {
		return rememberDate;
	}

	public LocalTime getRememberTime() {
		return rememberTime;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public CoffeeType getCoffeeType() {
		return coffeeType;
	}

	public SizeType getSizeType() {
		return sizeType;
	}

	public TasteType getTasteType() {
		return tasteType;
	}

	public PriceType getPriceType() {
		return priceType;
	}

	public Integer getCoffeeScore() {
		return coffeeScore;
	}

	public List<Image> getImages() {
		return images;
	}
}
