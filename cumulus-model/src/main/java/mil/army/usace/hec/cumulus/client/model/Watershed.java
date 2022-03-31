package mil.army.usace.hec.cumulus.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Objects;

/**
 * Watershed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-02T12:49:54.974-07:00[America/Los_Angeles]")
public class Watershed {

    @JsonProperty("id")
    private String id;

    @JsonProperty("office_symbol")
    private String officeSymbol;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("name")
    private String name;

    @JsonProperty("area_groups")
    private String[] areaGroups;

    @JsonProperty("bbox")
    private Integer[] bbox;

    /**
     * Get Id of Watershed
     *
     * @return
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get office symbol for Watershed
     *
     * @return
     */
    public String getOfficeSymbol() {
        return officeSymbol;
    }

    public void setOfficeSymbol(String officeSymbol) {
        this.officeSymbol = officeSymbol;
    }

    /**
     * Get Watershed slug
     *
     * @return
     */
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * Get Watershed name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Watershed's area groups
     *
     * @return
     */
    public String[] getAreaGroups() {
        return areaGroups;
    }

    public void setAreaGroups(String[] areaGroups) {
        this.areaGroups = areaGroups;
    }

    /**
     * Get Watershed bbox
     *
     * @return
     */
    public Integer[] getBbox() {
        return bbox;
    }

    public void setBbox(Integer[] bbox) {
        this.bbox = bbox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Watershed watershed = (Watershed) o;
        return Objects.equals(getId(), watershed.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Watershed {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    office_symbol: ").append(toIndentedString(officeSymbol)).append("\n");
        sb.append("    slug: ").append(toIndentedString(slug)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    area_groups: ").append(toIndentedString(Arrays.toString(areaGroups))).append("\n");
        sb.append("    bbox: ").append(toIndentedString(Arrays.toString(bbox))).append("\n");
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
