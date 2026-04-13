# Data Generation Guide

This guide explains how to add new rental listings to MapsAroundYou using the data generation workflow.

## Workflow Overview

The application maintains a two-layer CSV data model:

1. **`origin_nodes.csv`** - Origin identity and commute reference
   - Source file that defines where rentals are located
   - Used as reference data for listings and transit matrix alignment
   - Treat each row as one covered origin node, not one app listing row
   - Columns: `Flat_ID`, `Postal_Code`, `Region`, `Area_Name`

2. **`listings.csv`** - App-facing listing data
   - Auto-generated from `origin_nodes.csv` using the data generation script
   - Contains all fields needed by the CLI app for display and filtering
   - Multiple listing rows may share the same `originNodeId`
   - Columns: `listingId`, `title`, `monthlyRent`, `hasAircon`, `originNodeId`, `address`, `roomType`, `sourcePlatform`, `notes`

3. **`transit_matrix.csv`** - Commute lookup matrix
   - Pre-computed travel times from each origin to destinations
   - Aligned with `origin_nodes.csv` via `flat_id` foreign key
   - Columns: `flat_id`, `destination_id`, `pt_total`, `pt_walk`, `pt_bus`, `pt_rail`, `pt_transit`, `pt_transfers`, `pt_fare`, ... (and other transit metrics)

## Adding New Listings

### Prerequisites

- Python 3 on `PATH`
- See [Build and Run Guide](../ops/build-and-run.md) for the broader local setup
### Step 1: Add Origins to `origin_nodes.csv`

Open `src/main/resources/commute_data/origin_nodes.csv` and add new rows:

```csv
Flat_ID,Postal_Code,Region,Area_Name
R05,200150,East,Bedok / Near Bedok MRT
R06,200160,East,Bedok / Opposite Shopping Mall
```

**Important:** The `Flat_ID` column in `origin_nodes.csv` becomes the `originNodeId` in `listings.csv`.

### Step 2: Generate App-Facing Listings

Run the unified data generation script from the workspace root:

```bash
python scripts/generate_merged_listings.py --location-mode manual
```

This will:
- Add new origin rows to `origin_nodes.csv`
- Generate app-facing listing rows in `listings.csv`
- Append matching transit rows in `transit_matrix.csv`
- Keep all three datasets aligned by `Flat_ID` / `originNodeId`

**Optional arguments:**

```bash
# Randomly generate 6 new covered origins
python scripts/generate_merged_listings.py --location-mode random --new-origin-count 6

# Control how many listings each new origin gets
python scripts/generate_merged_listings.py --listings-per-origin 4 --seed 2103
```

### Step 3: Expand `transit_matrix.csv`

The unified generator appends the matching `transit_matrix.csv` rows for every
new `Flat_ID`. If you need to inspect or edit the expected format manually, it
looks like this:

```csv
flat_id,destination_id,pt_total,pt_walk,pt_bus,pt_rail,pt_transit,pt_transfers,pt_fare,drive_total,cycle_total,walk_total
R05,D01,35,8,20,0,20,1,1.80,15,50,75
R05,D02,60,15,18,20,38,2,2.40,30,160,220
R05,D03,30,5,10,8,18,1,1.75,13,60,70
...
```

**Important:** The `flat_id` values in `transit_matrix.csv` MUST match the `Flat_ID` values in `origin_nodes.csv`.

## Data Format Details

### CSV Field Mappings

| origin_nodes.csv | → | listings.csv |
|---|---|---|
| `Flat_ID` | → | `originNodeId` |
| (generated) | → | `listingId` (L001, L002, ...) |
| (generated) | → | `title` (descriptive, auto-generated) |
| `Postal_Code` | → | `address` (fetched from OneMap API) |
| (generated) | → | `monthlyRent` (random, price-bracket appropriate) |
| (generated) | → | `hasAircon` (boolean: 75% true, 25% false) |
| (generated) | → | `roomType` (e.g., "Condo room", "HDB room") |
| (generated) | → | `sourcePlatform` ("PropertyGuru" or "99.co") |
| (generated) | → | `notes` (e.g., "Curated demo listing") |

### hasAircon Field

- **Type:** Boolean CSV value (`true` / `false`)
- **NOT a nested JSON field** (unlike earlier designs)
- **Direct column in listings.csv** for simple filtering

### One Origin, Multiple Units

- `origin_nodes.csv` tracks covered commute origins, not the final unit inventory
- The generator can therefore emit multiple listings for the same `originNodeId`
- This lets the demo app show richer filter results without regenerating the commute matrix

## Troubleshooting

### CSV Encoding Issues
The script reads `origin_nodes.csv` with UTF-8 BOM support and writes append
operations using UTF-8 output.

## Key Design Principles

- **No Rental_List2.csv at runtime** - The intermediate file from data generation is not used by the app
- **Two-layer decoupling** - `origin_nodes.csv` (identity) stays separate from `listings.csv` (presentation)
- **Repo-portable** - All paths are relative to the repository root
- **Flat_ID alignment** - All three CSVs reference listings by their origin `Flat_ID` (or `originNodeId`)

## See Also

- [API Schema Documentation](../api/mock-api.md) - Full schema details
- [Build and Run Guide](../ops/build-and-run.md) - How to run the CLI application
