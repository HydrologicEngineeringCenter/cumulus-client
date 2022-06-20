package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Arrays;
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
    private final String[] productIds;

    /**
     * Download Request defining start/end times, watershed, and products for download.
     * @param start - start of time window
     * @param end - end of time window
     * @param watershed - watershed
     * @param products - list of products
     */
    public DownloadRequest(ZonedDateTime start, ZonedDateTime end, Watershed watershed, List<Product> products) {
        this.dateTimeStart = start;
        this.dateTimeEnd = end;
        this.watershedId = watershed.getId();
        this.productIds = buildProductIdList(products);
    }

    private String[] buildProductIdList(List<Product> products) {
        String[] retVal = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            retVal[i] = products.get(i).getId();
        }
        return retVal;
    }

    @Override
    public String toString() {
        return "DownloadRequest{"
            + "dateTimeStart=" + dateTimeStart
            + ", dateTimeEnd=" + dateTimeEnd
            + ", watershedId='" + watershedId + '\''
            + ", productIds=" + Arrays.toString(productIds)
            + '}';
    }
}
