package mil.army.usace.hec.cumulus.client.controllers;

import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.Watershed;

public final class CumulusDssDataUtil {

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
        String partA = "SHG";
        String partB = watershed.getName();
        String partC = product.getParameter();
        String partD = DateTimeFormatter.ofPattern("ddMMMyyyy:HHmm").format(product.getAfter());
        String partE = DateTimeFormatter.ofPattern("ddMMMyyyy:HHmm").format(product.getBefore());
        String partF = product.getDssFPart();
        retVal.add(partA).add(partB).add(partC).add(partD).add(partE).add(partF);
        return retVal.toString();
    }
}
