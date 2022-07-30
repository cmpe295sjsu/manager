# Manager Node
Manager node for the Distributed and Decentralized IoT Platform

## API Contract for Manager Node and Client

- Fetch the IFPS hash for a given device ID

`[PUT] /ifps-hash/{device_id}`

Request body:
```json
{
    "email": "<email>",
    "password": "<password>"
}
```

Response:
- For successfully authenticated user - 200 OK with IFPS hash
```json
{
    "ipfsHash": "<IFPS hash>"
}
```

- For unsuccessfully authenticated user - 401 Unauthorized

## API Contract for Manager Node and Web Portal

- Note: All requests will contain JWT in the Bearer token header

- Register user

`[POST] /users`

Request body:
```json
{
    "email": "<email>",
    "password": "<password>"
}
```

Response:

`200 OK` for successful registration
Response body:
```json
{
    "token": "<jwt>"
}
```

`400 Bad Request` for failed registration

- Sign in user

`[PUT] /users/signin`

Request body:
```json
{
    "email": "<email>",
    "password": "<password>"
}
```

Response:

`200 OK` for successful sign in
Response body:
```json
{
    "token": "<jwt>"
}
```

`401 Unauthorized` for failed sign in

- Register IoT device

`[POST] /devices`

Request body:
```json
{
    "id": "<id>",
    "name": "<name>",
    "region": "<region>"
}
```

Response:

`200 OK` for successful registration

`400 Bad Request` for failed registration

- Delete IoT device

`[DELETE] /devices/<id>`

Response:

`200 OK` for successful deletion

`400 Bad Request` for failed deletion

- Create access policy

`[POST] /policies`

Request body:
```json
{
    "device_id": "<device_id>",
    "accessing_device_id": "<accessing_device_id>"
}
```

Response:

`200 OK` for successful creation

`400 Bad Request` for failed creation

- Delete access policy

`[DELETE] /policies/device/<device-id>/accessing-device/<accessor-id>`

Response:

`200 OK` for successful deletion

`400 Bad Request` for failed deletion
