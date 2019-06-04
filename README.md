# AdvertBoard Backend

Backend of the AdvertBoard application

## Path prefix to all queries
```
/api
```

# Queries

## User

### Sign Up
```
path: /auth/signup
header: Content-Type: application/json
method: POST
request body: {"name":<USERNAME>, "email":<EMAIL>,"password":<PASSWORD>}
response: 201 Created
```

### Log in
```
path: /auth/login
header: Content-Type: application/json
method: POST
request body: {"email":<EMAIL>,"password":<PASSWORD>}
response: 200 OK
```

### Current user
```
path: /user/me
header: Authorization: Bearer <TOKEN>
method: GET
response: {"name":<NAME>,"email":<EMAIL>,"emailVerified":<EMAILVERIFIED>,"adverts":<ADVERTS>,"authProvider":<PROVIDER>,"profileView":<PROFILEVIEW>,"role":<ROLE>}
```

### Edit Profile
```
path: /user/me
header: Authorization: Bearer <TOKEN>
header: Content-Type: application/json
method: POST
request body: {"visibleName":<NAME>, "firstName":<NAME>, "lastName":<NAME>, "telephoneNumber":<NUMBER>, "contactMail":<MAIL>, "categoryEntries":<ENTRIES>}
response: 200 OK
```

### All users
```
path: /user/all
method: GET
request param: nameContains, page, limit, sort
response: list of user profiles
```

### Get user
```
path: /user/get
method: GET
request param: profileId
response: {"firstName":<FIRSTNAME>,"lastName":<LASTNAME>,"telephoneNumber":<NUMBER>,"contactMail":<MAIL>,"rating":<RATING>,"ratingCount":<COUNT>,"isVerified":<ISVERIFIED>,"advertSummaryViews":<VIEWS>}
```

### Rate user
```
path: /user/rate
header: Authorization: Bearer <TOKEN>
method: POST
request param: profileId, rating
response: 200 OK
```

### Refresh verification token
```
path: /user/refreshToken
method: POST
header: Authorization: Bearer <TOKEN>
response: 200 OK
```
### Report user
```
path: /user/report
header: Authorization: Bearer <TOKEN>
method: POST
request body: {"profileId":<ID>, "comment":<COMMENT>}
```



## Advert

### Add advert
```
path: /advert/add
header: Authorization: Bearer <TOKEN>; Content-Type: application/json
method: POST
request param: title, tags, description, imageFile, category, additionalInfo:{<INFO_ID>:<VALUE>, ...}}
response: 200 OK
```

### Edit advert
```
path: /advert/edit
header: Authorization: Bearer <TOKEN>; Content-Type: application/json
method: POST
request param: id, title, tags, description, imageFile, additionalInfo:{<INFO_ID>:<VALUE>, ...}}
response: 200 OK
```

### Remove advert
```
path: /advert/remove
header: Authorization: Bearer <TOKEN>
method: POST
request param: id
response: 200 Ok
```

### Get advert
```
path: /advert/get
method: GET
request param: id
response example: {
    "id": 4,
    "title": "Ferrari 488 Spider - Sprzedam",
    "pic": "",
    "categoryId": 6,
    "date": "2019-06-03",
    "recommended": false,
    "profileId": 9,
    "profileName": "admin",
    "description": "Po",
    "categoryName": "telefon",
    "tags": [
        "ferrari",
        "auto",
        "fajne"
    ],
    "additionalInfo": {
        "marka": "nowa",
        "cena": 500
    },
    "status": "OK",
    "entryCount": 1
}
```
### Get last user's advert
```
path: /advert/last
method: GET
request param: userId, limit
response: [
    {
        "id": <ID>,
        "title": <title>,
        "pic": <PIC>,
        "categoryId": <ID>,
        "date": <DATE>,
        "recommended": <RECOMMENDED>
    }, .....
]

```

### Browse adverts
```
path: /advert/browse
method: GET
request param: categoryId, sort, limit, page, titleContains
response example: {
    "content": [
        {
            "id": 4,
            "title": "Ferrari 488 Spider - Sprzedam",
            "pic": "",
            "categoryId": 6,
            "date": "2019-06-03",
            "recommended": false
        }
    ],
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "pageSize": 1,
        "pageNumber": 0,
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 4,
    "last": false,
    "totalElements": 4,
    "first": true,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
    "size": 1,
    "number": 0,
    "numberOfElements": 1,
    "empty": false
}

```

### Recommended adverts
```
path: /advert/recommended
header: Authorization: Bearer <TOKEN>
method: GET
response: [
    {
        "id": <ADVERT_ID>,
        "title": <TITLE>,
        "pic": <PIC>,
        "categoryId": <CAT_ID>,
        "date": <DATE>,
        "recommended": <RECOMMENDED>
    }, ......
]
```

### Report advert
```
path: /advert/report
header: Authorization: Bearer <TOKEN>
method: POST
request body: { "advertId":<ID>, "comment":<COMMENT> }
```


## Category

### Add category
```
path: /category/add
header: Authorization: Bearer <TOKEN>; Content-Type: application/json
method: POST
request body: {"categoryName":<NAME>, "description":<DESC>, "parentCategory":<PARENT_ID>, "infos": {<NAME>:<INFO_TYPE>, ...}}
response: 200 OK
```

### Remove category
```
path: /category/remove
header: Authorization: Bearer <TOKEN>
method: POST
request param: categoryName
response: 200 OK
```

### All categories
```
path: /category/all
method: GET
response: {
    "id": 0,
    "name": "root",
    "subcategories": [ ..... ]
    }
    "infoList": [
                                {
                                    "id": <ID>,
                                    "name": <NAME>,
                                    "type": <TYPE>
                                }, .... ]
```

## Admin

### Get all reports
```
path: /report/advert/all
method: get
header: Authorization: Bearer <TOKEN>
request param: limit, sort, page
```
