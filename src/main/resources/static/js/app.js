// ✅ CHECK IF CURRENT PAGE IS LOGIN OR REGISTER
const currentPage = window.location.pathname;

// 🔥 RUN BLOCK ONLY FOR PROTECTED PAGES
if (!currentPage.includes("login.html") && !currentPage.includes("register.html")) {

    const tokenCheck = localStorage.getItem("token");

    if (!tokenCheck || tokenCheck === "undefined" || tokenCheck === "null" || tokenCheck.trim() === "") {
        window.location.replace("login.html");
    }
}

let globalResume = "";

// 🔐 AUTH CHECK (IMPROVED + SAFE)
async function checkAuth() {

    const token = localStorage.getItem("token");

    if (!token || token === "undefined" || token === "null" || token.trim() === "") {
        window.location.replace("login.html");
        return false;
    }

    try {
        const res = await fetch("/auth/validate", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (res.status !== 200) {
            localStorage.removeItem("token");
            window.location.replace("login.html");
            return false;
        }

        return true;

    } catch (e) {
        window.location.replace("login.html");
        return false;
    }
}

// 🔥 PAGE LOAD CHECK (ONLY FOR PROTECTED PAGES)
window.onload = async function () {

    if (!currentPage.includes("login.html") && !currentPage.includes("register.html")) {
        const ok = await checkAuth();
        if (!ok) return;
    }
};

// 🔐 LOGOUT
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

// 🔧 HEADERS (SAFE)
function getHeaders(isJson = true) {

    const token = localStorage.getItem("token");

    if (!token) {
        window.location.replace("login.html");
        return {};
    }

    return isJson
        ? {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
        : {
            "Authorization": "Bearer " + token
        };
}

// 🔥 RESPONSE HANDLE (IMPROVED)
function handleResponse(res) {

    if (res.status === 401 || res.status === 403) {
        alert("Session expired ❌ Please login again");

        localStorage.removeItem("token");
        window.location.href = "login.html";

        throw new Error("Unauthorized");
    }

    return res;
}

// 🔍 ANALYZE
function analyze() {

    let text = document.getElementById("resumeText").value;

    if (!text.trim()) {
        alert("Enter resume first");
        return;
    }

    globalResume = text;
    document.getElementById("result").innerText = "Analyzing...";

    fetch("/resume/analyze", {
        method: "POST",
        headers: getHeaders(true),
        body: JSON.stringify({ resume: text })
    })
        .then(handleResponse)
        .then(res => res.text())
        .then(data => {
            document.getElementById("result").innerText = data;
            updateScore(data);
        })
        .catch(() => {
            document.getElementById("result").innerText = "Server Error";
        });
}

// 📄 PDF ANALYZE
function analyzePDF() {

    let file = document.getElementById("resumeFile").files[0];

    if (!file) {
        alert("Select PDF first");
        return;
    }

    let formData = new FormData();
    formData.append("file", file);

    document.getElementById("result").innerText = "Reading PDF...";

    fetch("/resume/analyze-pdf", {
        method: "POST",
        headers: getHeaders(false),
        body: formData
    })
        .then(handleResponse)
        .then(res => res.json())
        .then(data => {
            globalResume = data.resume;
            document.getElementById("result").innerText = data.result;
            updateScore(data.result);
        })
        .catch(() => {
            document.getElementById("result").innerText = "PDF Error";
        });
}

// 💼 MATCH JOB
function matchJob() {

    let job = document.getElementById("jobDesc").value;

    if (!globalResume) {
        alert("Upload or analyze resume first");
        return;
    }

    document.getElementById("result").innerText = "Matching...";

    fetch("/resume/match", {
        method: "POST",
        headers: getHeaders(true),
        body: JSON.stringify({ resume: globalResume, job: job })
    })
        .then(handleResponse)
        .then(res => res.text())
        .then(data => {
            document.getElementById("result").innerText = data;
        });
}

// 💌 COVER LETTER
function generateCoverLetter() {

    let job = document.getElementById("jobDesc").value;

    if (!globalResume) {
        alert("Upload or analyze resume first");
        return;
    }

    document.getElementById("result").innerText = "Generating cover letter...";

    fetch("/resume/cover-letter", {
        method: "POST",
        headers: getHeaders(true),
        body: JSON.stringify({ resume: globalResume, job: job })
    })
        .then(handleResponse)
        .then(res => res.text())
        .then(data => {
            document.getElementById("result").innerText = data;
        });
}

// 📊 SCORE
function updateScore(data) {

    let match = data.match(/(\d{2})\/100/);

    if (match) {
        let score = match[1];
        document.getElementById("scoreText").innerText = score;
        document.getElementById("scoreBar").style.width = score + "%";
    }
}

// 🌐 PORTFOLIO
function generatePortfolio() {

    if (!globalResume) {
        alert("Analyze or upload resume first");
        return;
    }

    document.getElementById("portfolioEditor").value = "Generating...";

    fetch("/portfolio/generate", {
        method: "POST",
        headers: getHeaders(true),
        body: JSON.stringify({ resume: globalResume })
    })
        .then(handleResponse)
        .then(res => res.text())
        .then(data => {
            document.getElementById("portfolioEditor").value = data;
        });
}