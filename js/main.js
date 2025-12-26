// ============================================================================
// DASHBOARD-FUNCTIONALITEIT
// Dit script laadt de facturen uit LocalStorage en toont de statistieken
// en zoekresultaten op het dashboard (index.html).
// ============================================================================

// Haal de actuele lijst met facturen op uit de browseropslag
function haalFacturenOp() {
    return JSON.parse(localStorage.getItem('facturen')) || [];
}

// ============================================================================
// Functie: berekent en toont het totaal aantal facturen op het dashboard
// ============================================================================
function toonTotaalAantalFacturen(facturen) {
    const totaalVak = document.getElementById("totalInvoices");
    if (totaalVak) {
        totaalVak.textContent = facturen.length;
    }
}

// ============================================================================
// Functie: toont de laatste factuur (op basis van index)
// ============================================================================
function toonLaatsteFactuur(facturen) {
    const laatsteVak = document.getElementById("latestInvoice");
    if (!laatsteVak) return;

    if (!facturen || facturen.length === 0) {
        laatsteVak.textContent = "Geen facturen beschikbaar";
        return;
    }

    const laatste = facturen[facturen.length - 1];
    laatsteVak.textContent = `${laatste.invoiceNumber} – ${laatste.date}`;
}

// ============================================================================
// Functie: vult de tabel met recente facturen (max 3)
// ============================================================================
function vulRecenteFacturen(facturen) {
    const tbody = document.querySelector("#recentInvoicesTable tbody");
    if (!tbody) return;

    tbody.innerHTML = "";

    // Toon de laatste 3 facturen, de nieuwste bovenaan
    const recente = facturen.slice(-3).reverse();

    recente.forEach(f => {
        const rij = document.createElement("tr");
        rij.innerHTML = `
            <td>${f.invoiceNumber}</td>
            <td>${f.customerName}</td>
            <td style="color: var(--accent-kleur); font-weight: bold;">€ ${f.totalAmount.toFixed(2)}</td>
        `;
        tbody.appendChild(rij);
    });
}

// ============================================================================
// Zoekfunctie voor het dashboard (Snel inzien)
// ============================================================================
const searchInput = document.getElementById("dashboardSearch");
if (searchInput) {
    searchInput.addEventListener("input", function(e) {
        const term = e.target.value.toLowerCase();
        const resultatenVak = document.getElementById("searchResults");
        const facturen = haalFacturenOp();

        if (term.length < 2) {
            resultatenVak.innerHTML = "";
            return;
        }

        const matches = facturen.filter(f =>
            f.invoiceNumber.toLowerCase().includes(term) ||
            f.customerName.toLowerCase().includes(term)
        );

        resultatenVak.innerHTML = matches.map(f => `
            <div class="card" style="padding: 10px; margin-bottom: 5px; cursor: pointer; border: 1px solid var(--accent-kleur);" 
                 onclick="window.location.href='pages/edit_invoice.html?invoiceNumber=${f.invoiceNumber}'">
                <strong>${f.invoiceNumber}</strong> - ${f.customerName} 
                <span style="float: right; color: var(--accent-kleur);">€${f.totalAmount.toFixed(2)}</span>
            </div>
        `).join("");
    });
}

// ============================================================================
// Init-functie: voert alle dashboard-acties uit
// ============================================================================
function initDashboard() {
    const facturen = haalFacturenOp();

    toonTotaalAantalFacturen(facturen);
    toonLaatsteFactuur(facturen);
    vulRecenteFacturen(facturen);
}

// Voer init uit wanneer de pagina geladen is
window.onload = initDashboard;