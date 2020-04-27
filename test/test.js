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

function createExam(manager){
	return {manager:manager, seal:false};
};

function createVersion(examId){
	return {examId:examId.toString()};
};

function createQuestion(versionId){
	return {versionId:versionId.toString()};
};

function createExamineeSolution(versionId){
	return {versionId:versionId.toString()};
};

function createExamineeAnswer(examineeSolutionId){
	return {examineeSolutionId:examineeSolutionId.toString()};
};

async function createExamContext(db, id, manager){
  await db.ref(`exams/${id}`).set(createExam(manager));
}

async function createVersionContext(db, id, examId, manager){
  createExamContext(db,examId, manager);
  await db.ref(`versions/${id}`).set(createVersion(id,examId));
}

async function createExamineeSolutionContext(db, id, verionId, examId, manager){
  createVersionContext(db, verionId,examId, manager);
  await db.ref(`examineeSolutions/${id}`).set(createExamineeSolution(verionId));
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
    createExamContext(alice, 1, "alice");
    await firebase.assertSucceeds(alice.ref("versions/1").set(createVersion(1)));
    await firebase.assertFails(alice.ref("versions/2").set(createVersion(2)));
  });
});

describe("examinees solution validate rules", () => {
  it("should have FK to versions", async () => {
    const alice = authedApp({ uid: "alice" });
    createVersionContext(alice, 1,1, "alice");
    await firebase.assertSucceeds(alice.ref("examineeSolutions/1").set(createExamineeSolution(1)));
    await firebase.assertFails(alice.ref("examineeSolutions/2").set(createExamineeSolution(2)));
  });
});

describe("examinees answer validate rules", () => {
  it("should have FK to examinee solutions", async () => {
    const alice = authedApp({ uid: "alice" });
    createExamineeSolutionContext(alice, 1,1,1, "alice");
    await firebase.assertSucceeds(alice.ref("examineeAnswers/1").set(createExamineeAnswer(1)));
    await firebase.assertFails(alice.ref("examineeAnswers/2").set(createExamineeAnswer(2)));
  });
});

describe("questions validate rules", () => {
  it("should have FK to versions", async () => {
    const alice = authedApp({ uid: "alice" });
    createVersionContext(alice, 1,1, "alice");
    await firebase.assertSucceeds(alice.ref("questions/1").set(createQuestion(1)));
    await firebase.assertFails(alice.ref("questions/2").set(createQuestion(2)));
  });
});

describe("exam creation", () => {
  it("should require the user creating a exam to be its manager", async () => {
    const alice = authedApp({ uid: "alice" });

    // should not be able to create exam managed by another user
    await firebase.assertFails(alice.ref("exams/1").set(createExam("bob")));
    // should not be able to create room with no owner
    await firebase.assertFails(
      alice.ref("exams/1").set({ graders: { alice: true } })
    );
    // alice should be allowed to create a room she owns
    await firebase.assertSucceeds(
      alice.ref("exams/1").set(createExam("alice"))
    );
  });
});

describe("exam write rules", () => {
  it("should allow only the manager to update exam  after he seals the exam", async () => {
    const alice = authedApp({ uid: "alice" });
    const bob = authedApp({ uid: "bob" });
    await alice.ref("exams/1").set(createExam("alice"));
    await alice.ref("exams/1/courseName").set("COMP");
    await firebase.assertSucceeds(
      bob.ref("exams/1/courseName").set("SPL")
    );
    await firebase.assertSucceeds(
      alice.ref("exams/1/courseName").set("COMP")
    );
    await alice.ref("exams/1/seal").set(true);
    await firebase.assertFails(
      bob.ref("exams/1/courseName").set("SPL")
    );
    await firebase.assertSucceeds(
      alice.ref("exams/1/courseName").set("CASPL")
    );
  });
});



