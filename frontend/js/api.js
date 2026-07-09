const BASE_URL = "http://localhost:8080/api";

// Token-a localStorage-la save pண்ணும்
function saveToken(token, role, name, email) {
    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("name", name);
    localStorage.setItem("email", email);
}

// Token-a edukum
function getToken() {
    return localStorage.getItem("token");
}

// Logged-in-ah illaியா check pண்ணும்
function isLoggedIn() {
    return getToken() !== null;
}

// Logout pண்ணும்
function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

// Common fetch wrapper — token automatic-ah header-la add pண்ணும்
async function apiRequest(endpoint, method = "GET", body = null) {
    const headers = {
        "Content-Type": "application/json"
    };

    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const options = {
        method: method,
        headers: headers
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(BASE_URL + endpoint, options);

    if (!response.ok) {
        const errorData = await response.json().catch(() => ({ error: "Something went wrong" }));
        throw new Error(errorData.error || errorData.message || "Request failed");
    }

    // 204 No Content-ku empty response varும், adha handle pண்ணும்
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}