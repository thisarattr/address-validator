[![Build Status](https://travis-ci.org/thisarattr/address-validator.svg?branch=master)](https://travis-ci.org/thisarattr/address-validator)

# Address Validator
This sample application provide simple apis to access (or search) address information via couple of apis and a secure api to save address information as per user provides. Because of the secure api, one more api added to autheticate the user and return token for secure api.

Little bit background on the application,
1. Security: Login api will check the database for user credentials and if its valid then generate a jwt token with the configured jwt secret. Secret is configure in the property file but it can be read from environment for security reason. Then this jwt token will be returned back to user.
Registration bean has used to forward all the secure api starting with `/api/v1/admin/*` into a `JwtTokenFilter` which validate the token and check the cache to see if its not expired. If not you will get access to the api and else `401 unauthoried` eror is raised. 
2. CacheService: This is used to cache the user tokens with expire time so that use can use same token intill it expire for multiple requests. As per current configuration cache expire in 60 mins.
3. Google checkstle is used with minor changes
4. Integration test cover all the functionalities and since there are minimum logics involved, very minimum unit tests used. Overall test coverage is about 80-90%. Check the jacoco reports.

## Libraries
Application is done basically using, spring boot framework, gradle as build tool and h2 as database.
* Spring boot 1.5.4
* Hibernate 5.0.12
* jjwt 0.7.0
* h2database
* httpclient 4.5.3

## APIs
On application start up h2 inmemory database start up and data.sql script will insert couple of users and few address records so that it can be tested.
### 1. Login
This api will validate the user crediatial with database and if success create a jwt. Then the user object will be added into a cache along with the jwt key with a configured expiration time. Finally jwt token is returned to user for the use of subsequent secure api call.
There is not a user registration/sign up api, thus initially added users will have to use for testing purposes.

Existing uses are:
1. Username/Password: user/password
2. Username/Password: admin/password

POST `/api/v1/login`

Header:
```
Content-Type: application/json
```
Payload:
```
{
	"username": "user",
 	"password": "password"
}
```
Reponse:
```
{
    "status": 200,
    "message": "Successfully completed.",
    "jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6InVzZXIiLCJpYXQiOjE0OTk2MDYwMjZ9.65jviaKdgPhhY4ysBzzT-3pUKJq30M63aIe9lBceVLc",
    "success": true
}
200 OK
400 Bad Request - Mandatory fields are not provided (usename/passowrd)
401 Unauthorized - Crediatial not valid
500 Internal Server Error
```
### 2. Find by postcode
This api is not a secure api and it returns list of addresses found for the provided postcode. This is a case insensitive search and return list is ordered by postcode. Postcode is string field, considering some contries are using both lettes and digits as postcode.

GET `/api/v1/address/postcode/{postcode}`

Reponse:
```
{
    "status": 200,
    "message": "Successfully completed.",
    "success": true,
    "addresses": [
        {
            "id": 1,
            "postcode": "200",
            "suburb": "AUSTRALIAN NATIONAL UNIVERSITY",
            "state": "ACT",
            "location": {
                "type": "Point",
                "coordinates": [
                    149.117136,
                    -35.277272
                ]
            }
        }
    ]
}
200 OK
500 Internal Server Error
```
### 3. Find by Suburb
This is almost same as above find by postcode api. Only difference is that this case insensitive search for suburb and return list is order by suburb.

GET `/api/v1/address/suburb/{suburb}`

Reponse:
```
{
    "status": 200,
    "message": "Successfully completed.",
    "success": true,
    "addresses": [
        {
            "id": 1,
            "postcode": "200",
            "suburb": "AUSTRALIAN NATIONAL UNIVERSITY",
            "state": "ACT",
            "location": {
                "type": "Point",
                "coordinates": [
                    149.117136,
                    -35.277272
                ]
            }
        }
    ]
}
200 OK
500 Internal Server Error
```
### 4. Save address
This is the only secure api and user need a valid, non expired jwt token to access this api. Thus, user first need to use login api to get his jtw token (access token) and use that token in authorization header of this request. Postcode, suburb and state fields are mandatory fields and location is optional. Location is following the `GeoGson` format so coordinates are in [logitude, latitude] fromat.

POST `/api/v1/admin/address`

Header:
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdmxhZGFAZ21haWwuY29tIiwi
```
Payload:
```
{
"postcode": "3168",
"suburb": "Clayton",
"state" : "VIC",
"location": {
	"coordinates": [
	    149.117136,
	    -35.277272
	]
    }
}
```

Reponse:
```
{
    "status": 200,
    "message": "Successfully completed.",
    "success": true
}
200 OK
400 Bad Request - Mandatory fields are not provided (postcode, suburb and state)
401 Unauthorized - token is not valid or expired
500 Internal Server Error
```

## Build and run
Gradle is used as build tool and [travis](https://travis-ci.org/thisarattr/address-validator) has been configured to do continuous integration. 

**Build**

`./gradlew clean build`

**Run**

Artificats can be located at `$buildDir/lib/address-validator-1.0.jar`

`java -jar pathToJar/address-validator-1.0.jar`
