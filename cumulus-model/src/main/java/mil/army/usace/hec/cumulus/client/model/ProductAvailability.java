package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

/**
 * ProductAvailability
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-02T12:49:54.974-07:00[America/Los_Angeles]")

public class ProductAvailability {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("date_counts")
    private ProductDateCount[] dateCounts;

    /**
     * Get Product Id.
     *
     * @return productId
     */
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Get Date-Counts for product availability.
     *
     * @return dateCounts
     */
    public ProductDateCount[] getDateCounts() {
        return dateCounts;
    }

    public void setDateCounts(ProductDateCount[] dateCounts) {
        this.dateCounts = dateCounts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProductFile {\n");

        sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
        sb.append("    dateCounts: ").append(toIndentedString(Arrays.toString(dateCounts))).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
