package org.thisarattr.auspost.addressvalidator.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.thisarattr.auspost.addressvalidator.dtos.AddressObj;

@Entity
@Table(name = "address", indexes = {
        @Index(columnList = "postcode", name = "idx_postcode"),
        @Index(columnList = "suburb", name = "idx_suburb")
})
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    /**
     * This will convert current address object into a AddressObj. AddressObj will be used in apis.
     *
     * @return AddressObj object
     */
    public AddressObj getJsonAddress() {
        AddressObj addressObj = new AddressObj();
        addressObj.setId(getId());
        addressObj.setPostcode(this.postcode);
        addressObj.setSuburb(this.suburb);
        addressObj.setState(this.state);
        if (this.latitude != null && this.longitude != null) {
            addressObj.setLocation(new AddressObj.GeoJson(AddressObj.Type.Point, this.longitude, this.latitude));
        }
        return addressObj;
    }
}
