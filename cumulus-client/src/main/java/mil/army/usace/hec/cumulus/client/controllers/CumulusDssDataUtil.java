package mil.army.usace.hec.cumulus.client.controllers;

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
        String watershedName = watershed.getName();
        String parameter = product.getParameter();
        String dssFPart = product.getDssFPart();
        String partA = "SHG";
        String partB = watershedName != null ? watershedName : "";
        String partC = parameter != null ? parameter : "";
        String partD = "";
        String partE = "";
        String partF = dssFPart != null ? dssFPart : "";
        retVal.add("/" + partA).add(partB).add(partC).add(partD).add(partE).add(partF + "/");
        return retVal.toString();
    }
}
