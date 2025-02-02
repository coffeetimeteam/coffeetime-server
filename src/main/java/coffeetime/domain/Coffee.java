package coffeetime.domain;

import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
	private User user;

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
	private List<Image> images = new ArrayList<>();

	@Builder
	private Coffee(
		final Long id,
		final User user,
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
		this.user = user;
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
		final User user,
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
			.user(user)
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
}
