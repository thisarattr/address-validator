package org.thisarattr.auspost.addressvalidator.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.thisarattr.auspost.addressvalidator.dtos.JsonAddress;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "postcode", nullable = false)
    private String postcode;
    @Column(name = "suburb", nullable = false)
    private String suburb;
    @Column(name = "state", nullable = false)
    private String state;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * This will convert current address object into a JsonAddress. JsonAddress will be used in apis.
     *
     * @return JsonAddress object
     */
    public JsonAddress getJsonAddress() {
        JsonAddress jsonAddress = new JsonAddress();
        jsonAddress.setId(this.id);
        jsonAddress.setPostcode(this.postcode);
        jsonAddress.setSuburb(this.suburb);
        jsonAddress.setState(this.state);
        if (this.latitude != null && this.longitude != null) {
            jsonAddress.setLocation(new JsonAddress.GeoJson(JsonAddress.Type.Point, this.longitude, this.latitude));
        }
        return jsonAddress;
    }
}
