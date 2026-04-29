import './Dashboard.css';

const RapportsPersonnel = () => {
  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <h1 className="page-title">Rapports du Personnel</h1>
        <p className="page-subtitle">Gestion et suivi des ressources humaines.</p>
      </header>
      
      <div className="dashboard-widgets">
        <div className="widget glass-panel">
          <h3 className="widget-title">Effectif Actif</h3>
          <p>Les données du personnel seront affichées ici.</p>
        </div>
      </div>
    </div>
  );
};
export default RapportsPersonnel;
