package org.thisarattr.auspost.addressvalidator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.core.Is.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.thisarattr.auspost.addressvalidator.dtos.JsonAddress;

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
        JsonAddress[] body = restTemplate.withBasicAuth("user", "password")
                .getForObject("/address/postcode/200", JsonAddress[].class);
        assertThat(body, arrayWithSize(1));
        assertThat(body[0].getSuburb(), is("AUSTRALIAN NATIONAL UNIVERSITY"));
        assertThat(body[0].getPostcode(), is("200"));
        assertThat(body[0].getState(), is("ACT"));
        assertThat(body[0].getLocation().getCoordinates()[0], is(149.117136));
        assertThat(body[0].getLocation().getCoordinates()[1], is(-35.277272));
    }

    @Test
    public void shouldReturnAddressArrayOrderByPostcodeWhenSearchByPostcode() {
        JsonAddress[] body = restTemplate.withBasicAuth("user", "password")
                .getForObject("/address/postcode/2", JsonAddress[].class);
        assertThat(body, arrayWithSize(2));
        assertThat(body[0].getSuburb(), is("AUSTRALIAN NATIONAL UNIVERSITY"));
        assertThat(body[0].getPostcode(), is("200"));
        assertThat(body[0].getState(), is("ACT"));
        assertThat(body[1].getSuburb(), is("BARTON"));
        assertThat(body[1].getPostcode(), is("221"));
        assertThat(body[1].getState(), is("ACT"));
    }

    @Test
    public void shouldReturnSingleObjectAddressArrayWhenSearchBySuburbForExactMatch() {
        JsonAddress[] body = restTemplate.withBasicAuth("user", "password")
                .getForObject("/address/suburb/barton", JsonAddress[].class);
        assertThat(body, arrayWithSize(1));
        assertThat(body[0].getSuburb(), is("BARTON"));
        assertThat(body[0].getPostcode(), is("221"));
        assertThat(body[0].getState(), is("ACT"));
        assertThat(body[0].getLocation().getCoordinates()[0], is(149.095065));
        assertThat(body[0].getLocation().getCoordinates()[1], is(-35.201372));
    }

    @Test
    public void shouldReturnAddressArrayOrderBySuburbWhenSearchBySuburb() {
        JsonAddress[] body = restTemplate.withBasicAuth("user", "password")
                .getForObject("/address/suburb/da", JsonAddress[].class);
        assertThat(body, arrayWithSize(2));
        assertThat(body[0].getSuburb(), is("DALY RIVER"));
        assertThat(body[0].getPostcode(), is("822"));
        assertThat(body[0].getState(), is("NT"));
        assertThat(body[1].getSuburb(), is("DARWIN"));
        assertThat(body[1].getPostcode(), is("800"));
        assertThat(body[1].getState(), is("NT"));
    }
}
