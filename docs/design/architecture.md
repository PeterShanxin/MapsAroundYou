# Architecture Overview

**Smart Rental Search Algorithm**

## Design Decision

**Local data only** for destinations, commute-time lookup, and listings. No live APIs, geocoding, or real-time scraping.

---

## Component View

![Architecture — Component Overview](diagrams/architecture-component.svg)

*Figure: Layer and dependency overview. See the Developer Guide's "Architecture & Coupling" section for the full narrative.*

---

## Components

### UI (GUI)

- Collects inputs from user
- Displays ranked results
- Restores last-used preferences on startup
- Displays listing details + split commute breakdown (total, transit, walk, transfers, fare)
- Persists persona preset and dark-mode settings

### Logic

- Sets up the search pipeline
- Exposes UI-friendly operations
- See [API Spec](../api/api-spec.md) for operations

### Services

| Service | Responsibility |
|---------|----------------|
| **CommuteEstimator** | Local travel-time lookup between listing origin nodes and selected destinations |
| **ListingFilter** | Rent/time constraints, aircon filter |
| **ListingRanker** | Scoring plus selectable sorting |
| **RouteAnalyzer** | Walk-dominant detection and commute breakdown |

### Model

- Entities: `Listing`, `Destination`, `Preferences`, `Results`
- Immutable-ish; lightweight DTOs between layers

### Storage

- Loads local datasets: destinations, travel times, listings
- Persists last-used preferences for improved UX

---

## Data Flow

1. **User input** → UI captures destination, rent, commute, transfer cap, walking cap, aircon, result count, sort mode, and walk-dominant toggle
2. **Logic** loads data from Storage, invokes Services via pipeline
3. **ListingFilter** → **CommuteEstimator** → **RouteAnalyzer** → **ListingRanker** → ranked results
4. **UI** renders `SearchResultViewModel[]`

---

## Related Documents

- [Software Design Document](./sdd.md)
- [API Spec](../api/api-spec.md)
- [Mock API / Data Schemas](../api/mock-api.md)
