package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Download Request.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadRequest {

    @JsonProperty("datetime_start")
    private final ZonedDateTime dateTimeStart;

    @JsonProperty("datetime_end")
    private final ZonedDateTime dateTimeEnd;

    @JsonProperty("watershed_id")
    private final String watershedId;

    @JsonProperty("product_id")
    private final List<String> productIds;

    /**
     * Download Request defining start/end times, watershed, and products for download.
     * @param start - start of time window
     * @param end - end of time window
     * @param watershedId - watershed ID
     * @param productIds - list of products IDs
     */
    public DownloadRequest(ZonedDateTime start, ZonedDateTime end, String watershedId, List<String> productIds) {
        this.dateTimeStart = start;
        this.dateTimeEnd = end;
        this.watershedId = watershedId;
        this.productIds = productIds;
    }

    @Override
    public String toString() {
        return "DownloadRequest{"
            + "dateTimeStart=" + dateTimeStart
            + ", dateTimeEnd=" + dateTimeEnd
            + ", watershedId='" + watershedId + '\''
            + ", productIds=" + productIds
            + '}';
    }
}
