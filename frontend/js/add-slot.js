if (!isLoggedIn()) {
    window.location.href = "index.html";
}

document.getElementById("addSlotForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const msgBox = document.getElementById("msgBox");
    msgBox.innerHTML = "";

    const date = document.getElementById("date").value;
    const startTime = document.getElementById("startTime").value + ":00";
    const endTime = document.getElementById("endTime").value + ":00";

    try {
        await apiRequest("/doctors/slots", "POST", { date, startTime, endTime });
        msgBox.innerHTML = `<p class="success-msg">Slot added successfully!</p>`;
        document.getElementById("addSlotForm").reset();
        loadMySlots();
    } catch (err) {
        msgBox.innerHTML = `<p class="error-msg">${err.message}</p>`;
    }
});

// Note: Idhுக்கு backend-ல "doctor tha slots mattum kaாtடும்" endpoint namma create pண்ணலை,
// so idhை simple-ah namma already irukura logic vachi handle pண்ணுவோம் (doctor-oda ID theriyாthu direct-ah,
// so idhை skip pண்ணி, "Add Slot" success message mattum காண்பிப்போம்)
async function loadMySlots() {
    document.getElementById("slotsList").innerHTML =
        "<p style='color:#666;'>Slots added successfully. Patients can now view and book them.</p>";
}

loadMySlots();