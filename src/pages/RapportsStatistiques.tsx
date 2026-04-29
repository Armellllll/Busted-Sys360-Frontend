import './Dashboard.css';

const RapportsStatistiques = () => {
  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <h1 className="page-title">Rapports Statistiques</h1>
        <p className="page-subtitle">Vue globale des métriques de la prison.</p>
      </header>

      <section className="stats-grid">
        <div className="stat-card glass-panel">
          <h3>Taux d'occupation</h3>
          <p className="stat-value">83%</p>
          <div className="progress-bar"><div className="fill" style={{width: '83%'}}></div></div>
        </div>
        <div className="stat-card glass-panel">
          <h3>Infractions mensuelles</h3>
          <p className="stat-value">24</p>
          <span className="stat-change negative">+5 ce mois</span>
        </div>
      </section>
    </div>
  );
};
export default RapportsStatistiques;
