# Security Setup & Seeding Guide

This guide explains how to configure and use authentication and authorization in PulseUp, then seed the application end to end with the IntelliJ HTTP Client files in `Seeding`.

The backend runs at `http://localhost:8080`. The current `application.properties` does not set `server.port`, so Spring Boot uses its default port, `8080`.

## 1. Security configuration

The JWT settings in `src/main/resources/application.properties` are:

```properties
security.jwt.secret=${PULSEUP_JWT_SECRET}
security.jwt.issuer=pulseup-api
security.jwt.expiration-seconds=3600
```

`PULSEUP_JWT_SECRET` is an environment variable. The signing secret is supplied at runtime instead of being stored in source control. Because this property has no default value, the application cannot create its JWT encoder and decoder when the variable is missing or invalid.

The issuer is exactly `pulseup-api`. A token with a different issuer fails validation. Tokens expire after `3600` seconds (one hour).

## 2. Why the JWT secret is required

PulseUp signs tokens with HMAC-SHA256 (`HS256`). This is a symmetric algorithm: the same secret signs tokens during login and verifies Bearer tokens on later requests.

`JwtConfig.createSecretKey(...)` applies these startup rules:

| Rule | Reason |
| --- | --- |
| The environment value must be valid standard Base64. | `Base64.getDecoder()` converts it into key bytes. |
| The decoded value must contain at least 32 bytes. | HS256 requires a key at least as large as its 256-bit output. |

A plain password such as `mysecret` is not suitable. Generate 48 cryptographically random bytes and Base64-encode them. This produces a 64-character Base64 value before optional padding and exceeds the minimum requirement.

## 3. Generate and set the secret

Generate a new secret for each environment. Do not reuse documentation output, share a real secret, or commit one to Git.

### Windows PowerShell

Run these commands in the same PowerShell window that will start the backend:

```powershell
$bytes = New-Object 'System.Byte[]' 48
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
try {
    $rng.GetBytes($bytes)
} finally {
    $rng.Dispose()
}
$env:PULSEUP_JWT_SECRET = [System.Convert]::ToBase64String($bytes)
$env:PULSEUP_JWT_SECRET
```

Then start the backend from IntelliJ by running `PulseUp2026Application`.

> The checked-in Maven wrapper script cannot currently run because `.mvn/wrapper/maven-wrapper.properties` is missing. After restoring the Maven wrapper files or installing Maven, you can instead run `mvn spring-boot:run` from the `PulseUp2026` directory.

## 4. How JWT login and Bearer authentication work

Sign in with an existing user by sending JSON to the login endpoint:

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
Accept: application/json

{
  "email": "admin@cput.ac.za",
  "password": "{{adminPassword}}"
}
```

A successful response contains a token:

```json
{
  "userId": 1,
  "firstName": "PulseUp",
  "lastName": "Administrator",
  "email": "admin@cput.ac.za",
  "role": "ADMIN",
  "token": "<jwt-value>",
  "tokenType": "Bearer"
}
```

A JWT contains the signed issuer, user email, issue time, expiry time, `userId`, and roles. The token is signed by the server, so a client cannot safely change its role or expiry.

Use the token on protected endpoints with the `Authorization` header:

```http
Authorization: Bearer {{adminToken}}
```

`Bearer` means that the caller who possesses the token may use it. The API verifies the signature, issuer, expiry, and roles before allowing the request. Treat a bearer token like a password: anyone who obtains a valid token can act as that user until it expires.

Sign in again when a protected request returns `401 Unauthorized`; the supplied JWT has expired or is invalid.

## 5. IntelliJ HTTP Client environment setup

The seed requests use IntelliJ HTTP Client variables, including `{{baseUrl}}`, `{{adminPassword}}`, and the captured `{{adminToken}}`.

1. Copy `http-client.env.json.example` in the `PulseUp2026` project root to `http-client.private.env.json` in the same directory.
2. Replace every `REPLACE_WITH_...` value with local development values. Administrator and staff passwords must be at least eight characters.
3. Open `Seeding/seed-pulseup.http`.
4. Select the `local` environment in IntelliJ's HTTP Client environment selector.
5. Start the backend, then use the green run icon beside each request.

Example `http-client.private.env.json` file:

```json
{
  "local": {
    "baseUrl": "http://localhost:8080",
    "adminPassword": "your-local-admin-password",
    "staffPassword": "your-local-staff-password",
    "studentEmail": "a-local-student@example.test",
    "studentPassword": "the-student-local-password",
    "appointmentId": "1"
  }
}
```

The private environment file is ignored by Git. Do not put real passwords or JWTs in `http-client.env.json.example`, a committed `.http` file, or Git history.

The login and bootstrap requests automatically store a fresh token in IntelliJ's global `adminToken` variable:

```http
> {% client.global.set("adminToken", response.body.token); %}
```

All later protected requests use that variable with `Authorization: Bearer {{adminToken}}`; no copied JWT is needed.

## 6. End-to-end seed procedure

The following resources are in `Seeding`:

| File | Purpose |
| --- | --- |
| `pulseup-seed-catalog.json` | Readable JSON source catalog for the administrator, staff members, and slots. |
| `seed-pulseup.http` | Complete IntelliJ runner that registers the seed users and creates slots. |
| `security-test.http` | Login and protected-route security smoke checks. |
| `confirm-thulani.http` | Student queue and administrator appointment-status examples. |

Run `seed-pulseup.http` in this order:

1. **Step 1 — Bootstrap administrator:** run only when the `admins` table is empty. `POST /api/auth/bootstrap-admin` creates the first administrator and captures `adminToken`.
2. **Step 2 — Existing administrator login:** use this instead of step 1 after the administrator already exists. It signs in and refreshes `adminToken`.
3. **Steps 3–7 — Register staff:** create the five staff members. Each response captures its generated user ID, such as `generalConsultationsStaffId`.
4. **Steps 8–12 — Create slots:** create one slot for each supported service. Slot requests use clean JSON bodies and generated staff IDs, rather than fixed database IDs.
5. **Steps 13–15 — Verify:** list seeded administrators, staff members, and time slots with the admin bearer token.

Account registration is intentionally not idempotent. Duplicate email addresses, staff numbers, or admin numbers return `400 Bad Request`. To seed again, use a clean development database or change the identifiers. The API does not currently reject overlapping or duplicate slots, so do not repeat slot steps against the same database unless duplicates are intended.

## 7. Time-slot JSON format

`POST /api/time-slots` accepts this JSON structure:

```json
{
  "staffId": 2,
  "slotDate": "2026-07-23",
  "startTime": "09:00",
  "endTime": "09:20",
  "appointmentType": "General Consultations",
  "roomNumber": "Unit A1",
  "estimatedDurationMinutes": 20
}
```

`slotDate` uses `YYYY-MM-DD`; `startTime` and `endTime` use ISO local time such as `HH:mm`. The selected staff member must exist and have a department compatible with the appointment type. `estimatedDurationMinutes` must be between 1 and 180, and `startTime` must be before `endTime`.

The previous parameter-based `POST /api/time-slots` contract remains available for existing clients, but the seed runner uses JSON.

## 8. Valid staff and service combinations

| Appointment type / department | Valid example position | Seed room | Duration |
| --- | --- | --- | --- |
| General Consultations | General Practitioner | Unit A1 | 20 minutes |
| Reproductive Health | Reproductive Health Nurse | Unit B1 | 30 minutes |
| HIV VCT | HIV Counsellor | Unit C1 | 30 minutes |
| TB DOTS | TB Nurse | Unit D1 | 15 minutes |
| Wound Dressings | Wound Care Nurse | Unit E1 | 30 minutes |

The staff department must match the time slot's appointment type. The application rejects invalid position/department combinations, invalid time ranges, unknown staff IDs, and invalid appointment types.
