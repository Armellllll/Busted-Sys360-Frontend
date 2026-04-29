import './Dashboard.css';

const Notifications = () => {
  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <h1 className="page-title">Centre de Notifications</h1>
        <p className="page-subtitle">Toutes les alertes et notifications du système.</p>
      </header>
      
      <div className="dashboard-widgets">
        <div className="widget glass-panel">
          <h3 className="widget-title">Alertes Récentes</h3>
          <div className="activity-list">
            <div className="activity-item">
              <div className="activity-dot warning"></div>
              <div className="activity-content">
                <p>Transfert urgent demandé (Cellule C4)</p>
                <span className="activity-time">Il y a 5 min</span>
              </div>
            </div>
            <div className="activity-item">
              <div className="activity-dot"></div>
              <div className="activity-content">
                <p>Rapport d'infraction soumis par Agent Dupont</p>
                <span className="activity-time">Il y a 1h</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Notifications;
