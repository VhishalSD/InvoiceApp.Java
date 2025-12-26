
// ============================================================================
// UNIEKE ID GENERATIE (Collision Detection)
// ============================================================================
function genereerUniekFactuurnummer() {
    const huidigeFacturen = JSON.parse(localStorage.getItem('facturen')) || [];
    let nieuwNummer;
    let isBezet = true;

    while (isBezet) {
        const jaar = new Date().getFullYear();
        const random = Math.floor(Math.random() * 9000) + 1000;
        nieuwNummer = `${jaar}-${random}`;
        isBezet = huidigeFacturen.some(f => f.invoiceNumber === nieuwNummer);
    }

    return nieuwNummer;
}

// ============================================================================
// HELPER FUNCTIES VOOR UI-FEEDBACK
// ============================================================================
function toonFout(veldId, bericht) {
    const veld = document.getElementById(veldId);
    const foutSpan = document.getElementById(`${veldId}-error`);

    if (veld) veld.classList.add("invalid");
    if (foutSpan) foutSpan.textContent = bericht;
}

function wisFouten() {
    document.querySelectorAll(".error-message").forEach(s => s.textContent = "");
    document.querySelectorAll("input").forEach(i => i.classList.remove("invalid"));
}

// ============================================================================
// FORMULIER VERWERKING & VALIDATIE
// ============================================================================
document.getElementById("addInvoiceForm").addEventListener("submit", function (e) {
    e.preventDefault();
    wisFouten();

    let isGeldig = true;

    // Basis-object aanmaken
    const invoice = {
        invoiceNumber: genereerUniekFactuurnummer(),
        date: document.getElementById("date").value.trim(),
        customerName: document.getElementById("customerName").value.trim(),
        customerEmail: document.getElementById("customerEmail").value.trim(),
        description: document.getElementById("description").value.trim(),
        quantity: 0,
        pricePerUnit: 0,
        vatRate: 0,
        totalAmount: 0
    };

    // ------------------------------------------------------------------------
    // DATUM VALIDATIE (dd-mm-jjjj + echte kalenderdatum)
    // ------------------------------------------------------------------------
    const dateRegex = /^(\d{2})-(\d{2})-(\d{4})$/;
    const dateMatch = invoice.date.match(dateRegex);

    if (!dateMatch) {
        toonFout("date", "Gebruik formaat dd-mm-jjjj.");
        isGeldig = false;
    } else {
        const d = parseInt(dateMatch[1], 10);
        const m = parseInt(dateMatch[2], 10);
        const y = parseInt(dateMatch[3], 10);
        const testDatum = new Date(y, m - 1, d);

        if (testDatum.getFullYear() !== y || testDatum.getMonth() !== m - 1 || testDatum.getDate() !== d) {
            toonFout("date", "Deze kalenderdatum bestaat niet.");
            isGeldig = false;
        } else if (y < 2000 || y > 2100) {
            toonFout("date", "Jaar moet tussen 2000 en 2100 liggen.");
            isGeldig = false;
        }
    }

    // ------------------------------------------------------------------------
    // KLANTNAAM & OMSCHRIJVING
    // ------------------------------------------------------------------------
    if (invoice.customerName.length < 2) {
        toonFout("customerName", "Minimaal 2 tekens.");
        isGeldig = false;
    }

    if (invoice.description.length < 3) {
        toonFout("description", "Omschrijving te kort.");
        isGeldig = false;
    }

    // ------------------------------------------------------------------------
    // E-MAIL VALIDATIE (stricter: geen dubbele puntjes zoals ik@ik..nl)
    // ------------------------------------------------------------------------
    function isValidEmailStrict(email) {
        // 1) Basis patroon
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test(email)) return false;

        // 2) Geen dubbele punten (..)
        if (email.includes("..")) return false;

        // 3) Local-part en domain mogen niet beginnen/eindigen met '.'
        const parts = email.split("@");
        if (parts.length !== 2) return false;

        const local = parts[0];
        const domain = parts[1];

        if (local.startsWith(".") || local.endsWith(".")) return false;
        if (domain.startsWith(".") || domain.endsWith(".")) return false;

        return true;
    }
    if (!isValidEmailStrict(invoice.customerEmail)) {
        toonFout("customerEmail", "Ongeldig e-mailadres.");
        isGeldig = false;
    }

    // ------------------------------------------------------------------------
    // NUMERIEKE VALIDATIE + OPSCHONING
    // ------------------------------------------------------------------------

    // 📦 Aantal (Quantity)
    const rawQty = document.getElementById("quantity").value.trim();
    if (rawQty === "") {
        toonFout("quantity", "Aantal is verplicht.");
        isGeldig = false;
    } else {
        const qty = parseInt(rawQty, 10);
        if (isNaN(qty) || qty < 1 || qty > 10000) {
            toonFout("quantity", "Aantal tussen 1 en 10.000.");
            isGeldig = false;
        } else {
            invoice.quantity = qty; // ✅ terugschrijven naar object
        }
    }

    // 💰 Prijs per stuk (PricePerUnit)
    const rawPrice = document.getElementById("pricePerUnit").value.trim().replace(',', '.');
    if (rawPrice === "") {
        toonFout("pricePerUnit", "Prijs is verplicht.");
        isGeldig = false;
    } else {
        const price = parseFloat(rawPrice);
        if (isNaN(price) || price < 0.01) {
            toonFout("pricePerUnit", "Minimaal € 0,01.");
            isGeldig = false;
        } else if (price > 1000000) {
            toonFout("pricePerUnit", "Maximum € 1.000.000.");
            isGeldig = false;
        } else {
            invoice.pricePerUnit = Math.round(price * 100) / 100; // ✅ 2 decimalen
        }
    }

    // 📊 BTW-tarief (VatRate) verwacht 0..1 (bijv. 0.21)
    const rawVat = document.getElementById("vatRate").value.trim().replace(',', '.');
    if (rawVat === "") {
        toonFout("vatRate", "BTW-tarief is verplicht.");
        isGeldig = false;
    } else {
        const vat = parseFloat(rawVat);
        if (isNaN(vat) || vat < 0 || vat > 1) {
            toonFout("vatRate", "Moet tussen 0 en 1 liggen (bijv. 0.21).");
            isGeldig = false;
        } else {
            invoice.vatRate = vat; // ✅ terugschrijven naar object
        }
    }

    // Stop als validatie faalt
    if (!isGeldig) return;

    // ============================================================================
    // BEREKENING (FIX)
    // totalAmount = (quantity * pricePerUnit) + BTW
    // ============================================================================
    const base = invoice.quantity * invoice.pricePerUnit;
    const total = base + (base * invoice.vatRate);
    invoice.totalAmount = Math.round(total * 100) / 100;

    // ============================================================================
    // OPSLAAN
    // ============================================================================
    const data = JSON.parse(localStorage.getItem('facturen'));
    const alleFacturen = Array.isArray(data) ? data : [];

    alleFacturen.push(invoice);
    localStorage.setItem('facturen', JSON.stringify(alleFacturen));

    // Succes feedback + redirect
    const titel = "Factuur Opgeslagen";
    const bericht = `Factuur ${invoice.invoiceNumber} is succesvol opgeslagen.`;

    window.toonBericht(
        titel,
        bericht,
        false,
        () => {
            window.location.href = "invoices.html";
        }
    );

});