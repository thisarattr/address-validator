package org.thisarattr.auspost.addressvalidator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.thisarattr.auspost.addressvalidator.dtos.AddressObj;
import org.thisarattr.auspost.addressvalidator.dtos.AddressResponse;
import org.thisarattr.auspost.addressvalidator.dtos.LoginRequest;
import org.thisarattr.auspost.addressvalidator.dtos.LoginResponse;
import org.thisarattr.auspost.addressvalidator.dtos.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AddressValidatorIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldReturnSingleObjectAddressArrayWhenSearchByPostcodeForExactMatch() {
        AddressResponse addressRes = restTemplate
                .getForObject("/address/postcode/200", AddressResponse.class);

        assertThat(addressRes.getAddresses(), hasSize(1));
        AddressObj address0 = addressRes.getAddresses().get(0);
        assertThat(address0.getSuburb(), is("AUSTRALIAN NATIONAL UNIVERSITY"));
        assertThat(address0.getPostcode(), is("200"));
        assertThat(address0.getState(), is("ACT"));
        assertThat(address0.getLocation().getCoordinates()[0], is(149.117136));
        assertThat(address0.getLocation().getCoordinates()[1], is(-35.277272));
    }

    @Test
    public void shouldReturnAddressArrayOrderByPostcodeWhenSearchByPostcode() {
        AddressResponse addressRes = restTemplate
                .getForObject("/address/postcode/2", AddressResponse.class);

        assertThat(addressRes.getAddresses(), hasSize(2));
        AddressObj address0 = addressRes.getAddresses().get(0);
        assertThat(address0.getSuburb(), is("AUSTRALIAN NATIONAL UNIVERSITY"));
        assertThat(address0.getPostcode(), is("200"));
        assertThat(address0.getState(), is("ACT"));
        AddressObj address1 = addressRes.getAddresses().get(1);
        assertThat(address1.getSuburb(), is("BARTON"));
        assertThat(address1.getPostcode(), is("221"));
        assertThat(address1.getState(), is("ACT"));
    }

    @Test
    public void shouldReturnSingleObjectAddressArrayWhenSearchBySuburbForExactMatch() {
        AddressResponse addressRes = restTemplate
                .getForObject("/address/suburb/barton", AddressResponse.class);

        assertThat(addressRes.getAddresses(), hasSize(1));
        AddressObj address0 = addressRes.getAddresses().get(0);
        assertThat(address0.getSuburb(), is("BARTON"));
        assertThat(address0.getPostcode(), is("221"));
        assertThat(address0.getState(), is("ACT"));
        assertThat(address0.getLocation().getCoordinates()[0], is(149.095065));
        assertThat(address0.getLocation().getCoordinates()[1], is(-35.201372));
    }

    @Test
    public void shouldReturnAddressArrayOrderBySuburbWhenSearchBySuburb() {
        AddressResponse addressRes = restTemplate
                .getForObject("/address/suburb/da", AddressResponse.class);

        assertThat(addressRes.getAddresses(), hasSize(2));
        AddressObj address0 = addressRes.getAddresses().get(0);
        assertThat(address0.getSuburb(), is("DALY RIVER"));
        assertThat(address0.getPostcode(), is("822"));
        assertThat(address0.getState(), is("NT"));
        AddressObj address1 = addressRes.getAddresses().get(1);
        assertThat(address1.getSuburb(), is("DARWIN"));
        assertThat(address1.getPostcode(), is("800"));
        assertThat(address1.getState(), is("NT"));
    }

    private LoginResponse getLoginResponse() {
        LoginRequest loginReq = new LoginRequest("user", "password");
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange("/login", HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(200));
        return loginResEntity.getBody();
    }

    @Test
    public void shouldSaveAddress() {
        final LoginResponse loginResponse = getLoginResponse();

        AddressObj reqObj = new AddressObj();
        reqObj.setPostcode("3168");
        reqObj.setState("VIC");
        reqObj.setSuburb("Clayton");
        reqObj.setLocation(new AddressObj.GeoJson(AddressObj.Type.Point, 149.095065, -35.201372));

        MultiValueMap<String, String> reqHeaders = new LinkedMultiValueMap<>();
        reqHeaders.add("Authorization", "Bearer " + loginResponse.getJwtToken());
        reqHeaders.add("Content-Type", "application/json");
        HttpEntity<AddressObj> reqEntity = new HttpEntity<>(reqObj, reqHeaders);

        ResponseEntity<Response> resEntity = restTemplate
                .exchange("/admin/address", HttpMethod.POST, reqEntity, Response.class);
        assertThat(resEntity.getStatusCode().value(), is(200));

        AddressResponse addressRes = restTemplate
                .getForObject("/address/suburb/clayton", AddressResponse.class);

        assertThat(addressRes.getAddresses(), hasSize(1));
        AddressObj address0 = addressRes.getAddresses().get(0);
        assertThat(address0.getSuburb(), is("Clayton"));
        assertThat(address0.getPostcode(), is("3168"));
        assertThat(address0.getState(), is("VIC"));
        assertThat(address0.getLocation().getCoordinates()[0], is(149.095065));
        assertThat(address0.getLocation().getCoordinates()[1], is(-35.201372));
    }

    @Test
    public void shouldFailSaveAddressMandatoryFieldsNotProvided() {
        final LoginResponse loginResponse = getLoginResponse();

        AddressObj reqObj = new AddressObj();
        reqObj.setPostcode("3168");
        reqObj.setState("VIC");
        reqObj.setLocation(new AddressObj.GeoJson(AddressObj.Type.Point, 149.095065, -35.201372));

        MultiValueMap<String, String> reqHeaders = new LinkedMultiValueMap<>();
        reqHeaders.add("Authorization", "Bearer " + loginResponse.getJwtToken());
        reqHeaders.add("Content-Type", "application/json");
        HttpEntity<AddressObj> reqEntity = new HttpEntity<>(reqObj, reqHeaders);

        ResponseEntity<Response> resEntity = restTemplate
                .exchange("/admin/address", HttpMethod.POST, reqEntity, Response.class);
        assertThat(resEntity.getStatusCode().value(), is(400));
        assertThat(resEntity.getBody().getMessage(), is("Postcode, suburb and state are mandatory fields"));

    }

    @Test
    public void shouldFailSaveAddressWhenExpiredTokenProvided() {
        AddressObj reqObj = new AddressObj();
        reqObj.setPostcode("3168");
        reqObj.setState("VIC");
        reqObj.setSuburb("Clayton");
        reqObj.setLocation(new AddressObj.GeoJson(AddressObj.Type.Point, 149.095065, -35.201372));

        MultiValueMap<String, String> reqHeaders = new LinkedMultiValueMap<>();
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIiLCJpYXQiOjE0OTk1ODg4ODF9."
                + "MFoBA1sbZWapVAyMpi-btZ-lQYsEVMiD45ptJxbQ4Lc";
        reqHeaders.add("Authorization", "Bearer " + invalidToken);
        reqHeaders.add("Content-Type", "application/json");
        HttpEntity<AddressObj> reqEntity = new HttpEntity<>(reqObj, reqHeaders);

        ResponseEntity<Response> resEntity = restTemplate
                .exchange("/admin/address", HttpMethod.POST, reqEntity, Response.class);
        assertThat(resEntity.getStatusCode().value(), is(401));
        assertThat(resEntity.getBody().getMessage(), containsString("Token expired"));
    }

    @Test
    public void shouldFailSaveAddressWhenInvalidTokenProvided() {
        AddressObj reqObj = new AddressObj();
        reqObj.setPostcode("3168");
        reqObj.setState("VIC");
        reqObj.setSuburb("Clayton");
        reqObj.setLocation(new AddressObj.GeoJson(AddressObj.Type.Point, 149.095065, -35.201372));

        MultiValueMap<String, String> reqHeaders = new LinkedMultiValueMap<>();
        String invalidToken = "123";
        reqHeaders.add("Authorization", "Bearer " + invalidToken);
        reqHeaders.add("Content-Type", "application/json");
        HttpEntity<AddressObj> reqEntity = new HttpEntity<>(reqObj, reqHeaders);

        ResponseEntity<Response> resEntity = restTemplate
                .exchange("/admin/address", HttpMethod.POST, reqEntity, Response.class);
        assertThat(resEntity.getStatusCode().value(), is(401));
        assertThat(resEntity.getBody().getMessage(), containsString("Invalid token"));
    }

    @Test
    public void shouldFailSaveAddressWhenTokenNotProvided() {
        AddressObj reqObj = new AddressObj();
        reqObj.setPostcode("3168");
        reqObj.setState("VIC");
        reqObj.setSuburb("Clayton");
        reqObj.setLocation(new AddressObj.GeoJson(AddressObj.Type.Point, 149.095065, -35.201372));

        MultiValueMap<String, String> reqHeaders = new LinkedMultiValueMap<>();
        reqHeaders.add("Content-Type", "application/json");
        HttpEntity<AddressObj> reqEntity = new HttpEntity<>(reqObj, reqHeaders);

        ResponseEntity<Response> resEntity = restTemplate
                .exchange("/admin/address", HttpMethod.POST, reqEntity, Response.class);
        assertThat(resEntity.getStatusCode().value(), is(401));
        assertThat(resEntity.getBody().getMessage(), containsString("Missing or invalid Authorization header"));
    }


    @Test
    public void shouldLoginWhenCorrectCredentialsProvided() {
        LoginRequest loginReq = new LoginRequest("user", "password");
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange("/login", HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(200));
        assertNotNull(loginResEntity.getBody());
        assertNotNull(loginResEntity.getBody().getJwtToken());
    }

    @Test
    public void shouldFailLoginWhenIncorrectCredentialsProvided() {
        LoginRequest loginReq = new LoginRequest("user", "password1");
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange("/login", HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(401));
        assertNotNull(loginResEntity.getBody());
        assertThat(loginResEntity.getBody().getMessage(), is("Incorrect username or password"));
    }

    @Test
    public void shouldFailLoginWhenMandatoryFieldsNotProvided() {
        LoginRequest loginReq = new LoginRequest("user", null);
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange("/login", HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(400));
        assertNotNull(loginResEntity.getBody());
        assertThat(loginResEntity.getBody().getMessage(), is("Username and password are mandatory"));
    }

    @Test
    public void shouldFailLoginWhenUserNotFound() {
        LoginRequest loginReq = new LoginRequest("user123", "password");
        MultiValueMap<String, String> loginHeaders = new LinkedMultiValueMap<>();
        loginHeaders.add("Content-Type", "application/json");
        HttpEntity<LoginRequest> loginReqEntity = new HttpEntity<>(loginReq, loginHeaders);
        ResponseEntity<LoginResponse> loginResEntity = restTemplate.exchange("/login", HttpMethod.POST,
                loginReqEntity, LoginResponse.class);

        assertThat(loginResEntity.getStatusCode().value(), is(401));
        assertNotNull(loginResEntity.getBody());
        assertThat(loginResEntity.getBody().getMessage(), is("User not found"));
    }
}
