package net.dusense.framework.core.cloud.version;

import lombok.Getter;
import org.springframework.http.MediaType;

/**
 * header Media Types，application/vnd.github.VERSION+json
 *
 * <p>https://developer.github.com/v3/media/
 */
@Getter
public class DusenseMediaType {
    private static final String MEDIA_TYPE_TEMP = "application/vnd.%s.%s+json";

    private final String appName = "dusense";
    private final String version;
    private final MediaType mediaType;

    public DusenseMediaType(String version) {
        this.version = version;
        this.mediaType = MediaType.valueOf(String.format(MEDIA_TYPE_TEMP, appName, version));
    }

    @Override
    public String toString() {
        return mediaType.toString();
    }
}
