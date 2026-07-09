if (!isLoggedIn()) {
    window.location.href = "index.html";
}

const form = document.getElementById("slotForm");

form.addEventListener("submit", async (e) => {

    e.preventDefault();

    const msgBox = document.getElementById("msgBox");

    const slot = {

        date: document.getElementById("date").value,

        startTime: document.getElementById("startTime").value + ":00",

        endTime: document.getElementById("endTime").value + ":00"

    };

    try {

        await apiRequest("/doctors/slots", "POST", slot);

        msgBox.innerHTML =
            `<p class="success-msg">Slot Added Successfully.</p>`;

        form.reset();

    } catch (err) {

        msgBox.innerHTML =
            `<p class="error-msg">${err.message}</p>`;

    }

});