package coffeetime.domain;


import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "\"user\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@FilterDef(
	name = "deletedFilter",
	parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@Size(min = 2, max = 20)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(name = "login_type", nullable = false)
	private LoginType loginType;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private RoleType role;

	@Column(name = "last_login_date")
	private LocalDateTime lastLoginDate;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

	@Column(name = "deleted_at", nullable = true)
	private LocalDateTime deletedAt = null;

	@Builder
	public User(final Long id, final LoginType loginType, final String username,
		final String nickname, final String password, final RoleType role) {
		this.id = id;
		this.loginType = loginType;
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.role = role;
		this.modifiedAt = LocalDateTime.now();
		this.lastLoginDate = LocalDateTime.now();
	}

	public static User createUser(final LoginType loginType, final String username,
		final String nickname, final String password, final RoleType role) {
		return User.builder()
			.loginType(loginType)
			.username(username)
			.nickname(nickname)
			.password(password)
			.role(role)
			.build();
	}


	public User(final LoginType loginType, final String username, final String nickname,
		final String password, final RoleType role) {
		this.loginType = loginType;
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.role = role;
		this.modifiedAt = LocalDateTime.now();
	}

	public User(final LoginType loginType, final Long id, final String username,
		final String nickname, final RoleType role) {
		this(id, loginType, username, nickname, null, role);
	}

	public User(final Long id, final String username, final RoleType role) {
		this(id, null, username, null, null, role);
	}

	public User(final String username, final String password) {
		this(null, username, null, password, null);
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
