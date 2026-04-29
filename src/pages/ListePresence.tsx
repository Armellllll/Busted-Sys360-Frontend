import './Dashboard.css';

const ListePresence = () => {
  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <h1 className="page-title">Liste de présence</h1>
        <p className="page-subtitle">Vérification de la présence des détenus.</p>
      </header>
      
      <div className="dashboard-widgets">
        <div className="widget glass-panel">
          <h3 className="widget-title">Appel du jour</h3>
          <p>Le registre d'appel sera affiché ici.</p>
        </div>
      </div>
    </div>
  );
};
export default ListePresence;
