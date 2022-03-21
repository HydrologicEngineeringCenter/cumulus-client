package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * AcquirableFile
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-02T12:49:54.974-07:00[America/Los_Angeles]")
public class AcquirableFile {

    @JsonProperty("acquirable_id")
    private String acquirableId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("datetime")
    private ZonedDateTime dateTime;

    @JsonProperty("file")
    private String file;

    @JsonProperty("create_date")
    private ZonedDateTime createDate;

    @JsonProperty("process_date")
    private ZonedDateTime processDate;


    /**
     * Get id of acquirable file
     *
     * @return acquirableId
     */
    public String getAcquirableId() {
        return acquirableId;
    }

    public void setAcquirableId(String acquirableId) {
        this.acquirableId = acquirableId;
    }

    /**
     * Get artifact id
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
     * Get file name
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
     * Get date-time of acquirable file
     *
     * @return dateTime
     */
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get create date of acquirable file
     *
     * @return createDate
     */
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Get process date of acquirable file
     *
     * @return processDate
     */
    public ZonedDateTime getProcessDate() {
        return processDate;
    }

    public void setProcessDate(ZonedDateTime processDate) {
        this.processDate = processDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id == null ? 0 : id.toLowerCase(), dateTime == null ? 0 : dateTime.toString().toLowerCase(),
            file == null ? 0 : file.toLowerCase(),  createDate == null ? 0 : createDate.toString().toLowerCase(),
            processDate == null ? 0 : processDate.toString().toLowerCase(), acquirableId == null ? 0 : acquirableId.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcquirableFile that = (AcquirableFile) o;
        return Objects.equals(getAcquirableId(), that.getAcquirableId()) && Objects.equals(getId(), that.getId()) &&
            Objects.equals(getDateTime(), that.getDateTime()) && Objects.equals(getFile(), that.getFile()) &&
            Objects.equals(getCreateDate(), that.getCreateDate()) && Objects.equals(getProcessDate(), that.getProcessDate());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AcquirableFile {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    datetime: ").append(toIndentedString(dateTime)).append("\n");
        sb.append("    file: ").append(toIndentedString(file)).append("\n");
        sb.append("    createDate: ").append(toIndentedString(createDate)).append("\n");
        sb.append("    processDate: ").append(toIndentedString(processDate)).append("\n");
        sb.append("    acquirableId: ").append(toIndentedString(acquirableId)).append("\n");
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
