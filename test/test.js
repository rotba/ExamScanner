const firebase = require("@firebase/testing");
const fs = require("fs");

/*
 * ============
 *    Setup
 * ============
 */
const databaseName = "database-emulator-example";
const coverageUrl = `http://localhost:9000/.inspect/coverage?ns=${databaseName}`;

const rules = fs.readFileSync("database.rules.json", "utf8");

/**
 * Creates a new app with authentication data matching the input.
 *
 * @param {object} auth the object to use for authentication (typically {uid: some-uid})
 * @return {object} the app.
 */
function authedApp(auth) {
  return firebase.initializeTestApp({ databaseName, auth }).database();
}

/**
 * Creates a new admin app.
 *
 * @return {object} the app.
 */
function adminApp() {
  return firebase.initializeAdminApp({ databaseName }).database();
}

function createExam(){
	return true;
};

function createVersion(examId){
	return {examId:examId.toString()};
};

function createQuestion(versionId){
	return {versionId:versionId.toString()};
};

async function createExamContext(db, id){
  await db.ref(`exams/${id}`).set(createExam());
}

async function createVersionContext(db, id, examId){
  createExamContext(db,examId);
  await db.ref(`versions/${id}`).set(createVersion(id,examId));
}

/*
 * ============
 *  Test Cases
 * ============
 */
before(async () => {
  // Set database rules before running these tests
  await firebase.loadDatabaseRules({
    databaseName,
    rules: rules
  });
});

beforeEach(async () => {
  // Clear the database between tests
  await adminApp()
    .ref()
    .set(null);
});

after(async () => {
  // Close any open apps
  await Promise.all(firebase.apps().map(app => app.delete()));
  console.log(`View rule coverage information at ${coverageUrl}\n`);
});


describe("versions validate rules", () => {
  it("should have FK to exams", async () => {
    const alice = authedApp({ uid: "alice" });
    createExamContext(alice, 1);
    await firebase.assertSucceeds(alice.ref("versions/1").set(createVersion(1)));
    await firebase.assertFails(alice.ref("versions/2").set(createVersion(2)));
  });
});

describe("questions validate rules", () => {
  it("should have FK to versions", async () => {
    const alice = authedApp({ uid: "alice" });
    createVersionContext(alice, 1,1);
    await firebase.assertSucceeds(alice.ref("questions/1").set(createQuestion(1)));
    await firebase.assertFails(alice.ref("questions/2").set(createQuestion(2)));
  });
});


