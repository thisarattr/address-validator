[![Build Status](https://travis-ci.org/thisarattr/address-validator.svg?branch=master)](https://travis-ci.org/thisarattr/address-validator)

# Address Validator
This sample application provide simple apis to access (or search) address information via couple of apis and a secure api to save address information as per user provides. Because of the secure api, one more api added to autheticate the user and return token for secure api.

## Libraries
Application is done basically using, spring boot framework, gradle as build tool and h2 as database.
* Spring boot 1.5.4
* Hibernate 5.0.12
* jjwt 0.7.0
* h2database
* httpclient 4.5.3

## APIs
On application start up h2 inmemory database start up and data.sql script will insert couple of users and few address records so that it can be tested.
### Login
This api will validate the user crediatial with database and if success create a jwt. Then the user object will be added into a cache along with the jwt key with a configured expiration time. Finally jwt token is returned to user for the use of subsequent secure api call.
There is not a user registration/sign up api, thus initially added users will have to use for testing purposes.

Existing uses are:
1. Username/Password: user/password
2. Username/Password: admin/password

POST /api/v1/login

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
401 Unauthorized - Crediatial not valid
400 Bad Request - Mandatory fields are not provided (usename/passowrd)
```

