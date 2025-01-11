package coffeetime.domain;

import static lombok.AccessLevel.PROTECTED;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@FilterDef(
    name = "deletedFilter",
    parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Role role;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt = null;

    public User(final Long id, final LoginType loginType, final String username,  final String nickname, final String password, final Role role) {
        this.id = id;
        this.loginType = loginType;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.modifiedAt = LocalDateTime.now();
        this.lastLoginDate = LocalDateTime.now();
    }

    public User(final LoginType loginType, final String username,  final String nickname, final String password, final Role role) {
        this.loginType = loginType;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.modifiedAt = LocalDateTime.now();
    }

    public User(final LoginType loginType, final Long id, final String username, final String nickname, final Role role) {
        this(id, loginType, username, nickname, null, role);
    }

    public User(final Long id, final String username, final Role role) {
        this(id, null, username, null, null, role);
    }

    public User(final String username, final String password) {
       this(null,  username, null, password, null);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
