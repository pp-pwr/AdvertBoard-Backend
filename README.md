# AdvertBoard Backend

Backend of the application

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
```

### Log in
```
path: /auth/login
header: Content-Type: application/json
method: POST
request body: {"email":<EMAIL>,"password":<PASSWORD>}
```

### Current user
```
path: /user/me
header: Authorization: Bearer <TOKEN>
method: GET
```

### Edit Profile
```
path: /user/me
header: Authorization: Bearer <TOKEN>
header: Content-Type: application/json
method: POST
request body: {"visibleName":<NAME>, "firstName":<NAME>, "lastName":<NAME>, "telephoneNumber":<NUMBER>, "contactMail":<MAIL>}
```

### All users
```
path: /user/all
method: GET
request param: nameContains, page, limit, sort
```

### Get user
```
path: /user/get
method: GET
request param: profileId
```

## Advert

### Add advert
```
path: /advert/add
header: Authorization: Bearer <TOKEN>; Content-Type: application/json
method: POST
request body: {"title":<TITLE>, "tags":<TAGS>, "description":<DESC>, "image": {"base64":<B64>, "name":<NAME>}, category:<CAT_ID>, "additionalInfo":{<INFO_ID>:<VALUE>, ...}}
```

### Edit advert
```
path: /advert/edit
header: Authorization: Bearer <TOKEN>; Content-Type: application/json
method: POST
request body: {"id":<ID>, "title":<TITLE>, "tags":<TAGS>, "description":<DESC>, "image": {"base64":<B64>, "name":<NAME>}, "additionalInfo":{<INFO_ID>:<VALUE>, ...}}
```

### Remove advert
```
path: /advert/remove
header: Authorization: Bearer <TOKEN>
method: POST
request param: id
```

### All adverts
```
path: /advert/all
method: GET
request param: page, limit, sort
```

### Get advert
```
path: /advert/get
method: GET
request param: id
```

## Category

### Add category
```
path: /category/add
header: Authorization: Bearer <TOKEN>; Content-Type: application/json
method: POST
request body: {"categoryName":<NAME>, "description":<DESC>, "parentCategory":<PARENT_ID>, "infos": {<NAME>:<INFO_TYPE>, ...}}
```

### Remove category
```
path: /category/remove
header: Authorization: Bearer <TOKEN>
method: POST
request param: categoryName
```

### All categories
```
path: /category/all
method: GET
```

### Get category adverts
```
path: /category/get
method: GET
request param: categoryName, page, limit, sort, titleContains
```

