# Mandatory API Test Cases

## Summary

This document covers the mandatory API scope, endpoints 1-27 only.

Automated tests use MockMvc controller tests with mocked services plus existing service/serialization tests. They do not require a real PostgreSQL connection and do not use real JWT secrets, generated tokens, or database passwords.

Latest automated result: PASS (`.\mvnw.cmd clean test`, 68 tests, 0 failures).

DB smoke test status: NOT VERIFIED.

Run all tests:

```powershell
.\mvnw.cmd clean test
```

## Coverage Matrix

| STT | Module | Method | Path | Allowed roles | Main success test | Important negative/security tests | Expected success status | Expected error status | Automated test class/method | Status | Notes |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | Auth | POST | `/api/auth/login` | PUBLIC | Valid username/password returns token and `ApiResponse` | Bad credentials 401; missing fields 400 | 200 | 400, 401 | `AuthControllerTests.login_shouldReturnToken_whenCredentialsAreValid` | AUTOMATED | Bad credentials and validation are automated too. |
| 2 | Auth | GET | `/api/auth/me` | ADMIN, MENTOR, STUDENT | Authenticated request returns current user DTO | Unauthenticated request returns 401 | 200 | 401 | `AuthControllerTests.getCurrentUser_shouldReturnCurrentUser_whenAuthenticated` | AUTOMATED | 401 is documented; JWT entry point format covered by existing security code/tests. |
| 3 | Users | GET | `/api/users` | ADMIN | Returns user list in `ApiResponse` | Role filter; non-ADMIN 403; unauthenticated 401 | 200 | 401, 403 | `UserControllerTests.getUsers_shouldReturnUserList` | AUTOMATED | Role filter automated by `getUsers_shouldPassRoleFilterToService`. |
| 4 | Users | GET | `/api/users/{user_id}` | ADMIN | Returns user detail DTO | Missing user 404; password not exposed | 200 | 404 | `UserControllerTests.getUserById_shouldReturnUserDetail` | AUTOMATED | Missing user automated. |
| 5 | Users | POST | `/api/users` | ADMIN | Creates user and returns 201 | Invalid request 400; duplicate username/email 400; password not returned | 201 | 400, 403 | `UserControllerTests.createUser_shouldReturnCreatedUserWithoutPassword` | AUTOMATED | Duplicate rules covered by service tests. |
| 6 | Users | PUT | `/api/users/{user_id}` | ADMIN | Updates basic user information | Missing user 404; invalid request 400 | 200 | 400, 404 | `UserControllerTests.updateUser_shouldReturnUpdatedUser` | AUTOMATED | Service tests cover duplicate username/email on update. |
| 7 | Users | PUT | `/api/users/{user_id}/status` | ADMIN | Updates `isActive` | Non-ADMIN 403; invalid request 400 | 200 | 400, 403 | `UserControllerTests.updateUserStatus_shouldReturnUpdatedStatus` | AUTOMATED | RBAC negative is documented. |
| 8 | Users | PUT | `/api/users/{user_id}/role` | ADMIN | Updates role | Cannot change another ADMIN role; non-ADMIN 403 | 200 | 403 | `UserControllerTests.updateUserRole_shouldReturnUpdatedRole` | AUTOMATED | Forbidden rule automated by `updateUserRole_shouldReturnAccessDenied_whenChangingAnotherAdminRole`. |
| 9 | Users | DELETE | `/api/users/{user_id}` | ADMIN | Deletes user and returns null data | Missing user 404; non-ADMIN 403 | 200 | 403, 404 | `UserControllerTests.deleteUser_shouldReturnSuccessWithNullData` | AUTOMATED | Missing user/security cases documented. |
| 10 | Students | GET | `/api/students` | ADMIN, MENTOR | Returns student list | STUDENT 403; unauthenticated 401; mentor only assigned students | 200 | 401, 403 | `StudentControllerTests.getStudents_shouldReturnStudents` | AUTOMATED | Mentor filtering is covered by `StudentServiceImplTests`. |
| 11 | Students | GET | `/api/students/{student_id}` | ADMIN, MENTOR, STUDENT | Returns student detail | Missing student 404; student cannot view another student 403; mentor cannot view unassigned student 403 | 200 | 403, 404 | `StudentControllerTests.getStudentById_shouldReturnStudentDetail` | AUTOMATED | Missing student automated; ownership rules covered by service tests/docs. |
| 12 | Students | POST | `/api/students` | ADMIN | Creates student profile | Linked user must be STUDENT; duplicate code 400; non-ADMIN 403 | 201 | 400, 403 | `StudentControllerTests.createStudent_shouldReturnCreatedStudent` | AUTOMATED | Role and duplicate rules covered by service tests. |
| 13 | Students | PUT | `/api/students/{student_id}` | ADMIN, STUDENT | Updates student profile | Student cannot update another student; mentor 403; invalid request 400 | 200 | 400, 403 | `StudentControllerTests.updateStudent_shouldReturnUpdatedStudent` | AUTOMATED | Ownership rules covered by service tests/docs. |
| 14 | Mentors | GET | `/api/mentors` | ADMIN, STUDENT | Returns mentor list | MENTOR 403; unauthenticated 401 | 200 | 401, 403 | `MentorControllerTests.getMentors_shouldReturnMentors` | AUTOMATED | RBAC negative documented. |
| 15 | Mentors | GET | `/api/mentors/{mentor_id}` | ADMIN, MENTOR, STUDENT | Returns mentor detail | Missing mentor 404; mentor cannot view another mentor 403 | 200 | 403, 404 | `MentorControllerTests.getMentorById_shouldReturnMentorDetail` | AUTOMATED | Missing mentor automated; ownership rule covered by service tests/docs. |
| 16 | Mentors | POST | `/api/mentors` | ADMIN | Creates mentor profile | Linked user must be MENTOR; duplicate profile 400; non-ADMIN 403 | 201 | 400, 403 | `MentorControllerTests.createMentor_shouldReturnCreatedMentor` | AUTOMATED | Role and duplicate rules covered by service tests. |
| 17 | Mentors | PUT | `/api/mentors/{mentor_id}` | ADMIN, MENTOR | Updates mentor profile | Mentor cannot update another mentor; STUDENT 403 | 200 | 403 | `MentorControllerTests.updateMentor_shouldReturnUpdatedMentor` | AUTOMATED | Ownership rules covered by service tests/docs. |
| 18 | Internship Phases | GET | `/api/internship_phases` | ADMIN, MENTOR, STUDENT | Returns phase list | Unauthenticated 401 | 200 | 401 | `InternshipPhaseControllerTests.getPhases_shouldReturnPhaseList` | AUTOMATED | All three roles are allowed by security config. |
| 19 | Internship Phases | GET | `/api/internship_phases/{phase_id}` | ADMIN, MENTOR, STUDENT | Returns phase detail | Missing phase 404 | 200 | 404 | `InternshipPhaseControllerTests.getPhaseById_shouldReturnPhaseDetail` | AUTOMATED | Missing phase automated. |
| 20 | Internship Phases | POST | `/api/internship_phases` | ADMIN | Creates phase | MENTOR/STUDENT 403; duplicate name 400; invalid date range 400 | 201 | 400, 403 | `InternshipPhaseControllerTests.createPhase_shouldReturnCreatedPhase` | AUTOMATED | Invalid date range automated; duplicate covered by service tests. |
| 21 | Internship Phases | PUT | `/api/internship_phases/{phase_id}` | ADMIN | Updates phase | MENTOR/STUDENT 403; duplicate name excluding current 400; invalid date range 400 | 200 | 400, 403 | `InternshipPhaseControllerTests.updatePhase_shouldReturnUpdatedPhase` | AUTOMATED | Duplicate/date rules covered by service tests. |
| 22 | Internship Phases | DELETE | `/api/internship_phases/{phase_id}` | ADMIN | Deletes phase and returns null data | MENTOR/STUDENT 403; missing phase 404 | 200 | 403, 404 | `InternshipPhaseControllerTests.deletePhase_shouldReturnSuccessWithNullData` | AUTOMATED | FK behavior remains database-controlled. |
| 23 | Evaluation Criteria | GET | `/api/evaluation_criteria` | ADMIN, MENTOR, STUDENT | Returns criteria list | Unauthenticated 401 | 200 | 401 | `EvaluationCriterionControllerTests.getCriteria_shouldReturnCriterionList` | AUTOMATED | All three roles are allowed by security config. |
| 24 | Evaluation Criteria | GET | `/api/evaluation_criteria/{criterion_id}` | ADMIN, MENTOR, STUDENT | Returns criterion detail | Missing criterion 404 | 200 | 404 | `EvaluationCriterionControllerTests.getCriterionById_shouldReturnCriterionDetail` | AUTOMATED | Missing criterion automated. |
| 25 | Evaluation Criteria | POST | `/api/evaluation_criteria` | ADMIN | Creates criterion | MENTOR/STUDENT 403; duplicate name 400; maxScore <= 0 returns 400 | 201 | 400, 403 | `EvaluationCriterionControllerTests.createCriterion_shouldReturnCreatedCriterion` | AUTOMATED | Invalid maxScore automated; duplicate covered by service tests. |
| 26 | Evaluation Criteria | PUT | `/api/evaluation_criteria/{criterion_id}` | ADMIN | Updates criterion | MENTOR/STUDENT 403; duplicate name excluding current 400; maxScore <= 0 returns 400 | 200 | 400, 403 | `EvaluationCriterionControllerTests.updateCriterion_shouldReturnUpdatedCriterion` | AUTOMATED | Duplicate/maxScore rules covered by service tests. |
| 27 | Evaluation Criteria | DELETE | `/api/evaluation_criteria/{criterion_id}` | ADMIN | Deletes criterion and returns null data | MENTOR/STUDENT 403; missing criterion 404 | 200 | 403, 404 | `EvaluationCriterionControllerTests.deleteCriterion_shouldReturnSuccessWithNullData` | AUTOMATED | FK behavior remains database-controlled. |

## Response Format Assertions

Automated controller tests assert representative success responses contain:

- `success = true`
- `status_code`
- `message`
- `data`
- `timestamp`

Automated error tests assert representative error responses contain:

- `success = false`
- `status_code`
- `error_code`
- `errors`
- `timestamp`

Serialization tests assert snake_case field names:

- `status_code`, not `statusCode`
- `error_code`, not `errorCode`

User-related controller and DTO tests assert responses do not expose:

- `passwordHash`
- `password_hash`
- raw `password`

## Automated Test Classes

- `AuthControllerTests`
- `UserControllerTests`
- `StudentControllerTests`
- `MentorControllerTests`
- `InternshipPhaseControllerTests`
- `EvaluationCriterionControllerTests`
- Existing service tests for business rules and ownership checks
- Existing DTO serialization tests for sensitive field exposure
- Existing common response serialization tests

## Documented RBAC Cases

The following cases are documented in the matrix and enforced by `SecurityConfig` and service-level ownership checks. They are suitable for manual Postman/curl verification or future full-context security MockMvc tests:

- Protected endpoints without JWT return 401.
- Non-ADMIN users receive 403 for `/api/users/**`.
- STUDENT cannot call `GET /api/students`.
- MENTOR can list only assigned students.
- STUDENT can view/update only own student profile.
- MENTOR cannot update student profile.
- MENTOR cannot call `GET /api/mentors`.
- MENTOR can view/update only own mentor profile.
- Internship Phase and Evaluation Criteria GET endpoints allow ADMIN, MENTOR, STUDENT.
- Internship Phase and Evaluation Criteria POST/PUT/DELETE endpoints allow ADMIN only.

## Known Limitations

- Real PostgreSQL smoke tests remain separate when `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` are not available.
- Controller tests use mocked services, so they verify controller paths, HTTP statuses, response wrapper shape, validation, and error formatting without touching the database.
- Full request-filter RBAC tests are documented rather than exhaustively automated to keep this student project test suite readable and independent from a real JWT/database runtime.

