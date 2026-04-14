# Product Requirements Document (PRD)

**Project Name:** Smart Rental Search Algorithm
**Module:** CS2103DE Team Project (tP)
**Target Release:** v0.3
**Format:** Standalone Desktop Application (Java/GUI)

## 1. Product Overview
The Smart Rental Search Algorithm is a desktop Java application designed to reverse the conventional location-based property search. Instead of requiring users to know which neighborhood they want to live in, the system allows users to input their primary daily destination (e.g., school or workplace) and their commute constraints. The application then calculates and presents an optimized shortlist of rental units that meet these specific lifestyle needs.

## 2. Target Audience
* **Primary Persona:** International student or newcomer working professional in Singapore.
* **Key Characteristics:** Lacks local geographic and transit knowledge; prioritizes a manageable daily commute and budget over specific postal codes.

## 3. Graphical User Interface (GUI) Requirements
- [ ] **Left Input Panel (Constraints):** Contains text fields, dropdowns, and toggle switches for all user inputs (Destination, Max Rent, Max Commute Time, Max Transfers, Walking Tolerance, Air-Conditioning, Result Limit, and Sort Mode).
- [ ] **Right Display Panel (Results):** A scrollable list displaying the generated shortlist of rental units. Each result row will show the rent, address, aircon status, and a breakdown of the commute summary.
- [ ] **Settings Surface:** Lets users choose a persona preset and dark mode, and preserves those settings for later app launches.

## 4. Functional Requirements (v0.3 Scope)

### 4.1 Destination and Commute Filtering
- [ ] **REQ-1A (Destination Input):** The GUI must provide a dropdown or text field for the user to select their primary destination address (e.g., specific MRT stations or campuses).
- [ ] **REQ-1B (Total Travel Time Cap):** The user must be able to input an acceptable total travel time in minutes. The system will exclude any listings whose pre-calculated commute time to the destination exceeds this cap.

### 4.2 Unit Constraints
- [ ] **REQ-2A (Budget Limit):** The GUI must include a field to set a maximum monthly rent. Listings exceeding this value will not be processed.
- [ ] **REQ-2B (Air-Conditioning):** The GUI must include a checkbox to require air-conditioning.
- [ ] **REQ-2C (Transfer and Walking Caps):** The GUI must include inputs for maximum transfers and maximum walking time.

### 4.3 Ranking, Filtering, and Persistence
- [ ] **REQ-3A (Route Rejection):** The algorithm must implement a sanity rule to reject routes where the walking time ratio is disproportionately high (e.g., walking ratio >= 0.6 of total time), ensuring practical public transport suggestions.
- [ ] **REQ-3B (Result Controls):** The user must be able to set the shortlist size and choose a supported sort mode.
- [ ] **REQ-3C (Preference Persistence):** The application should restore the last successful search preferences locally on startup.
- [ ] **REQ-3D (Settings Persistence):** The application should restore the selected persona preset and dark-mode choice on startup.

### 4.4 Output and Display
- [ ] **REQ-4A (Shortlist Generation):** The system must deterministically output a shortlist of the top N listings (default N=10) that pass all filters.
- [ ] **REQ-4B (Commute Summary):** The GUI must display the commute details for each shortlisted listing, explicitly separating "Transit Time", "Walking Time", and "Transfers".

## 5. Data & Architecture Strategy (JAR Constraints)
- [ ] **Static Listing Database:** Rental unit data will be stored locally within the application package (e.g., a bundled listings.json or listings.csv file containing sample units with attributes like rent, nearest origin node, and aircon availability).
- [ ] **Pre-Calculated Transit Matrix:** Live mapping APIs (like Google Maps) are excluded. The application will bundle a static time-distance matrix mapping travel times between covered origin nodes and supported destinations to simulate realistic routing instantaneously.

## 6. Non-Functional Requirements
- [ ] **Performance:** The filtering algorithm and GUI update must complete the search and display results within 2 seconds of the user clicking "Search."
- [ ] **Portability:** The final product must be fully self-contained within a `.jar` executable, requiring only a standard Java Runtime Environment (JRE) to run on Windows, macOS, or Linux.
- [ ] **Offline Capability:** The core v0.3 functionality must operate entirely without an active internet connection.
