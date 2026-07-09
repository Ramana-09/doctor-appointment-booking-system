// Login pண்ணி இல்லைனா, login page-ku thirumba anупு
if (!isLoggedIn()) {
    window.location.href = "index.html";
}

async function loadDoctors() {
    const listDiv = document.getElementById("doctorsList");
    listDiv.innerHTML = "Loading...";

    try {
        const doctors = await apiRequest("/doctors", "GET");

        if (doctors.length === 0) {
            listDiv.innerHTML = "<p>No doctors available right now.</p>";
            return;
        }

        listDiv.innerHTML = doctors.map(doc => `
            <div class="card">
                <h3>Dr. ${doc.name}</h3>
                <p><strong>Specialization:</strong> ${doc.specialization}</p>
                <p><strong>Experience:</strong> ${doc.experience} years</p>
                <p><strong>Clinic:</strong> ${doc.clinicAddress || "N/A"}</p>
                <a class="btn-small" href="slots.html?doctorId=${doc.id}">View Available Slots</a>
            </div>
        `).join("");

    } catch (err) {
        listDiv.innerHTML = `<p class="error-msg">${err.message}</p>`;
    }
}

loadDoctors();