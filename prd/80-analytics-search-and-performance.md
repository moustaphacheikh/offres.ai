# 80 — Analytics, Search & Performance

This module covers: (a) employee search/query with rich filters and batch actions, and (b) analytics/reporting exports. In legacy, analytics are produced as Excel workbooks, not interactive dashboards.

Ground truth
- Search/filter and batch actions: `ui/EmployequeryFrame.java` (tabs Filtre, Mise à jour, Paie, Décompte) and `util/ModelClass$tmEmployequery.java` (result table model/columns).
- Excel analytics exports: `ui/etats.java` uses `util/WriteExcel.java` to produce workbooks for Etat de paie, Masse salariale, Engagements, Simulation de congés, Liste du personnel, Diplômes.

Out of scope vs new build
- Interactive BI dashboards are not present in legacy. We’ll preserve Excel exports and add lightweight summaries now; dashboards can be added later as new scope.

## Feature: Advanced Employee Search (Selection for batch ops)

Description
- Provide an employee query screen with toggleable conditions and typed inputs. Results render in a selectable table to drive batch updates and operations.

Legacy behavior (authoritative)
- Conditions exist as checkboxes to include/exclude a filter; data is filtered in-memory then sorted by employee id.
	- Filters supported (names approximate Django fields):
		- Actif (is_active) — `cActif` gated by `cCondActif`
		- En congés (on_leave) — `cEnConges` gated by `cCondEnConges`
		- En débauche (in_termination) — `cEnDebauche` gated by `cCondEnDebauche`
		- Embauche between [du, au] — `tEmbaucheDu/Au` gated by `cCondEmbauche`
		- Sortie between [du, au] — `tSortieDu/Au` gated by `cCondSortie`
		- ID range [du, au] — `tIdDu/Au` gated by `cCondID`
		- Départements, Postes, Catégories — combo boxes gated by `cCondDepartement`, `cCondPoste`, `cCondCategorie`
		- Mode de paye, Banque, Domicilié — `cCondModePaie`, `cCondBanque`, `cCondDomicilie`
		- Origine (zone), Sexe, Situation familiale — `cCondOrigine`, `cCondSexe`, `cCondSitFam`
		- Type de contrat — `cCondTypeContrat`
		- Détaché CNSS/CNAM, Exonéré ITS — `cCondCNSS`, `cCondCNAM`, `cCondITS`
- Result columns (from `ModelClass$tmEmployequery`): selectable, color-coded active/leave flags, id, name, sexe, nationalité, origine, situation familiale, date embauche, NJT/SN, département, poste, catégorie, det_CNSS, det_CNAM, exon_ITS, mode_paie, banque, domicilie, type_contrat, date sortie, en_débauche.
- Bulk selection: “Tout cocher” toggles all rows. Table model stores a boolean selection in column 0.
- Batch operations invoked on selection (executed in background threads with a progress bar and confirmations):
	- Mise à jour des attributs (actif, en congés, banque, mode de paye, sexe, sit. fam., contrat, heures/semaine, préavis, zone d’origine, etc.).
	- Paie quick ops: ajouter/supprimer rubrique (base/nombre, fixe), mettre à jour NJT pour un motif, message paie courante.
	- Décompte: congés/sortie avec options (droits congés, indemnités, solder retenues, retraite) et dates.

Target (Django)
- Server-side filtering with the same condition set; AND logic initially (as in legacy). OR groupings are future scope.
- Paginated results (default 50), stable sort by id; CSV/Excel export of current result set.
- Preserve bulk actions by applying operations to the filtered selection (with role checks, dry-run preview, and async execution for long jobs).

Acceptance Criteria
- Filters map 1:1 with legacy conditions and return the same set for the same data.
- “Select all in page” and “select all results” supported; bulk ops confirm and run in the background with progress.
- Exports include all table columns and reflect current filters.

Notes
- Legacy filters are evaluated in-memory from a preloaded list; we will implement DB-backed filters with indexes for performance.

## Feature: Analytics Exports (Management reporting)

Description
- Provide Excel exports equivalent to the legacy “Etats” module with selectable columns and computed totals. These are used as analytics.

Legacy behavior (authoritative)
- Available documents: Etat de paie, Masse salariale, Engagements, Simulation de congés, Liste du personnel, Diplômes/Formations (`ui/etats.java`).
- Column chooser: For Etat de paie, a table of checkboxes allows picking identity/HR columns plus dynamic payroll rubriques for a given period/motif. Columns are rendered in the Excel workbook via `WriteExcel` helpers.
- Key derived metrics and formulas:
	- CNSS MED. = CNSS × 2; CNSS PAT. = CNSS × 13
	- CNAM PAT. = CNAM × 5/4
	- Masse salariale: computes sums across NET, CNSS parts, CNAM parts, BRUT TOTAL; Taxe d’apprentissage = BRUT TOTAL × 0.6%; MASSE SAL. = BRUT TOTAL + taxe
	- Simulation de congés: brut congé ≈ (cumul BI + BNI)/12 with statutory CNSS/CNAM/ITS computations; NET = brut − cnss − cnam − its
- Progress bars shown during generation; files are opened on completion.

Target (Django)
- Implement the same exports server-side (XLSX and CSV). Keep the column chooser for Etat de paie; support FR/AR labels as legacy.
- Long-running exports execute async; user notified with a downloadable artifact when ready.
- Totals and formulas must match legacy for the same dataset.

Acceptance Criteria
- Each export produces an Excel with the same columns, ordering, and totals as legacy, for a given period/motif.
- Column chooser persists user selections per user until changed.
- Large datasets (>10k rows) complete under 2 minutes via background processing.

## Performance and Non-Functional Requirements

Data volume & responsiveness
- Server-side filtering and pagination for list screens; avoid loading entire datasets in memory.
- Streaming CSV for large exports; XLSX generated asynchronously with worker timeouts and retries.

Indexes (minimum)
- Employee: actif, en_congé, en_débauche, date_embauche, date_sortie, departement_id, poste_id, catégorie, mode_paiement, banque_id, origine_id, type_contrat, sexe, situation_familiale, detache_cnss, detache_cnam, exonore_its, id_salarie_pointeuse.
- Payroll items: periode (month), motif_id, employe_id, rubrique_id.

Limits and batching
- UI page size default 50; max 500 per page with server guardrails.
- Bulk ops limited to 5,000 employees per job; jobs chunked in batches of 500 with progress tracking.

Observability
- Log search queries (filters, duration, count) and export jobs (user, params, status).
- Capture slow queries (>500 ms) with SQL and add missing indexes.

## Future Scope (not in legacy, plan as new)
- Interactive dashboards (age/seniority/paye trends, cost centers) with role-based widgets.
- Saved searches with sharing and scheduling.
- Search audit trail UI.

## Dependencies
- Employee, Organization, Payroll, Rubrique modules; Motif, Période; permissions/roles.