# ChatterTap - Demo app for WireMock workshop

ChatterTap is a simple Java web app to display tweets for a geographical location 
using the following to HTTP APIs:
* geonames.org — free geographical search
* api.twitter.com — lookup tweets tagged with latitude/longitude

Its main purpose is to serve as an example app for practicing use of WireMock.
 
The application expects the following login credentials as system environment variables:
* geonames.username
* twitter.key
* twitter.secret

Login credentials can be obtained by registering a user at http://www.geonames.org and https://apps.twitter.com.
