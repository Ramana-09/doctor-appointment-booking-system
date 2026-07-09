document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const errorMsg = document.getElementById("errorMsg");

    errorMsg.textContent = "";

    try {
        const data = await apiRequest("/auth/login", "POST", { email, password });

        saveToken(data.token, data.role, data.name, data.email);

        // Role vachi correct page-ku redirect pண்ணும்
        if (data.role === "PATIENT") {
            window.location.href = "doctors.html";
        } else if (data.role === "DOCTOR") {
            window.location.href = "add-slot.html";
        } else {
            window.location.href = "doctors.html";
        }

    } catch (err) {
        errorMsg.textContent = err.message;
    }
});