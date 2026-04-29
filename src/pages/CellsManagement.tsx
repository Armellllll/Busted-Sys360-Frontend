import './CellsManagement.css';

const CellsManagement = () => {
  const cellBlocks = [
    { block: 'A', total: 50, occupied: 48, status: 'warning' },
    { block: 'B', total: 50, occupied: 30, status: 'safe' },
    { block: 'C', total: 20, occupied: 20, status: 'danger' }, // Haute Sécurité
  ];

  return (
    <div className="cells-management animate-fade-in">
      <div className="page-header">
        <div>
          <h1 className="page-title font-roboto">Gestion des Cellules</h1>
          <p className="page-subtitle">Occupation et statut des blocs d'hébergement.</p>
        </div>
        <div className="header-actions">
          <button className="btn-secondary">Plan de la prison</button>
          <button className="btn-primary">+ Nouvelle affectation</button>
        </div>
      </div>

      <div className="blocks-overview">
        {cellBlocks.map((b) => {
          const percentage = Math.round((b.occupied / b.total) * 100);
          return (
            <div key={b.block} className="card block-card">
              <div className="block-header">
                <h2 className="font-roboto">Bloc {b.block}</h2>
                <span className={`badge-status status-${b.status}`}>
                  {percentage}% Plein
                </span>
              </div>
              <div className="block-stats">
                <div className="stat">
                  <span className="stat-value font-mono">{b.occupied}</span>
                  <span className="stat-label">Détenus</span>
                </div>
                <div className="stat text-right">
                  <span className="stat-value font-mono">{b.total - b.occupied}</span>
                  <span className="stat-label">Places Libres</span>
                </div>
              </div>
              <div className="progress-bar-container">
                <div className={`progress-fill bg-${b.status}`} style={{ width: `${percentage}%` }}></div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="card cells-grid">
        <div className="grid-header">
          <h3 className="font-roboto">Statut détaillé par cellule (Bloc A)</h3>
          <div className="cell-filters">
            <select>
              <option>Tous les niveaux</option>
              <option>Niveau 1</option>
              <option>Niveau 2</option>
            </select>
          </div>
        </div>
        
        <div className="cells-map">
          {[...Array(12)].map((_, i) => (
            <div key={i} className={`cell-box ${i < 11 ? 'occupied' : 'empty'}`}>
              <span className="cell-number font-mono">A-{i + 1}</span>
              <span className="cell-status">
                {i < 11 ? 'Occupé (2/2)' : 'Libre (0/2)'}
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CellsManagement;
