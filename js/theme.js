/* ============================================================
   THEMA LADEN VANUIT LOCALSTORAGE
   Controleert direct bij het inladen of er een voorkeur is.
   ============================================================ */
const opgeslagenThema = localStorage.getItem("theme");
if (opgeslagenThema) {
    document.body.dataset.theme = opgeslagenThema;
}

// ============================================================================
// Thema-wisselaar: schakelt tussen licht en donker thema.
// ============================================================================
function initThemaWisselaar() {
    const knop = document.getElementById("themeToggle");

    // Controleer of de knop op deze pagina bestaat
    if (!knop) return;

    knop.addEventListener("click", () => {
        const huidigThema = document.body.dataset.theme || "light";
        const nieuwThema = huidigThema === "dark" ? "light" : "dark";

        document.body.dataset.theme = nieuwThema;

        /* Thema opslaan zodat het blijft tijdens pagina-wissels */
        localStorage.setItem("theme", nieuwThema);
    });
}

// Start de wisselaar zodra de DOM geladen is
document.addEventListener("DOMContentLoaded", initThemaWisselaar);

// ... onderaan je theme.js ...

/**
 * 📢 UNIVERSELE MODAL FUNCTIE (Vervangt alert en confirm)
 */
window.toonBericht = function(titel, bericht, isConfirm = false, callback = null) {
    const modal = document.getElementById("customModal");
    const titelVak = document.getElementById("modalTitle");
    const berichtVak = document.getElementById("modalMessage");
    const knoppenVak = document.getElementById("modalButtons");

    if (!modal) return;

    titelVak.textContent = titel;
    berichtVak.textContent = bericht;
    knoppenVak.innerHTML = "";

    // OK / Bevestig knop
    const okBtn = document.createElement("button");
    okBtn.textContent = isConfirm ? "Bevestigen" : "Begrepen";
    okBtn.className = isConfirm ? "deleteBtn" : "editBtn";
    okBtn.onclick = () => {
        modal.style.display = "none";
        if (callback) callback();
    };

    // Annuleren knop (alleen bij confirm)
    if (isConfirm) {
        const cancelBtn = document.createElement("button");
        cancelBtn.textContent = "Annuleren";
        cancelBtn.className = "editBtn";
        cancelBtn.style.background = "rgba(255,255,255,0.1)";
        cancelBtn.onclick = () => modal.style.display = "none";
        knoppenVak.appendChild(cancelBtn);
    }

    knoppenVak.appendChild(okBtn);
    modal.style.display = "flex";
};