function toggleDoctorFields() {
    const role = document.getElementById("role").value;
    const doctorFields = document.getElementById("doctorFields");
    doctorFields.style.display = (role === "DOCTOR") ? "block" : "none";
}

document.getElementById("registerForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const errorMsg = document.getElementById("errorMsg");
    const successMsg = document.getElementById("successMsg");
    errorMsg.textContent = "";
    successMsg.textContent = "";

    const role = document.getElementById("role").value;

    const payload = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        role: role
    };

    if (role === "DOCTOR") {
        payload.specialization = document.getElementById("specialization").value;
        payload.experience = parseInt(document.getElementById("experience").value) || 0;
        payload.clinicAddress = document.getElementById("clinicAddress").value;
    }

    try {
        const data = await apiRequest("/auth/register", "POST", payload);

        saveToken(data.token, data.role, data.name, data.email);
        successMsg.textContent = "Registration successful! Redirecting...";

        setTimeout(() => {
            if (data.role === "PATIENT") {
                window.location.href = "doctors.html";
            } else {
                window.location.href = "add-slot.html";
            }
        }, 1000);

    } catch (err) {
        errorMsg.textContent = err.message;
    }
});