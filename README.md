# url-shortener-example

The task: REST service implement to shorten links. The service should have to 2 methods for urls.

<b>POST</b> - when we shortener url
<br> <b>GET</b> - when returned full url

Tech stack: Spring Boot, Redis, Maven

How it start: 
1. Go to https://redis.io/ and install stable version redis
2. Download and open project in IDEA, run the application next.

How it use in the postman:

<b>POST-request</b>
<br>URL: http://localhost:8888/
<br>BODY - "x-www-form-urlencoded"
<br>KEY: "url"
<br>VALUE: any url (for ex: "https://github.com/ValentinaErmilova/url-shortener-example/new/master?readme=1")
<br><i>curl-variant</i>
<br>curl --request POST 'http://localhost:8888/' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'url=https://github.com/ValentinaErmilova/url-shortener-example'

<b>GET-request</b>
<br>URL: http://localhost:8888/ + ID (after used post request, for ex: 202d17f7)
<br><i>curl-variant</i>
<br>curl --location --request GET 'http://localhost:8888/202d17f7'
