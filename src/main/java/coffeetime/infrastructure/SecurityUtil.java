package coffeetime.infrastructure;

import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

	public Long getCurrentId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			return Long.parseLong(authentication.getName());
		} catch (Exception e) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_AUTH);
		}
	}
}