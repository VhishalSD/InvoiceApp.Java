// ============================================================================
// FACTUUR-OVERZICHT – FRONTEND LOGICA
// Dit script beheert de lijst met facturen en de interactie in de tabel.
// ============================================================================

// Haal de data op uit de 'browser database'
let facturen = JSON.parse(localStorage.getItem('facturen')) || [
    {
        invoiceNumber: "2025-0001",
        customerName: "Demo Klant",
        description: "Voorbeeld omschrijving",
        date: "01-01-2025",
        totalAmount: 199.95
    }
];

/**
 * Slaat de huidige staat van de facturenlijst op in LocalStorage.
 */
function slaFacturenOp() {
    localStorage.setItem('facturen', JSON.stringify(facturen));
}

// ============================================================================
// Functie: verwijder factuur met de nieuwe Custom Modal
// ============================================================================
function verwijderFactuur(invoiceNumber) {
    // Gebruik onze eigen mooie pop-up in plaats van de standaard confirm()
    window.toonBericht(
        "Verwijderen bevestigen",
        `Weet je zeker dat je factuur ${invoiceNumber} wilt verwijderen? Dit kan niet ongedaan worden gemaakt.`,
        true, // Zorgt voor de 'Annuleren' knop
        () => {
            // Deze code wordt pas uitgevoerd als de gebruiker op 'Bevestigen' klikt
            facturen = facturen.filter(f => f.invoiceNumber !== invoiceNumber);
            slaFacturenOp();
            vulFacturenTabel();

            // Optioneel: toon een korte bevestiging dat het gelukt is
            window.toonBericht("Verwijderd", `Factuur ${invoiceNumber} is succesvol gewist.`);
        }
    );
}

// ============================================================================
// Functie: navigeer naar edit-pagina
// ============================================================================
function bewerkFactuur(invoiceNumber) {
    window.location.href = `edit_invoice.html?invoiceNumber=${invoiceNumber}`;
}

// ============================================================================
// Functie: tabel vullen met data uit de browser
// ============================================================================
function vulFacturenTabel() {
    const tbody = document.querySelector("#invoicesTable tbody");
    if (!tbody) return;

    tbody.innerHTML = ""; // Maak tabel leeg

    facturen.forEach(f => {
        const rij = document.createElement("tr");

        rij.innerHTML = `
            <td>${f.invoiceNumber}</td>
            <td>${f.customerName}</td>
            <td>${f.description || '-'}</td>
            <td>${f.date}</td>
            <td><strong>€ ${f.totalAmount.toFixed(2)}</strong></td>
            <td>
                <button class="editBtn">Bewerken</button>
                <button class="deleteBtn">Verwijderen</button>
            </td>
        `;

        // Koppel de acties aan de nieuwe knoppen
        rij.querySelector(".editBtn").addEventListener("click", () => bewerkFactuur(f.invoiceNumber));
        rij.querySelector(".deleteBtn").addEventListener("click", () => verwijderFactuur(f.invoiceNumber));

        tbody.appendChild(rij);
    });
}

// ============================================================================
// Initialisatie
// ============================================================================
function initFacturenPagina() {
    vulFacturenTabel();
}

// Start wanneer de pagina volledig geladen is
window.onload = initFacturenPagina;