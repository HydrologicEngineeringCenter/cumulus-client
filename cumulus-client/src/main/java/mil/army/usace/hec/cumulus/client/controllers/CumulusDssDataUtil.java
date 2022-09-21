package mil.army.usace.hec.cumulus.client.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.StringJoiner;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.Watershed;

public final class CumulusDssDataUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("ddMMMyyyy:HHmm")
        .toFormatter();

    private CumulusDssDataUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * Builds Dss path string for given Watershed and Product.
     * @param watershed - Watershed
     * @param product - Product
     * @return DSS path String
     */
    public static String buildDssPath(Watershed watershed, Product product) {
        StringJoiner retVal = new StringJoiner("/");
        String watershedName = watershed.getName();
        String parameter = product.getParameter();
        String dssFPart = product.getDssFPart();
        String partA = "SHG";
        String partB = watershedName != null ? watershedName : "";
        String partC = parameter != null ? parameter : "";
        String partD = "";
        String partE = "";
        ZonedDateTime beforeDate = product.getBefore();
        Integer duration = product.getTemporalDuration();
        if (beforeDate != null) {
            Instant start = beforeDate.toInstant().minusSeconds(duration);
            partD = ZonedDateTime.ofInstant(start, ZoneId.of("UTC")).format(DATE_TIME_FORMATTER);
            if (duration > 0) { //if periodic set E-Part, otherwise leave E-Part blank for instantaneous
                partE = beforeDate.format(DATE_TIME_FORMATTER);
            }
        }
        String partF = dssFPart != null ? dssFPart : "";
        retVal.add("/" + partA).add(partB).add(partC).add(partD).add(partE).add(partF + "/");
        return retVal.toString();
    }
}
