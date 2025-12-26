// ============================================================================
// FACTUUR BEWERKEN – FRONTEND LOGICA
// ============================================================================

function haalAlleFacturen() {
    let data = JSON.parse(localStorage.getItem('facturen'));
    return Array.isArray(data) ? data : [];
}

function haalInvoiceNummerUitUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("invoiceNumber");
}

/**
 * Helper functies voor visuele validatie (gelijk aan add_invoice.js)
 */
function toonFout(veldId, bericht) {
    const veld = document.getElementById(veldId);
    const foutSpan = document.getElementById(`${veldId}-error`);
    if (veld) veld.classList.add("invalid");
    if (foutSpan) foutSpan.textContent = bericht;
}

function wisFouten() {
    document.querySelectorAll(".error-message").forEach(s => s.textContent = "");
    document.querySelectorAll("input, textarea, select").forEach(i => i.classList.remove("invalid"));
}

function vulFormulier(factuur) {
    document.getElementById("invoiceNumber").value = factuur.invoiceNumber;
    document.getElementById("date").value = factuur.date;
    document.getElementById("customerName").value = factuur.customerName;
    document.getElementById("customerEmail").value = factuur.customerEmail;
    document.getElementById("description").value = factuur.description;
    document.getElementById("quantity").value = factuur.quantity;
    document.getElementById("pricePerUnit").value = factuur.pricePerUnit;
    document.getElementById("vatRate").value = factuur.vatRate;
}

document.getElementById("editInvoiceForm").addEventListener("submit", function (e) {
    e.preventDefault();
    wisFouten();

    const invoiceNumber = document.getElementById("invoiceNumber").value;
    let isGeldig = true;

    // Nieuwe waarden ophalen en direct opschonen
    const updatedInvoice = {
        invoiceNumber: invoiceNumber,
        date: document.getElementById("date").value.trim(),
        customerName: document.getElementById("customerName").value.trim(),
        customerEmail: document.getElementById("customerEmail").value.trim(),
        description: document.getElementById("description").value.trim(),
        quantity: 0,
        pricePerUnit: 0,
        vatRate: 0,
        totalAmount: 0
    };

    // KALENDER-VALIDATIE
    const dateRegex = /^(\d{2})-(\d{2})-(\d{4})$/;
    const dateMatch = updatedInvoice.date.match(dateRegex);
    if (!dateMatch) {
        toonFout("date", "Gebruik dd-mm-jjjj.");
        isGeldig = false;
    } else {
        const d = parseInt(dateMatch[1], 10);
        const m = parseInt(dateMatch[2], 10);
        const y = parseInt(dateMatch[3], 10);
        const testDate = new Date(y, m - 1, d);
        if (testDate.getFullYear() !== y || testDate.getMonth() !== m - 1 || testDate.getDate() !== d) {
            toonFout("date", "Datum bestaat niet.");
            isGeldig = false;
        }
    }

    // NAAM & EMAIL
    if (updatedInvoice.customerName.length < 2) {
        toonFout("customerName", "Minimaal 2 tekens.");
        isGeldig = false;
    }
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailPattern.test(updatedInvoice.customerEmail)) {
        toonFout("customerEmail", "Ongeldig e-mailadres.");
        isGeldig = false;
    }

    // NUMERIEKE NORMALISATIE (Consistent met add_invoice)
    const rawQty = parseInt(document.getElementById("quantity").value, 10);
    const rawPrice = parseFloat(document.getElementById("pricePerUnit").value.toString().replace(',', '.'));
    const rawVat = parseFloat(document.getElementById("vatRate").value.toString().replace(',', '.'));

    if (isNaN(rawQty) || rawQty < 1) { toonFout("quantity", "Minimaal 1."); isGeldig = false; }
    else { updatedInvoice.quantity = rawQty; }

    if (isNaN(rawPrice) || rawPrice < 0.01) { toonFout("pricePerUnit", "Ongeldige prijs."); isGeldig = false; }
    else { updatedInvoice.pricePerUnit = Math.round(rawPrice * 100) / 100; }

    if (isNaN(rawVat) || rawVat < 0 || rawVat > 1) { toonFout("vatRate", "Tussen 0 en 1."); isGeldig = false; }
    else { updatedInvoice.vatRate = Math.round(rawVat * 10000) / 10000; }

    if (!isGeldig) return;

    // Totaal berekenen
    const base = updatedInvoice.quantity * updatedInvoice.pricePerUnit;
    updatedInvoice.totalAmount = Math.round((base + (base * updatedInvoice.vatRate)) * 100) / 100;

    // OPSLAAN IN LOCALSTORAGE
    let facturen = haalAlleFacturen();
    facturen = facturen.map(f => f.invoiceNumber === invoiceNumber ? updatedInvoice : f);
    localStorage.setItem('facturen', JSON.stringify(facturen));

    // VERVANG DE ALERT DOOR DE CUSTOM MODAL
    window.toonBericht(
        "Bijgewerkt",
        `De wijzigingen voor factuur ${invoiceNumber} zijn succesvol opgeslagen.`,
        false,
        () => {
            window.location.href = "invoices.html";
        }
    );
});

function initEditPagina() {
    const invoiceNumber = haalInvoiceNummerUitUrl();
    if (!invoiceNumber) return;
    const facturen = haalAlleFacturen();
    const factuur = facturen.find(f => f.invoiceNumber === invoiceNumber);
    if (factuur) vulFormulier(factuur);
    else window.location.href = "invoices.html";
}

window.onload = initEditPagina;