package fiap.stock.portal.address.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addresses")
public class Address {

    @Id
    private String id;

    private String zipCode;
    private String complement;
    private String city;
    private String state;
    private String country;

    public Address() {
    }

    public Address(String zipCode, String complement, String city, String state, String country) {
        this.zipCode = zipCode;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
