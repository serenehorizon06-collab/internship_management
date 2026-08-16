# Mandatory API Audit

## 1. Summary

Overall result: PARTIAL

Test result: PASS (`.\mvnw.cmd clean test`, 68 tests)

Audit date: 2026-08-17

Audited base commit: `ccfb5d0`

Audit scope: mandatory endpoints 1-27 only. APIs 28-44 are intentionally not implemented. Code audit and regression tests passed; overall is PARTIAL only because the real PostgreSQL smoke test was not verified in this environment.

Repository checks:

- Working tree before audit: the two mandatory audit docs were deleted locally; they were restored because this final audit requires them.
- Phase commits 01-09 are present in git log.
- Remote `origin` points to `https://github.com/serenehorizon06-collab/internship_management.git`.
- No tracked `.env`, `.env.*`, `target/`, generated token, real secret, or database password file was found.
- `application.yaml` uses environment variables for DB and JWT config; the committed JWT value is a local placeholder.
- Removed unused optional `spring-boot-devtools` dependency from `pom.xml`; no source behavior changed.

## 2. Endpoint Coverage Table

| STT | Method | Path | Required role from SRS | Implemented role | Status | Notes |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | `/api/auth/login` | PUBLIC | PUBLIC | PASS | Returns JWT login response via `ApiResponse`; inactive users cannot login. |
| 2 | GET | `/api/auth/me` | ADMIN, MENTOR, STUDENT | Authenticated user | PASS | Requires JWT and returns current user DTO. |
| 3 | GET | `/api/users` | ADMIN | ADMIN | PASS | Optional role filter supported; response DTO does not expose `passwordHash`. |
| 4 | GET | `/api/users/{user_id}` | ADMIN | ADMIN | PASS | Path implemented as `/api/users/{userId}`; same URL shape. |
| 5 | POST | `/api/users` | ADMIN | ADMIN | PASS | Password is BCrypt encoded before save. |
| 6 | PUT | `/api/users/{user_id}` | ADMIN | ADMIN | PASS | Updates basic user fields only. |
| 7 | PUT | `/api/users/{user_id}/status` | ADMIN | ADMIN | PASS | Updates `isActive`. |
| 8 | PUT | `/api/users/{user_id}/role` | ADMIN | ADMIN | PASS | Service prevents changing another ADMIN's role. |
| 9 | DELETE | `/api/users/{user_id}` | ADMIN | ADMIN | PASS | Deletes user through repository. |
| 10 | GET | `/api/students` | ADMIN, MENTOR | ADMIN, MENTOR | PASS | Mentor result is filtered by `internship_assignments`. |
| 11 | GET | `/api/students/{student_id}` | ADMIN, MENTOR, STUDENT | ADMIN, MENTOR, STUDENT | PASS | Student can view only own profile; mentor only assigned students. |
| 12 | POST | `/api/students` | ADMIN | ADMIN | PASS | Requires linked user role STUDENT. |
| 13 | PUT | `/api/students/{student_id}` | ADMIN, STUDENT | ADMIN, STUDENT | PASS | Student can update only own profile; mentor cannot update. |
| 14 | GET | `/api/mentors` | ADMIN, STUDENT | ADMIN, STUDENT | PASS | Mentor cannot list all mentors. |
| 15 | GET | `/api/mentors/{mentor_id}` | ADMIN, MENTOR, STUDENT | ADMIN, MENTOR, STUDENT | PASS | Mentor can view only own profile; student access allowed by SRS list. |
| 16 | POST | `/api/mentors` | ADMIN | ADMIN | PASS | Requires linked user role MENTOR. |
| 17 | PUT | `/api/mentors/{mentor_id}` | ADMIN, MENTOR | ADMIN, MENTOR | PASS | Mentor can update only own profile. |
| 18 | GET | `/api/internship_phases` | ADMIN, MENTOR, STUDENT | ADMIN, MENTOR, STUDENT | PASS | Returns DTO list. |
| 19 | GET | `/api/internship_phases/{phase_id}` | ADMIN, MENTOR, STUDENT | ADMIN, MENTOR, STUDENT | PASS | Returns DTO detail. |
| 20 | POST | `/api/internship_phases` | ADMIN | ADMIN | PASS | Checks unique phase name and date range. |
| 21 | PUT | `/api/internship_phases/{phase_id}` | ADMIN | ADMIN | PASS | Checks unique name excluding current row and date range. |
| 22 | DELETE | `/api/internship_phases/{phase_id}` | ADMIN | ADMIN | PASS | Deletes phase; DB cascade/constraints remain schema-controlled. |
| 23 | GET | `/api/evaluation_criteria` | ADMIN, MENTOR, STUDENT | ADMIN, MENTOR, STUDENT | PASS | Returns DTO list. |
| 24 | GET | `/api/evaluation_criteria/{criterion_id}` | ADMIN, MENTOR, STUDENT | ADMIN, MENTOR, STUDENT | PASS | Returns DTO detail. |
| 25 | POST | `/api/evaluation_criteria` | ADMIN | ADMIN | PASS | Checks unique criterion name and `maxScore > 0`. |
| 26 | PUT | `/api/evaluation_criteria/{criterion_id}` | ADMIN | ADMIN | PASS | Checks unique name excluding current row and `maxScore > 0`. |
| 27 | DELETE | `/api/evaluation_criteria/{criterion_id}` | ADMIN | ADMIN | PASS | Deletes criterion; FK behavior remains schema-controlled. |

## 3. Security/RBAC Findings

- PASS: `POST /api/auth/login` is public.
- PASS: `GET /api/auth/me` requires authentication.
- PASS: JWT is read from `Authorization: Bearer <token>`.
- PASS: BCrypt is configured through `PasswordEncoder`.
- PASS: Inactive users are rejected during login.
- PASS: Missing, invalid, and expired JWT responses are JSON `ErrorResponse`, not HTML.
- PASS: `/api/users/**` is ADMIN only.
- PASS: ADMIN cannot change the role of another ADMIN.
- PASS: `passwordHash` is not exposed in response DTOs.
- PASS: Student and mentor profile ownership restrictions are enforced in service layer.
- PASS: Internship Phase and Evaluation Criteria write operations are ADMIN only.

## 4. Response Format Findings

- PASS: Success responses use `ApiResponse`.
- PASS: Error responses use `ErrorResponse`.
- PASS: JSON uses `status_code`, not `statusCode`.
- PASS: JSON uses `error_code`, not `errorCode`.
- PASS: Validation errors return `INVALID_INPUT_DATA`.

Required error codes:

| Error code | Status |
| --- | --- |
| `INVALID_INPUT_DATA` | PASS |
| `DUPLICATE_RESOURCE` | PASS |
| `EXPIRED_JWT_TOKEN` | PASS |
| `INVALID_JWT_TOKEN` | PASS |
| `BAD_CREDENTIALS` | PASS |
| `ACCESS_DENIED` | PASS |
| `RESOURCE_NOT_FOUND` | PASS |
| `INVALID_ASSIGNMENT_STATE` | PASS, defined for later assignment rules; not exercised by mandatory endpoints 1-27. |
| `INTERNAL_SERVER_ERROR` | PASS |

## 5. Database Mapping Findings

- PASS: Entities exist for the 9 PostgreSQL schema tables: `users`, `students`, `mentors`, `internship_phases`, `evaluation_criteria`, `assessment_rounds`, `round_criteria`, `internship_assignments`, `assessment_results`.
- PASS: Table and column names are mapped with `@Table` and `@Column`.
- PASS: `DATE` fields use `LocalDate`.
- PASS: `TIMESTAMP` fields use `LocalDateTime`.
- PASS: `DECIMAL` fields use `BigDecimal`.
- PASS: `UserRole` and `AssignmentStatus` enums are mapped as strings.
- PASS: `student_id` and `mentor_id` are not auto-generated.
- PASS: `password_hash` is mapped only on `User` and is not exposed by response DTOs.
- PASS: `spring.jpa.hibernate.ddl-auto` defaults to `validate`, not `create` or `create-drop`.

## 6. Validation Findings

- PASS: Users handle duplicate `username` and `email`.
- PASS: Students handle duplicate `studentCode`.
- PASS: Internship Phases handle duplicate `phaseName` and validate `startDate <= endDate`.
- PASS: Evaluation Criteria handle duplicate `criterionName` and validate `maxScore > 0`.
- PASS: Student profile creation requires a linked user with role STUDENT.
- PASS: Mentor profile creation requires a linked user with role MENTOR.
- PASS: Jakarta Validation errors are handled as `INVALID_INPUT_DATA`.

## 7. Out-of-Scope API Check

PASS: No controller mappings were found for:

- `/api/assessment_rounds`
- `/api/round_criteria`
- `/api/internship_assignments`
- `/api/assessment_results`

Entities and repositories for these tables exist because the database schema has 9 tables, but endpoints 28-44 are intentionally not implemented in this mandatory scope.

## 8. Known Limitations

- APIs 28-44 are intentionally not implemented because they are outside the approved mandatory scope.
- Manual DB smoke test status: NOT VERIFIED.
- Reason: this environment does not expose a complete DB/JWT runtime configuration through `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`. `DB_PASSWORD` is set, but `DB_URL`, `DB_USERNAME`, and `JWT_SECRET` are not set. No password or secret was hardcoded for this audit.
- Delete behavior for `internship_phases` and `evaluation_criteria` follows the existing PostgreSQL FK cascade/constraint definitions.

## 9. Manual Test Commands

Set local environment variables first:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/internship_management"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="<your local password>"
$env:JWT_SECRET="<your local long jwt secret>"
$env:JPA_DDL_AUTO="validate"
.\mvnw.cmd spring-boot:run
```

Login as ADMIN:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

Use the returned token:

```bash
TOKEN="<paste token here>"
```

Auth/me:

```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

Users:

```bash
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"

curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"student01\",\"password\":\"student123\",\"fullName\":\"Student One\",\"email\":\"student01@example.com\",\"phoneNumber\":\"0900000001\",\"role\":\"STUDENT\",\"isActive\":true}"
```

Students:

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"studentId\":2,\"studentCode\":\"SV001\",\"major\":\"IT\",\"className\":\"IT01\",\"dateOfBirth\":\"2004-01-01\",\"address\":\"Hanoi\"}"

curl http://localhost:8080/api/students \
  -H "Authorization: Bearer $TOKEN"
```

Mentors:

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"mentor01\",\"password\":\"mentor123\",\"fullName\":\"Mentor One\",\"email\":\"mentor01@example.com\",\"phoneNumber\":\"0900000002\",\"role\":\"MENTOR\",\"isActive\":true}"

curl -X POST http://localhost:8080/api/mentors \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"mentorId\":3,\"department\":\"Software Engineering\",\"academicRank\":\"Lecturer\"}"

curl http://localhost:8080/api/mentors \
  -H "Authorization: Bearer $TOKEN"
```

Internship Phases:

```bash
curl -X POST http://localhost:8080/api/internship_phases \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"phaseName\":\"Internship Phase 1\",\"startDate\":\"2026-08-01\",\"endDate\":\"2026-12-01\",\"description\":\"Mandatory internship phase\"}"

curl http://localhost:8080/api/internship_phases \
  -H "Authorization: Bearer $TOKEN"
```

Evaluation Criteria:

```bash
curl -X POST http://localhost:8080/api/evaluation_criteria \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"criterionName\":\"Work attitude\",\"description\":\"Evaluate responsibility and initiative\",\"maxScore\":10.00}"

curl http://localhost:8080/api/evaluation_criteria \
  -H "Authorization: Bearer $TOKEN"
```

RBAC negative checks:

```bash
# Login as STUDENT or MENTOR, then verify this is forbidden:
curl -X POST http://localhost:8080/api/evaluation_criteria \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"criterionName\":\"Should fail\",\"description\":\"Non-admin write\",\"maxScore\":10.00}"

# STUDENT must not list all students:
curl http://localhost:8080/api/students \
  -H "Authorization: Bearer $TOKEN"

# MENTOR must not list all mentors:
curl http://localhost:8080/api/mentors \
  -H "Authorization: Bearer $TOKEN"
```
