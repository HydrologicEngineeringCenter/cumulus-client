package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Download Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadRequest {

    public DownloadRequest(ZonedDateTime start, ZonedDateTime end, String watershedId, List<String> productIds) {
        this.dateTimeStart = start;
        this.dateTimeEnd = end;
        this.watershedId = watershedId;
        this.productIds = productIds.toArray(new String[]{});
    }

    @JsonProperty("datetime_start")
    private ZonedDateTime dateTimeStart;

    @JsonProperty("datetime_end")
    private ZonedDateTime dateTimeEnd;

    @JsonProperty("watershed_id")
    private String watershedId;

    @JsonProperty("product_id")
    private String[] productIds;

}
