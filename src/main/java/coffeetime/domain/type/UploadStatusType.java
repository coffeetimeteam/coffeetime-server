package coffeetime.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadStatusType {
	NONE("no upload"),
	UPLOADING("uploading"),
	COMPLETE("upload complete");

	private final String uploadStatus;
}
