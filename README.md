1. Run docker-compose.yml
2. Create a user with certain role in Keycloak (Patient or Practitioner). Specify Patients in the "Permissions" field Patient.Read, Patient.Write, Patient.Delete (separated by commas).
3. Set password for created user.
4. Open Postman and test all neccesary methods (use Patients.postman_collection.json file).
