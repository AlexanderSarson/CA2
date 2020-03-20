package parsing;

/**
 *
 * @author oscar
 */
public class ZipCity {
    
    private String city;
    private String zipcode;

    public ZipCity(ZipCode zip) {
        this.city = zip.getNavn();
        this.zipcode = zip.getNr();
    }

    public ZipCity() {
    }

    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    
    
}
