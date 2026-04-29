import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { ShieldAlert, CheckCircle, Activity } from 'lucide-react';
import './InfractionDeclaration.css';

const InfractionDeclaration = () => {
  const { selectedInmate } = useSelectedInmate();
  const navigate = useNavigate();
  
  const [infractionType, setInfractionType] = useState('Bagarre');
  const [severity, setSeverity] = useState('Moyenne');
  const [description, setDescription] = useState('');
  const [step, setStep] = useState(1);

  if (!selectedInmate) {
    return (
      <div className="empty-state-container">
        <h2>Aucun Détenu Sélectionné</h2>
        <p>Sélectionnez un détenu dans la liste de gauche pour déclarer une infraction.</p>
      </div>
    );
  }

  const handleValidation = () => {
    // Sequence Diagram Logic Simulated here
    console.log("Transmission des données via la couche réseau sécurisée...");
    console.log("Calcul automatique de la sanction en cours...");
    setStep(2);
  };

  const confirmInfraction = () => {
    console.log("Enregistrement en base de données...");
    console.log("Fiche détenu mise à jour...");
    setStep(3);
  };

  return (
    <div className="infraction-declaration animate-fade-in">
      <header className="page-header space-between" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 className="page-title font-roboto">Infractions & Incidents</h1>
          <p className="page-subtitle">Déclaration et traitement automatisé.</p>
        </div>
        <button 
          className="btn-danger" 
          onClick={() => navigate('/medical')}
          style={{ background: '#C0392B', color: 'white', padding: '0.5rem 1rem', borderRadius: '6px', border: 'none', display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', fontWeight: 'bold' }}
        >
          <Activity size={18} /> Déclarer Urgence Médicale
        </button>
      </header>

      {/* Résumé du détenu */}
      <div className="card inmate-summary" style={{ display: 'flex', gap: '15px', padding: '15px', marginBottom: '20px', alignItems: 'center', borderLeft: '4px solid #2E6DA4' }}>
         <div className="summary-photo" style={{ width: '60px', height: '60px', backgroundColor: '#333', borderRadius: '50%' }}></div>
         <div>
            <h3 style={{ margin: '0 0 5px 0', color: 'var(--brand-accent)' }}>{selectedInmate.lastName}, {selectedInmate.firstName}</h3>
            <p style={{ margin: 0, color: '#555', fontSize: '0.9rem' }}>Cellule: A-12 | Statut: {selectedInmate.releaseStatus.toUpperCase()}</p>
         </div>
      </div>

      <div className="card declaration-form">
        {step === 1 && (
          <div className="form-section animate-fade-in">
            <h3 className="font-roboto">Nouvelle Déclaration</h3>
            
            <div className="form-grid">
              <div className="input-group full-width">
                <label>Détenu concerné (Automatique)</label>
                <input type="text" value={`#${selectedInmate.id} - ${selectedInmate.firstName} ${selectedInmate.lastName}`} disabled />
              </div>

              <div className="input-group">
                <label>Type d'incident</label>
                <select value={infractionType} onChange={(e) => setInfractionType(e.target.value)}>
                  <option>Bagarre</option>
                  <option>Contrebande détectée</option>
                  <option>Dégradation de matériel</option>
                  <option>Insubordination</option>
                </select>
              </div>

              <div className="input-group">
                <label>Gravité estimée</label>
                <select value={severity} onChange={(e) => setSeverity(e.target.value)}>
                  <option>Faible</option>
                  <option>Moyenne</option>
                  <option>Élevée</option>
                  <option>Critique</option>
                </select>
              </div>

              <div className="input-group full-width">
                <label>Description des faits</label>
                <textarea 
                  rows={4} 
                  placeholder="Décrivez précisément l'incident..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="btn-primary" onClick={handleValidation}>
                Valider et Évaluer la Sanction
              </button>
            </div>
          </div>
        )}

        {step === 2 && (
          <div className="form-section animate-fade-in">
            <h3 className="font-roboto">Proposition de Sanction (Automatisée)</h3>
            
            <div className="sanction-preview">
              <div className="preview-header">
                <ShieldAlert size={24} />
                <h4>Isolement Disciplinaire</h4>
                <span className="badge-severity elevee">Haute</span>
              </div>
              <div className="preview-content">
                <p><strong>Durée calculée :</strong> 5 jours</p>
                <p><strong>Impacts :</strong> Suspension temporaire des droits de visite non-obligatoires.</p>
                <p className="notification-text">Une notification sera automatiquement envoyée au Directeur pour visa.</p>
              </div>
            </div>

            <div className="form-actions" style={{ gap: '1rem', display: 'flex' }}>
              <button className="btn-secondary" onClick={() => setStep(1)}>Modifier la déclaration</button>
              <button className="btn-primary" onClick={confirmInfraction} style={{ background: 'var(--status-danger)', border: 'none' }}>
                Confirmer et Appliquer
              </button>
            </div>
          </div>
        )}

        {step === 3 && (
          <div className="success-panel animate-fade-in">
            <CheckCircle className="success-icon" size={64} />
            <h2>Infraction Enregistrée</h2>
            <p>Le dossier du détenu a été mis à jour.</p>
            <p>La sanction est applicable immédiatement selon le protocole de sécurité en vigueur.</p>
            <button className="btn-primary" style={{ marginTop: '2rem' }} onClick={() => setStep(1)}>
              Saisir une nouvelle déclaration
            </button>
          </div>
        )}
      </div>

      {/* Historique Infractions */}
      <div className="card" style={{ padding: '20px', marginTop: '20px' }}>
         <h3 style={{ color: 'var(--brand-accent)', borderBottom: '1px solid #CCC', paddingBottom: '10px' }}>Historique des infractions récentes</h3>
         <ul style={{ listStyle: 'none', padding: 0 }}>
            <li style={{ padding: '10px 0', borderBottom: '1px solid #EEE' }}>
               <strong style={{ color: '#444' }}>10 Fév 2026</strong> - Refus d'obtempérer (Confinement 24h)
            </li>
         </ul>
      </div>

    </div>
  );
};

export default InfractionDeclaration;
