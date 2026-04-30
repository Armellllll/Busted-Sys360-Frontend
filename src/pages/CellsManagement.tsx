import { useState, useMemo } from 'react';
import { useInmatesData } from '../context/InmatesDataContext';
import './CellsManagement.css';

type Level = 1 | 2;

interface CellInfo {
  id: string;       // e.g. "A-101"
  block: string;
  level: Level;
  capacity: number;
  occupants: string[]; // inmate IDs
}

/** Generate the full cell plan: Bloc A (L1+L2), Bloc B (L1+L2), Bloc C (L1) */
function buildCellPlan(): CellInfo[] {
  const cells: CellInfo[] = [];
  const addBlock = (block: string, levels: Level[], perLevel: number, cap = 2) => {
    levels.forEach(level => {
      for (let i = 1; i <= perLevel; i++) {
        cells.push({ id: `${block}-${level}${String(i).padStart(2,'0')}`, block, level, capacity: cap, occupants: [] });
      }
    });
  };
  addBlock('A', [1, 2], 12); // 24 cells, 48 places
  addBlock('B', [1, 2], 12); // 24 cells, 48 places
  addBlock('C', [1],    10, 2); // 10 cells, 20 places (haute sécurité)
  return cells;
}

const CellsManagement = () => {
  const { inmates, setInmates } = useInmatesData();
  const [selectedBlock, setSelectedBlock] = useState<'A' | 'B' | 'C'>('A');
  const [selectedLevel, setSelectedLevel] = useState<Level | 'all'>('all');

  // Build cell plan and auto-assign inmates
  const cellPlan = useMemo(() => {
    const plan = buildCellPlan();
    // Assign each inmate (with a cellule) to its cell
    inmates.forEach(inmate => {
      if (inmate.cellule && inmate.cellule !== 'Non assignée') {
        const cell = plan.find(c => c.id === inmate.cellule);
        if (cell) cell.occupants.push(inmate.id);
      }
    });
    return plan;
  }, [inmates]);

  /** Auto-assign all unassigned inmates to first available cell */
  const handleAutoAssign = () => {
    const unassigned = inmates.filter(i => !i.cellule || i.cellule === 'Non assignée');
    if (unassigned.length === 0) return;

    const updatedPlan = buildCellPlan();
    // Mark existing occupants
    inmates.forEach(inmate => {
      if (inmate.cellule && inmate.cellule !== 'Non assignée') {
        const cell = updatedPlan.find(c => c.id === inmate.cellule);
        if (cell) cell.occupants.push(inmate.id);
      }
    });

    const assignments: Record<string, string> = {};
    unassigned.forEach(inmate => {
      // Pick first cell with available space (not in bloc C for standard security)
      const target = updatedPlan.find(c =>
        c.occupants.length < c.capacity &&
        (inmate.releaseStatus !== 'danger' ? c.block !== 'C' : c.block === 'C')
      );
      if (target) {
        target.occupants.push(inmate.id);
        assignments[inmate.id] = target.id;
      }
    });

    setInmates(inmates.map(i => assignments[i.id] ? { ...i, cellule: assignments[i.id] } : i));
  };

  const blocStats = useMemo(() => {
    return ['A', 'B', 'C'].map(block => {
      const blockCells = cellPlan.filter(c => c.block === block);
      const total = blockCells.reduce((s, c) => s + c.capacity, 0);
      const occupied = blockCells.reduce((s, c) => s + c.occupants.length, 0);
      const pct = Math.round((occupied / total) * 100);
      const status = pct >= 100 ? 'danger' : pct >= 80 ? 'warning' : 'safe';
      return { block, total, occupied, free: total - occupied, pct, status };
    });
  }, [cellPlan]);

  const displayedCells = cellPlan.filter(c =>
    c.block === selectedBlock && (selectedLevel === 'all' || c.level === selectedLevel)
  );

  const hasUnassigned = inmates.some(i => !i.cellule || i.cellule === 'Non assignée');

  return (
    <div className="cells-management animate-fade-in">
      <div className="page-header">
        <div>
          <h1 className="page-title font-roboto">Gestion des Cellules</h1>
          <p className="page-subtitle">Occupation et affectation automatique des blocs.</p>
        </div>
        <div className="header-actions" style={{ gap: '10px' }}>
          {hasUnassigned && (
            <button className="btn-primary" onClick={handleAutoAssign} style={{ background: '#22c55e' }}>
              ⚡ Affectation automatique ({inmates.filter(i => !i.cellule || i.cellule === 'Non assignée').length} non assigné(s))
            </button>
          )}
          <button className="btn-secondary">Plan de la prison</button>
        </div>
      </div>

      {/* Block summary cards */}
      <div className="blocks-overview">
        {blocStats.map(b => (
          <div
            key={b.block}
            className={`card block-card ${selectedBlock === b.block ? 'selected-block' : ''}`}
            onClick={() => setSelectedBlock(b.block as 'A' | 'B' | 'C')}
            style={{ cursor: 'pointer', border: selectedBlock === b.block ? '2px solid #3b82f6' : '2px solid transparent' }}
          >
            <div className="block-header">
              <h2 className="font-roboto">Bloc {b.block}{b.block === 'C' ? ' 🔒' : ''}</h2>
              <span className={`badge-status status-${b.status}`}>{b.pct}% Plein</span>
            </div>
            <div className="block-stats">
              <div className="stat">
                <span className="stat-value font-mono">{b.occupied}</span>
                <span className="stat-label">Détenus</span>
              </div>
              <div className="stat text-right">
                <span className="stat-value font-mono">{b.free}</span>
                <span className="stat-label">Places libres</span>
              </div>
            </div>
            <div className="progress-bar-container">
              <div className={`progress-fill bg-${b.status}`} style={{ width: `${b.pct}%` }} />
            </div>
          </div>
        ))}
      </div>

      {/* Detailed cell grid */}
      <div className="card cells-grid">
        <div className="grid-header">
          <h3 className="font-roboto">Cellules — Bloc {selectedBlock}</h3>
          <div className="cell-filters">
            <select value={selectedLevel} onChange={e => setSelectedLevel(e.target.value === 'all' ? 'all' : Number(e.target.value) as Level)}>
              <option value="all">Tous les niveaux</option>
              <option value={1}>Niveau 1</option>
              {selectedBlock !== 'C' && <option value={2}>Niveau 2</option>}
            </select>
          </div>
        </div>

        <div className="cells-map">
          {displayedCells.map(cell => {
            const isFull  = cell.occupants.length >= cell.capacity;
            const isEmpty = cell.occupants.length === 0;
            const status  = isFull ? 'occupied' : isEmpty ? 'empty' : 'partial';
            const occupantNames = cell.occupants
              .map(id => inmates.find(i => i.id === id))
              .filter(Boolean)
              .map(i => `${i!.lastName} ${i!.firstName}`)
              .join(', ');

            return (
              <div key={cell.id} className={`cell-box ${status}`} title={occupantNames || 'Libre'}>
                <span className="cell-number font-mono">{cell.id}</span>
                <span className="cell-status" style={{ fontSize: '0.7rem' }}>
                  {cell.occupants.length}/{cell.capacity} — {isEmpty ? 'Libre' : isFull ? 'Plein' : 'Partiel'}
                </span>
                {occupantNames && (
                  <span style={{ fontSize: '0.65rem', opacity: 0.75, display: 'block', marginTop: '2px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {occupantNames}
                  </span>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default CellsManagement;
