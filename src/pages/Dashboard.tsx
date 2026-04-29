import './Dashboard.css';

const Dashboard = () => {
  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <h1 className="page-title font-roboto">Tableau de bord</h1>
        <p className="page-subtitle">Vue d'ensemble en temps réel.</p>
      </header>

      <section className="stats-grid">
        <div className="stat-card card">
          <h3>Total Détenus</h3>
          <p className="stat-value font-mono">1 245</p>
          <span className="stat-change positive">+12 cette semaine</span>
        </div>
        <div className="stat-card card">
          <h3>Taux d'occupation</h3>
          <p className="stat-value font-mono">83%</p>
          <div className="progress-bar"><div className="fill" style={{width: '83%'}}></div></div>
        </div>
        <div className="stat-card card">
          <h3>Personnel de garde</h3>
          <p className="stat-value font-mono">142</p>
          <span className="stat-change neutral">Service normal</span>
        </div>
        <div className="stat-card card alert">
          <h3>Alertes Actives</h3>
          <p className="stat-value font-mono text-danger">2</p>
          <span className="stat-change negative">Action requise</span>
        </div>
      </section>
      
      <div className="dashboard-widgets">
        <div className="widget card">
          <h3 className="widget-title font-roboto">Activité Récente</h3>
          <div className="activity-list">
            <div className="activity-item">
              <div className="activity-dot"></div>
              <div className="activity-content">
                <p>Transfert finalisé pour le Détenu #4592</p>
                <span className="activity-time">Il y a 10 min</span>
              </div>
            </div>
            <div className="activity-item">
              <div className="activity-dot warning"></div>
              <div className="activity-content">
                <p>Altération mineure signalée Bloc C</p>
                <span className="activity-time">Il y a 45 min</span>
              </div>
            </div>
            <div className="activity-item">
              <div className="activity-dot"></div>
              <div className="activity-content">
                <p>Changement de service: Équipe Alpha déployée</p>
                <span className="activity-time">Il y a 2 heures</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Dashboard;
