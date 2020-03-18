package parsing;

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
    private List<Integer> bbox;
    private List<Integer> visueltCenter;
    private List<List<String>> kommuner;
    private String ændret;
    private String geo_ændret;
    private Integer geo_version;
    private String dagi_id;

    public ZipCode() {
    }

    public ZipCode(String href, String nr, String navn, String stormodtageradresser, List<Integer> bbox, List<Integer> visueltCenter, List<List<String>> kommuner, String ændret, String geo_ændret, Integer geo_version, String dagi_id) {
        this.href = href;
        this.nr = nr;
        this.navn = navn;
        this.stormodtageradresser = stormodtageradresser;
        this.bbox = bbox;
        this.visueltCenter = visueltCenter;
        this.kommuner = kommuner;
        this.ændret = ændret;
        this.geo_ændret = geo_ændret;
        this.geo_version = geo_version;
        this.dagi_id = dagi_id;
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

    public List<Integer> getBbox() {
        return bbox;
    }

    public void setBbox(List<Integer> bbox) {
        this.bbox = bbox;
    }

    public List<Integer> getVisueltCenter() {
        return visueltCenter;
    }

    public void setVisueltCenter(List<Integer> visueltCenter) {
        this.visueltCenter = visueltCenter;
    }

    public List<List<String>> getKommuner() {
        return kommuner;
    }

    public void setKommuner(List<List<String>> kommuner) {
        this.kommuner = kommuner;
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

    public Integer getGeo_version() {
        return geo_version;
    }

    public void setGeo_version(Integer geo_version) {
        this.geo_version = geo_version;
    }

    public String getDagi_id() {
        return dagi_id;
    }

    public void setDagi_id(String dagi_id) {
        this.dagi_id = dagi_id;
    }

}
