package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * Product
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@javax.annotation.processing.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-02T12:49:54.974-07:00[America/Los_Angeles]")
public class Product {

    @JsonProperty("id")
    private String id;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("name")
    private String name;

    @JsonProperty("tags")
    private String[] tags;

    @JsonProperty("temporal_resolution")
    private Integer temporalResolution;

    @JsonProperty("temporal_duration")
    private Integer temporalDuration;

    @JsonProperty("dss_fpart")
    private String dssFPart;

    @JsonProperty("parameter_id")
    private String parameterId;

    @JsonProperty("parameter")
    private String parameter;

    @JsonProperty("unit_id")
    private String unitId;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("description")
    private String description;

    @JsonProperty("suite_id")
    private String suiteId;

    @JsonProperty("suite")
    private String suite;

    @JsonProperty("label")
    private String label;

    @JsonProperty("before")
    private ZonedDateTime before;

    @JsonProperty("after")
    private ZonedDateTime after;

    @JsonProperty("productfile_count")
    private int productFileCount;

    @JsonProperty("last_forecast_version")
    private String lastForecastVersion;

    @JsonProperty("dss_datatype")
    private String dssDatatype;


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
     * Get slug
     *
     * @return slug
     */
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Get name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get tags
     *
     * @return tags
     */
    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    /**
     * Get temportal resolution
     *
     * @return temporalResolution
     */
    public Integer getTemporalResolution() {
        return temporalResolution;
    }

    public void setTemporalResolution(Integer temporalResolution) {
        this.temporalResolution = temporalResolution;
    }

    /**
     * Get temportal duration
     *
     * @return temporalDuration
     */
    public Integer getTemporalDuration() {
        return temporalDuration;
    }

    public void setTemporalDuration(Integer temporalDuration) {
        this.temporalDuration = temporalDuration;
    }

    /**
     * Get dssDatatype
     * @return dssDatatype
     **/

    public String getDssDatatype() {
        return dssDatatype;
    }

    public void setDssDatatype(String dssDatatype) {
        this.dssDatatype = dssDatatype;
    }

    /**
     * Get dss f-part
     *
     * @return dssFPart
     */
    public String getDssFPart() {
        return dssFPart;
    }

    public void setDssFPart(String dssFPart) {
        this.dssFPart = dssFPart;
    }

    /**
     * Get parameter id
     *
     * @return parameterId
     */
    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    /**
     * Get parameter
     *
     * @return parameter
     */
    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Get unit id
     *
     * @return unitId
     */
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    /**
     * Get unit
     *
     * @return unit
     */
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Get description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get Suite UUID.
     *
     * @return suiteId
     */
    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    /**
     * Get Suite.
     *
     * @return
     */
    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    /**
     * Get Label.
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get End of Time Window.
     *
     * @return before
     */
    public ZonedDateTime getBefore() {
        return before;
    }

    public void setBefore(ZonedDateTime before) {
        this.before = before;
    }

    /**
     * Get Beginning of Time Window.
     *
     * @return after
     */
    public ZonedDateTime getAfter() {
        return after;
    }

    public void setAfter(ZonedDateTime after) {
        this.after = after;
    }

    /**
     * Get Product File Count
     *
     * @return productFileCount
     */
    public int getProductFileCount() {
        return productFileCount;
    }

    public void setProductFileCount(int productFileCount) {
        this.productFileCount = productFileCount;
    }

    /**
     * Get Last Forecast Version
     *
     * @return lastForecastVersion
     */
    public String getLastForecastVersion() {
        return lastForecastVersion;
    }

    public void setLastForecastVersion(String lastForecastVersion) {
        this.lastForecastVersion = lastForecastVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getName();
    }

    public String toExtendedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Product {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    slug: ").append(toIndentedString(slug)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    tags: ").append(toIndentedString(Arrays.toString(tags))).append("\n");
        sb.append("    temporal_resolution: ").append(toIndentedString(temporalResolution)).append("\n");
        sb.append("    temporal_duration: ").append(toIndentedString(temporalDuration)).append("\n");
        sb.append("    dss_fpart: ").append(toIndentedString(dssFPart)).append("\n");
        sb.append("    dss_datatype: ").append(toIndentedString(dssDatatype)).append("\n");
        sb.append("    parameter_id: ").append(toIndentedString(parameterId)).append("\n");
        sb.append("    parameter: ").append(toIndentedString(parameter)).append("\n");
        sb.append("    unit_id: ").append(toIndentedString(unitId)).append("\n");
        sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    suite_id: ").append(toIndentedString(suiteId)).append("\n");
        sb.append("    suite: ").append(toIndentedString(suite)).append("\n");
        sb.append("    label: ").append(toIndentedString(label)).append("\n");
        sb.append("    after: ").append(toIndentedString(after)).append("\n");
        sb.append("    before: ").append(toIndentedString(before)).append("\n");
        sb.append("    productfile_count: ").append(toIndentedString(productFileCount)).append("\n");
        sb.append("    last_forecast_version: ").append(toIndentedString(lastForecastVersion)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private static String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
