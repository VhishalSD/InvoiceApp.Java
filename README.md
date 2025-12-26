1. Wat is dit project?

InvoiceApp is een webapp die ik heb gemaakt om facturen te beheren.
Je kunt facturen toevoegen, bekijken en opslaan. Alles draait in de browser, zonder backend of database.

Ik heb dit project gemaakt om te laten zien dat ik:
	•	met HTML, CSS en JavaScript kan werken
	•	validaties kan bouwen
	•	logisch kan nadenken over berekeningen
	•	en mijn code overzichtelijk kan houden

⸻

2. Wat kan de applicatie?

De app bestaat uit meerdere pagina’s:

Dashboard
	•	Snel zoeken op factuurnummer of klantnaam
	•	Zien hoeveel facturen er zijn
	•	Laatste factuur bekijken
	•	Overzicht van recente facturen

Facturen
	•	Overzicht van alle facturen
	•	Data komt uit LocalStorage

Factuur toevoegen
	•	Formulier om een nieuwe factuur toe te voegen
	•	Invoer wordt gecontroleerd
	•	Totaalbedrag wordt automatisch berekend
	•	Factuur wordt opgeslagen
	•	Je krijgt een popup als het gelukt is

⸻

3. Gebruikte technieken

Ik heb alleen frontend gebruikt:
	•	HTML voor de structuur
	•	CSS voor styling en dark/light theme
	•	JavaScript voor logica, validatie en berekeningen
	•	LocalStorage om facturen op te slaan

Er is geen server of database nodig.

⸻

4. Validatie (belangrijk deel)

Ik heb veel aandacht besteed aan validatie.

Datum
	•	Moet formaat dd-mm-jjjj hebben
	•	Moet een echte datum zijn
	•	Jaar mag niet te ver in het verleden of toekomst liggen

Klantnaam
	•	Minimaal 2 tekens

Omschrijving
	•	Minimaal 3 tekens

E-mail
	•	Moet een geldig e-mailadres zijn
	•	ik@ik..nl wordt niet geaccepteerd
	•	Geen dubbele puntjes
	•	Geen punt aan begin of einde

Aantal
	•	Minimaal 1
	•	Alleen hele getallen

Prijs
	•	Minimaal €0,01
	•	Wordt afgerond op 2 decimalen

BTW
	•	Moet tussen 0 en 1 liggen (bijvoorbeeld 0.21)


⸻

5. Berekening van het totaalbedrag

De berekening gebeurt pas als alles geldig is.

In simpele woorden:
	•	eerst: aantal × prijs
	•	daarna: btw erbij
	•	resultaat wordt afgerond op 2 decimalen

Zo voorkom ik verkeerde bedragen.

⸻

6. Opslag van gegevens

Alle facturen worden opgeslagen in LocalStorage van de browser.

Elke factuur bevat onder andere:
	•	factuurnummer (uniek)
	•	datum
	•	klantnaam
	•	e-mail
	•	omschrijving
	•	aantal
	•	prijs per stuk
	•	btw
	•	totaalbedrag

Bij opnieuw laden blijven de facturen gewoon staan.

⸻

7. Uiterlijk en gebruik
	•	Dark en light theme
	•	Thema wisselen met een knop
	•	Geen witte invoervelden in dark mode
	•	Geen vaste meldingen op de pagina
	•	Bevestigingen gebeuren via een popup (modal)

⸻

8. Controle & kwaliteit
	•	HTML is gecontroleerd met de validator
	•	Geen errors
	•	Code is overzichtelijk
	•	Alles werkt zonder console errors

⸻

9. Afsluiting

Dit project laat zien dat ik:
	•	zelfstandig een webapp kan bouwen
	•	problemen kan oplossen (zoals validatie en layout issues)
	•	logisch kan programmeren
	•	en aandacht heb voor gebruiksvriendelijkheid
