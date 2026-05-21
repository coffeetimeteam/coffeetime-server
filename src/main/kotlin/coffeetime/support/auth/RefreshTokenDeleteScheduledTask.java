package coffeetime.support.auth;

import static lombok.AccessLevel.PROTECTED;

import coffeetime.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
@AllArgsConstructor(access = PROTECTED)
public class RefreshTokenDeleteScheduledTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(
		RefreshTokenDeleteScheduledTask.class);

	private RefreshTokenRepository refreshTokenRepository;

	@Transactional
	@Scheduled(fixedDelay = 1800000)
	public void deleteExpiredRefreshTokens() {
		int tokenDelete = refreshTokenRepository.deleteByExpiredAt();
		LOGGER.info("Deleted expired refresh tokens: " + tokenDelete);
	}
}
