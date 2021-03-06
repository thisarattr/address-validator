package org.thisarattr.auspost.addressvalidator.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.thisarattr.auspost.addressvalidator.models.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AddressObj {

    private Long id;
    private String postcode;
    private String suburb;
    private String state;
    private GeoJson location;

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

    public GeoJson getLocation() {
        return location;
    }

    public void setLocation(GeoJson location) {
        this.location = location;
    }

    @JsonIgnore
    public Address getAddress() {
        Address address = new Address();
        address.setPostcode(getPostcode());
        address.setSuburb(getSuburb());
        address.setState(getState());
        if (getLocation() != null) {
            address.setLongitude(getLocation().getCoordinates()[0]);
            address.setLatitude(getLocation().getCoordinates()[1]);
        }
        return address;
    }

    public static class GeoJson {
        private Type type;
        private Double[] coordinates;

        public GeoJson() {
        }

        public GeoJson(Type type, double longitude, double latitude) {
            this.type = type;
            this.coordinates = new Double[]{longitude, latitude};
        }

        public Type getType() {
            return type;
        }

        public Double[] getCoordinates() {
            return coordinates;
        }
    }

    public enum  Type {
        Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon, GeometryCollection, Feature,
        FeatureCollection;
    }
}
