if (!isLoggedIn()) {
    window.location.href = "index.html";
}

async function loadAppointments() {
    const listDiv = document.getElementById("appointmentsList");
    listDiv.innerHTML = "Loading...";

    try {
        const appointments = await apiRequest("/appointments/my", "GET");

        if (appointments.length === 0) {
            listDiv.innerHTML = "<p>You have no appointments yet.</p>";
            return;
        }

        listDiv.innerHTML = appointments.map(appt => `
            <div class="card">
                <h3>Dr. ${appt.doctorName} (${appt.specialization})</h3>
                <p><strong>Date:</strong> ${appt.date}</p>
                <p><strong>Time:</strong> ${appt.startTime} - ${appt.endTime}</p>
                <p><strong>Reason:</strong> ${appt.reasonForVisit || "N/A"}</p>
                <p><span class="status-badge status-${appt.status.toLowerCase()}">${appt.status}</span></p>
                ${appt.status === "CONFIRMED" ? `<button class="btn-small" style="background:#e63946;" onclick="cancelAppointment(${appt.id})">Cancel Appointment</button>` : ""}
            </div>
        `).join("");

    } catch (err) {
        listDiv.innerHTML = `<p class="error-msg">${err.message}</p>`;
    }
}

async function cancelAppointment(id) {
    const msgBox = document.getElementById("msgBox");

    if (!confirm("Are you sure you want to cancel this appointment?")) {
        return;
    }

    try {
        await apiRequest(`/appointments/${id}/cancel`, "PUT");
        msgBox.innerHTML = `<p class="success-msg">Appointment cancelled successfully.</p>`;
        loadAppointments();
    } catch (err) {
        msgBox.innerHTML = `<p class="error-msg">${err.message}</p>`;
    }
}

loadAppointments();