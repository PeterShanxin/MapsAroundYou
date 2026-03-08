<<<<<<< HEAD
# Mock API & Data Generation Guide

This guide outlines the workflow for generating the offline travel-time database (`transit_matrix.csv`) used by the **MapsAroundYou** application. To ensure our Java application remains a lightweight, offline-capable JAR file, we pre-generate all routing data using a Python script connected to the OneMap Singapore API.

---

## ⚠️ 1. Critical: The 3-Day API Token Refresh
The OneMap API requires token-based authentication. **Tokens expire automatically every 3 days.** If you attempt to run the data generation script and receive an `{"message":"Unauthorized"}` error, your token has expired.

**How to Refresh the Token:**
1. Go to the [OneMap API Documentation Portal](https://www.onemap.gov.sg/apidocs/).
2. Log in with your registered account credentials.
3. Generate a new Authentication Token.
4. Open the [`Generate_TravelData.py`](./Generate_TravelData.py) script.
5. Replace the string in the `ONEMAP_TOKEN` variable with your new token. 
   *(Note: Do not include the word "Bearer", just paste the raw token string).*

---

## 2. File Overview

The data generation pipeline relies on three main files to produce the final database.

### [`Rental_List.csv`](./Rental_List.csv) (Input)
This file contains our mock database of 50 realistic rental locations, distributed fairly across Singapore's 5 main regions.
* `Flat_ID`: Unique identifier for the rental unit (e.g., `R01`).
* `Postal_Code`: 6-digit Singapore postal code.
* `Region`: General area (Central, East, North, North-East, West).
* `Area_Name`: Specific neighborhood (e.g., Bukit Merah).

### `Dst_List.csv`](./Dst_List.csv) (Input)
This file contains our 10 realistic destination hubs based on our target personas (Students, CBD workers, Industrial engineers, etc.).
* `ID`: Unique identifier for the destination (e.g., `D01`).
* `Category`: Type of destination (e.g., University, CBD Office).
* `Location Name`: Name of the landmark.
* `Postal Code`: 6-digit destination postal code.

### [`Generate_TravelData.py`](./Generate_TravelData.py) (Script)
The Python script that reads the two input CSVs, translates the postal codes into Latitude/Longitude using OneMap's Search API, and then queries the OneMap Routing API to calculate travel times for four modes: Public Transport, Driving, Cycling, and Walking.

---

## 3. How to Generate the Data

Ensure you have Python 3 installed along with the `requests` library (`pip install requests`).

1. Ensure [`Rental_List.csv`](./Rental_List.csv), [`Dst_List.csv`](./Dst_List.csv), and [`Generate_TravelData.py`](./Generate_TravelData.py) are all in the same folder.
2. Verify your `ONEMAP_TOKEN` is up-to-date (less than 3 days old).
3. Open your terminal or command prompt in that folder.
4. Run the script:
   ```bash
   python Generate_TravelData.py
   ```
## 4. Output: `transit_matrix.csv`

Once the script completes successfully, it generates `transit_matrix.csv`. This file acts as the offline relational database for our Java application, utilizing a "Wide Format" to store all transit modes efficiently in a single row per route.

**Data Dictionary:**

| Column Name | Description |
| :--- | :--- |
| `flat_id` | Foreign key matching `Flat_ID` in `Rental_List.csv`. |
| `destination_id` | Foreign key matching `ID` in `Dst_List.csv`. |
| `pt_total` | Total travel time via public transport in minutes. |
| `pt_walk` | Walking time in minutes during the public transport commute (e.g., walking to/from stations). |
| `pt_bus` | Time spent specifically on buses in minutes. |
| `pt_rail` | Time spent specifically on MRT/LRT in minutes. |
| `pt_transit` | Total time spent inside moving public transit vehicles in minutes. |
| `pt_fare` | Estimated public transport fare in SGD. |
| `drive_total` | Total driving time by car in minutes. |
| `cycle_total` | Total cycling time in minutes. |
| `walk_total` | Total walking time for the entire journey in minutes. |

---

## 5. Troubleshooting

* **`-1` Values in Output:** If a row contains `-1` for times, it means the API could not find a valid route between those two coordinates. Check if the postal codes in your input CSVs are valid.
* **`Unauthorized` Errors:** Your OneMap token has expired. See Section 1.
* **Connection Errors / Rate Limiting:** If the script crashes midway, you may be hitting the API too fast. Increase the `time.sleep(0.3)` values inside `Generate_TravelData.py` to `0.5` or `1.0`.
=======
# Mock API / Local Data Schemas

**Smart Rental Search Algorithm**

The application uses **local data files** (JSON/CSV) instead of live APIs. This document describes the data schemas and structures for mock/demo datasets.

---

## Data Sources

| Repository | File | Description |
|------------|------|-------------|
| `StationRepository` | stations file | MRT stations |
| `TransitGraphRepository` | edges file | Transit graph edges (station-to-station travel times) |
| `ListingRepository` | listings file | Rental listings |
| `UserPrefsRepository` | (optional) | Persisted user preferences |

---

## Schema Definitions

### Stations

| Field | Type | Description |
|-------|------|-------------|
| `stationId` | String | Unique identifier (e.g., MRT station code) |
| `name` | String | Display name |
| `lines` | List&lt;String&gt; | MRT lines (e.g., "NS", "EW"); values should be unique |

**Example (JSON):**

```json
{
  "stationId": "NS1",
  "name": "Jurong East",
  "lines": ["NS", "EW"]
}
```

### Edges (Transit Graph)

| Field | Type | Description |
|-------|------|-------------|
| `fromStationId` | String | Origin station |
| `toStationId` | String | Destination station |
| `travelMinutes` | int | Travel time in minutes |
| `line` | String | MRT line for this segment |

**Example (JSON):**

```json
{
  "fromStationId": "NS1",
  "toStationId": "NS2",
  "travelMinutes": 2,
  "line": "NS"
}
```

### TransitGraph

- Structure: `adj: Map<String, List<Edge>>`
- Adjacency list representation for Dijkstra shortest path

---

### Rental Listings

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `listingId` | String | Yes | Unique identifier |
| `title` | String | Yes | Listing title |
| `monthlyRent` | int | Yes | Monthly rent (SGD) |
| `hasAircon` | boolean | Yes | Has air conditioning |
| `nearestStationId` | String | Yes | Closest MRT station |
| `address` | String | No | Full address |
| `roomType` | String | No | e.g., "HDB", "Condo" |
| `notes` | String | No | Additional notes |

**Example (JSON):**

```json
{
  "listingId": "L001",
  "title": "Cozy room near Jurong East",
  "monthlyRent": 1200,
  "hasAircon": true,
  "nearestStationId": "NS1",
  "address": "123 Jurong Street",
  "roomType": "HDB"
}
```

---

### User Preferences (Optional Persistence)

| Field | Type | Description |
|-------|------|-------------|
| `destinationStationId` | String | Primary destination MRT station |
| `maxRent` | int | Max monthly rent filter |
| `maxCommuteMinutes` | int | Max commute time (minutes) |
| `requireAircon` | boolean | Require aircon |
| `transportMode` | enum | MVP default: MRT |

---

## Validation Requirements

- Schema must be validated on load
- Invalid or missing fields should produce clear load errors
- Use a curated demo dataset for development and testing

---

## Data Freshness

Since the app uses local data only:

> Display a label such as *"Accurate as of 2025-01-15"*, where the date is derived from the dataset metadata and formatted as ISO 8601 (YYYY-MM-DD).

---

## Related Documents

- [Software Design Document](../design/sdd.md)
- [Architecture Overview](../design/architecture.md)
- [API Spec](./api-spec.md)
>>>>>>> fd4c4159dfb335b162d9b6cec313e197847a7bcb
