import { useNavigate } from 'react-router-dom';
import './InmateRegistration.css';

const InmateRegistration = () => {
  const navigate = useNavigate();

  return (
    <div className="inmate-registration animate-fade-in">
      <div className="page-header">
        <div>
          <h1 className="page-title">Nouvelle Admission</h1>
          <p className="page-subtitle">Formulaire d'enregistrement pour un nouveau détenu.</p>
        </div>
        <button className="btn-secondary" onClick={() => navigate('/inmates')}>Retour à la liste</button>
      </div>

      <div className="registration-form-container glass-panel">
        <form className="registration-form" onSubmit={(e) => { e.preventDefault(); navigate('/inmates'); }}>
          <div className="form-section">
            <h3>Informations Personnelles</h3>
            <div className="form-grid">
              <div className="form-group">
                <label>Nom</label>
                <input type="text" placeholder="Entrez le nom" required />
              </div>
              <div className="form-group">
                <label>Prénom</label>
                <input type="text" placeholder="Entrez le prénom" required />
              </div>
              <div className="form-group">
                <label>Date de Naissance</label>
                <input type="date" required />
              </div>
              <div className="form-group">
                <label>Sexe</label>
                <select required>
                  <option value="">Sélectionnez...</option>
                  <option value="M">Masculin</option>
                  <option value="F">Féminin</option>
                </select>
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>Dossier Légal</h3>
            <div className="form-grid">
              <div className="form-group full-width">
                <label>Motif d'incarcération</label>
                <input type="text" placeholder="Ex: Vol qualifié" required />
              </div>
              <div className="form-group">
                <label>Niveau de Sécurité</label>
                <select required>
                  <option value="safe">Standard</option>
                  <option value="warning">Surveillance Raprochée</option>
                  <option value="danger">Haute Sécurité</option>
                </select>
              </div>
              <div className="form-group">
                <label>Durée de la peine (Mois)</label>
                <input type="number" min="1" required />
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="btn-secondary" onClick={() => navigate('/inmates')}>Annuler</button>
            <button type="submit" className="btn-primary">Enregistrer l'admission</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default InmateRegistration;
