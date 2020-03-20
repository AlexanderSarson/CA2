package parsing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oscar
 */
public class ZipCode {

    private String href;
    private String nr;
    private String navn;
    private String stormodtageradresser;
    private List<Double> bbox;
    private List<Double> visueltCenter;
    private List<kommuner> kommuner;
    private String ændret;
    private String geo_ændret;
    private Double geo_version;
    private String dagi_id;

    public ZipCode() {
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getStormodtageradresser() {
        return stormodtageradresser;
    }

    public void setStormodtageradresser(String stormodtageradresser) {
        this.stormodtageradresser = stormodtageradresser;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public List<Double> getVisueltCenter() {
        return visueltCenter;
    }

    public void setVisueltCenter(List<Double> visueltCenter) {
        this.visueltCenter = visueltCenter;
    }

    public String getÆndret() {
        return ændret;
    }

    public void setÆndret(String ændret) {
        this.ændret = ændret;
    }

    public String getGeo_ændret() {
        return geo_ændret;
    }

    public void setGeo_ændret(String geo_ændret) {
        this.geo_ændret = geo_ændret;
    }

    public Double getGeo_version() {
        return geo_version;
    }

    public void setGeo_version(Double geo_version) {
        this.geo_version = geo_version;
    }

    public String getDagi_id() {
        return dagi_id;
    }

    public void setDagi_id(String dagi_id) {
        this.dagi_id = dagi_id;
    }

    public List<kommuner> getKommuner() {
        return kommuner;
    }

    public void setKommuner(List<kommuner> kommuner) {
        this.kommuner = kommuner;
    }

    public static List<String> convertToZipCodeList(List<ZipCode> zipCodeList) {
        List<String> zipcodes = new ArrayList<>();
        zipCodeList.forEach(zipcode -> zipcodes.add(zipcode.getNr()));
        return zipcodes;
    }

    public static List<ZipCity> convertToZipCodeListWithCityName(List<ZipCode> zipCodeList) {
        List<ZipCity> zipcodes = new ArrayList<>();
        zipCodeList.forEach(zipcode -> zipcodes.add(new ZipCity(zipcode)));
        return zipcodes;
    }

}
