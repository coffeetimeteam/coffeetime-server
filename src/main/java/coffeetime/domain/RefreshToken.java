package coffeetime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(unique = true, nullable = false, length = 256)
	private String token;

	@Column(nullable = false)
	private Date expiredAt;

	public RefreshToken(final Integer id, final User user,final String token,
		final Date expiredAt) {
		this.id = id;
		this.user = user;
		this.token = token;
		this.expiredAt = expiredAt;
	}

	public RefreshToken(final User user, final String token, final Date expiredAt) {
		this(null, user, token, expiredAt);
	}
}
