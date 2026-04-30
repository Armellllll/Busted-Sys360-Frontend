import { useState } from 'react';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { useInmatesData } from '../context/InmatesDataContext';
import { Activity, Calendar } from 'lucide-react';
import './MedicalDashboard.css';

const MedicalDashboard = () => {
  const { selectedInmate } = useSelectedInmate();
  const { addMedicalRequest, medicalRequests } = useInmatesData();
  const [mode, setMode] = useState<'rdv' | 'urgence'>('rdv');
  const [severity, setSeverity] = useState<'basse' | 'moyenne' | 'haute' | 'critique'>('moyenne');
  const [reason, setReason] = useState('');
  const [submitted, setSubmitted] = useState(false);

  if (!selectedInmate) {
    return (
      <div className="empty-state-container">
        <h2>Aucun Détenu Sélectionné</h2>
        <p>Sélectionnez un détenu dans la liste de gauche pour accéder à son dossier médical.</p>
      </div>
    );
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    addMedicalRequest({
      inmateId: selectedInmate.id,
      inmateName: `${selectedInmate.firstName} ${selectedInmate.lastName}`,
      type: mode,
      reason: reason,
      severity: mode === 'urgence' ? severity : 'basse'
    });

    setSubmitted(true);
    setTimeout(() => {
      setSubmitted(false);
      setReason('');
    }, 3000);
  };

  return (
    <div className="medical-dashboard animate-fade-in" style={{ padding: '0' }}>
      <header className="page-header" style={{ marginBottom: '1.5rem' }}>
        <h1 className="page-title font-roboto">Espace Médical</h1>
        <p className="page-subtitle">Dossier de {selectedInmate.firstName} {selectedInmate.lastName}</p>
      </header>

      {/* Boutons d'action principaux */}
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button 
          onClick={() => setMode('rdv')}
          style={{ 
            flex: 1, 
            padding: '1rem', 
            borderRadius: '8px', 
            border: mode === 'rdv' ? '2px solid #2E6DA4' : '1px solid var(--border-color)',
            backgroundColor: mode === 'rdv' ? 'rgba(46, 109, 164, 0.1)' : 'var(--bg-card)',
            color: '#A0C4FF',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            fontWeight: 'bold',
            fontSize: '1.1rem',
            cursor: 'pointer'
          }}
        >
          <Calendar /> Demander un Rendez-vous
        </button>
        <button 
          onClick={() => setMode('urgence')}
          style={{ 
            flex: 1, 
            padding: '1rem', 
            borderRadius: '8px', 
            border: mode === 'urgence' ? '2px solid #C0392B' : '1px solid var(--border-color)',
            backgroundColor: mode === 'urgence' ? 'rgba(192, 57, 43, 0.1)' : 'var(--bg-card)',
            color: '#FFADAD',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            fontWeight: 'bold',
            fontSize: '1.1rem',
            cursor: 'pointer'
          }}
        >
          <Activity /> Déclarer une Urgence
        </button>
      </div>

      {/* Formulaire Contextuel */}
      <div className="card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h3 style={{ color: mode === 'urgence' ? '#FFADAD' : 'var(--brand-accent)', borderBottom: '1px solid var(--border-color)', paddingBottom: '10px', marginBottom: '20px' }}>
          {mode === 'urgence' ? 'Formulaire d\'Urgence Médicale' : 'Planification de Rendez-vous Régulier'}
        </h3>
        
        {submitted ? (
          <div style={{ padding: '2rem', textAlign: 'center', color: '#2ecc71' }}>
            <h3>Demande Transmise avec Succès !</h3>
            <p>L'équipe médicale a été notifiée concernant le détenu #{selectedInmate.id}.</p>
          </div>
        ) : (
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <div className="input-group full-width">
              <label>Détenu sélectionné</label>
              <input type="text" value={`#${selectedInmate.id} - ${selectedInmate.firstName} ${selectedInmate.lastName}`} disabled style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid var(--border-color)' }} />
            </div>
            
            <div className="input-group full-width">
              <label>Motif de la demande {mode === 'urgence' && <span style={{color: '#ff4d4d'}}>*</span>}</label>
              <textarea 
                rows={4} 
                required 
                value={reason}
                onChange={e => setReason(e.target.value)}
                placeholder={mode === 'urgence' ? "Nature de la blessure ou des symptômes..." : "Raison de la consultation..."} 
                style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid var(--border-color)', background: 'var(--bg-main)', color: 'var(--text-main)' }}
              />
            </div>

            {mode === 'urgence' && (
              <div className="input-group full-width">
                <label>Niveau de Gravité</label>
                <div style={{ display: 'flex', gap: '10px', marginTop: '5px' }}>
                  {['basse', 'moyenne', 'haute', 'critique'].map((s) => (
                    <button
                      key={s}
                      type="button"
                      onClick={() => setSeverity(s as any)}
                      style={{
                        flex: 1,
                        padding: '8px',
                        borderRadius: '4px',
                        border: '1px solid var(--border-color)',
                        backgroundColor: severity === s ? (s === 'critique' ? '#C0392B' : s === 'haute' ? '#D35400' : s === 'moyenne' ? '#2E6DA4' : '#16A085') : 'transparent',
                        color: severity === s ? 'white' : 'var(--text-main)',
                        fontSize: '0.8rem',
                        fontWeight: 'bold',
                        cursor: 'pointer',
                        textTransform: 'capitalize'
                      }}
                    >
                      {s}
                    </button>
                  ))}
                </div>
              </div>
            )}
            
            <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '10px' }}>
               <button 
                 type="submit" 
                 style={{ 
                   backgroundColor: mode === 'urgence' ? '#C0392B' : '#2E6DA4', 
                   color: 'white', 
                   border: 'none', 
                   padding: '10px 20px', 
                   borderRadius: '4px', 
                   fontWeight: 'bold', 
                   cursor: 'pointer' 
                 }}
               >
                 {mode === 'urgence' ? 'Déclencher l\'Alerte' : 'Soumettre la Demande'}
               </button>
            </div>
          </form>
        )}
      </div>

      {/* Historique Médical (Dynamique) */}
      <div className="card" style={{ padding: '20px' }}>
         <h3 style={{ color: 'var(--brand-accent)', borderBottom: '1px solid var(--border-color)', paddingBottom: '10px', marginBottom: '15px' }}>Suivi des Demandes Récentes</h3>
         
         {medicalRequests.filter(r => r.inmateId === selectedInmate.id).length === 0 ? (
           <p style={{ opacity: 0.5, fontSize: '0.9rem' }}>Aucune demande récente pour ce détenu.</p>
         ) : (
           <ul style={{ listStyle: 'none', padding: 0 }}>
              {medicalRequests
                .filter(req => req.inmateId === selectedInmate.id)
                .map(req => (
                  <li key={req.id} style={{ padding: '12px 0', borderBottom: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div style={{ display: 'flex', flexDirection: 'column' }}>
                      <span style={{ fontWeight: 'bold', color: '#e2e8f0' }}>{req.date}</span>
                      <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>{req.type.toUpperCase()} : {req.reason}</span>
                    </div>
                    <span 
                      style={{ 
                        padding: '4px 10px', 
                        borderRadius: '12px', 
                        fontSize: '0.75rem', 
                        fontWeight: 'bold',
                        backgroundColor: 
                          req.status === 'treated' ? 'rgba(34, 197, 94, 0.1)' : 
                          req.status === 'pending' ? 'rgba(234, 179, 8, 0.1)' : 'rgba(239, 68, 68, 0.1)',
                        color: 
                          req.status === 'treated' ? '#22c55e' : 
                          req.status === 'pending' ? '#eab308' : '#ef4444',
                        border: `1px solid ${
                          req.status === 'treated' ? '#22c55e' : 
                          req.status === 'pending' ? '#eab308' : '#ef4444'
                        }`
                      }}
                    >
                      {req.status === 'treated' ? 'TRAITÉ' : 
                       req.status === 'pending' ? 'EN ATTENTE' : 'ANNULÉ'}
                    </span>
                  </li>
                ))}
           </ul>
         )}
      </div>
    </div>
  );
};

export default MedicalDashboard;
