package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Download
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-02T12:49:54.974-07:00[America/Los_Angeles]")
public class Download {

    @JsonProperty("id")
    private String id;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("datetime_start")
    private ZonedDateTime dateTimeStart;

    @JsonProperty("datetime_end")
    private ZonedDateTime dateTimeEnd;

    @JsonProperty("watershed_id")
    private String watershedId;

    @JsonProperty("product_id")
    private String[] productId;

    @JsonProperty("status_id")
    private String statusId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("progress")
    private Integer progress;

    @JsonProperty("file")
    private String file;

    @JsonProperty("processing_start")
    private ZonedDateTime processingStart;

    @JsonProperty("processing_end")
    private ZonedDateTime processingEnd;

    @JsonProperty("watershed_slug")
    private String watershedSlug;

    @JsonProperty("watershed_name")
    private String watershedName;

    /**
     * Get id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get sub
     *
     * @return sub
     */
    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    /**
     * Get dateTimeStart
     *
     * @return dateTimeStart
     */
    public ZonedDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(ZonedDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    /**
     * Get dateTimeEnd
     *
     * @return dateTimeEnd
     */
    public ZonedDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(ZonedDateTime dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    /**
     * Get watershedId
     *
     * @return watershedId
     */
    public String getWatershedId() {
        return watershedId;
    }

    public void setWatershedId(String watershedId) {
        this.watershedId = watershedId;
    }

    /**
     * Get productId
     *
     * @return productId
     */
    public String[] getProductId() {
        return productId;
    }

    public void setProductId(String[] productId) {
        this.productId = productId;
    }

    /**
     * Get statusId
     *
     * @return statusId
     */
    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    /**
     * Get status
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get progress
     *
     * @return progress
     */
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * Get file
     *
     * @return file
     */
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Get processingStart
     *
     * @return processingStart
     */
    public ZonedDateTime getProcessingStart() {
        return processingStart;
    }

    public void setProcessingStart(ZonedDateTime processingStart) {
        this.processingStart = processingStart;
    }

    /**
     * Get processingEnd
     *
     * @return processingEnd
     */
    public ZonedDateTime getProcessingEnd() {
        return processingEnd;
    }

    public void setProcessingEnd(ZonedDateTime processingEnd) {
        this.processingEnd = processingEnd;
    }

    /**
     * Get watershedSlug
     *
     * @return watershedSlug
     */
    public String getWatershedSlug() {
        return watershedSlug;
    }

    public void setWatershedSlug(String watershedSlug) {
        this.watershedSlug = watershedSlug;
    }

    /**
     * Get watershedName
     *
     * @return watershedName
     */
    public String getWatershedName() {
        return watershedName;
    }

    public void setWatershedName(String watershedName) {
        this.watershedName = watershedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Download download = (Download) o;
        return Objects.equals(getId(), download.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Download {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    sub: ").append(toIndentedString(sub)).append("\n");
        sb.append("    datetime_start: ").append(toIndentedString(dateTimeStart)).append("\n");
        sb.append("    datetime_end: ").append(toIndentedString(dateTimeEnd)).append("\n");
        sb.append("    watershed_id: ").append(toIndentedString(watershedId)).append("\n");
        sb.append("    product_id: ").append(getArrayAsString(productId)).append("\n");
        sb.append("    status_id: ").append(toIndentedString(statusId)).append("\n");
        sb.append("    status: ").append(toIndentedString(statusId)).append("\n");
        sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
        sb.append("    file: ").append(toIndentedString(file)).append("\n");
        sb.append("    processing_start: ").append(toIndentedString(processingStart)).append("\n");
        sb.append("    processing_end: ").append(toIndentedString(processingEnd)).append("\n");
        sb.append("    watershed_slug: ").append(toIndentedString(watershedSlug)).append("\n");
        sb.append("    watershed_name: ").append(toIndentedString(watershedName)).append("\n");
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

    private String getArrayAsString(Object[] arrObjs) {
        StringBuilder sb = new StringBuilder("[");
        for(int i=0; i < arrObjs.length; i++){
            sb.append(toIndentedString(arrObjs[i]));
            if(i < arrObjs.length-1){
                sb.append(",");
                sb.append("\n");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
