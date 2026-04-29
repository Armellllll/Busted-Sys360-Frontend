import { useState } from 'react';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { Calendar as CalendarIcon, ShieldAlert, CheckCircle, Clock } from 'lucide-react';
import './VisitsScheduler.css';

const VisitsScheduler = () => {
  const { selectedInmate } = useSelectedInmate();
  const [visitorName, setVisitorName] = useState('');
  const [visitType, setVisitType] = useState('Famille');
  const [selectedSlot, setSelectedSlot] = useState('');
  
  // Simulation: Affichage systématique de créneaux
  const availableSlots = ["Demain 14:00", "Demain 16:30", "Jeudi 09:00"];

  if (!selectedInmate) {
    return (
      <div className="empty-state-container">
        <h2>Aucun Détenu Sélectionné</h2>
        <p>Sélectionnez un détenu dans la liste de gauche pour gérer ses visites.</p>
      </div>
    );
  }

  const isAuthorized = selectedInmate.releaseStatus !== 'danger';

  const confirmVisit = () => {
    alert(`Visite enregistrée avec succès pour ${selectedInmate.firstName} le créneau: ${selectedSlot}`);
    setSelectedSlot('');
    setVisitorName('');
  };

  return (
    <div className="visits-scheduler animate-fade-in" style={{ padding: '0' }}>
       <header className="page-header" style={{ marginBottom: '1.5rem' }}>
           <h1 className="page-title font-roboto">Gestion des Visites</h1>
           <p className="page-subtitle">Dossier de visite de {selectedInmate.firstName} {selectedInmate.lastName}</p>
       </header>

       {/* Statut d'autorisation */}
       <div className="card" style={{ marginBottom: '20px', padding: '15px', borderLeft: isAuthorized ? '4px solid #27AE60' : '4px solid #C0392B', display: 'flex', alignItems: 'center', gap: '15px' }}>
          {isAuthorized ? <CheckCircle color="#27AE60" size={32} /> : <ShieldAlert color="#C0392B" size={32} />}
          <div>
             <h3 style={{ margin: 0, color: isAuthorized ? '#27AE60' : '#C0392B' }}>
                {isAuthorized ? 'Visites Autorisées' : 'Visites Suspendues'}
             </h3>
             <p style={{ margin: 0, color: '#666', fontSize: '0.9rem' }}>
                {isAuthorized ? 'Ce détenu dispose de tous ses droits de visite réguliers.' : 'Détenu actuellement sous le coup d\'une sanction disciplinaire (Isolement). Seules les visites obligatoires (Avocats) sont permises.'}
             </p>
          </div>
       </div>

       <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
         {/* Historique */}
         <div className="card" style={{ padding: '20px' }}>
           <h3 style={{ color: 'var(--brand-accent)', borderBottom: '1px solid #CCC', paddingBottom: '10px' }}><Clock size={16} style={{ verticalAlign: 'middle', marginRight: '5px' }} /> Historique des visites</h3>
           <ul style={{ listStyle: 'none', padding: 0 }}>
              <li style={{ padding: '10px 0', borderBottom: '1px solid #EEE' }}>
                 <strong style={{ color: '#444' }}>14 Avr 2026</strong> - Marie Dupont (Mère) - <span style={{ color: '#27AE60' }}>Terminée</span>
              </li>
              <li style={{ padding: '10px 0', borderBottom: '1px solid #EEE' }}>
                 <strong style={{ color: '#444' }}>02 Fév 2026</strong> - Me. Martin (Avocat) - <span style={{ color: '#27AE60' }}>Terminée</span>
              </li>
           </ul>
         </div>

         {/* Planification d'une visite */}
         <div className="card" style={{ padding: '20px', opacity: !isAuthorized ? 0.6 : 1, pointerEvents: !isAuthorized ? 'none' : 'auto' }}>
           <h3 style={{ color: 'var(--brand-accent)', borderBottom: '1px solid #CCC', paddingBottom: '10px' }}>Planifier une Visite</h3>
           
           <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '15px' }}>
             <div className="input-group">
               <label>Nom du visiteur pré-enregistré</label>
               <input type="text" value={visitorName} onChange={e => setVisitorName(e.target.value)} placeholder="Ex: Marie Dupont" style={{ width: '100%' }} />
             </div>
             
             <div className="input-group">
               <label>Motif / Type</label>
               <select value={visitType} onChange={e => setVisitType(e.target.value)} style={{ width: '100%' }}>
                 <option>Famille</option>
                 <option>Conjoint(e)</option>
                 <option>Ami / Autre</option>
                 <option>Officiel (Avocat)</option>
               </select>
             </div>

             <div style={{ marginTop: '15px' }}>
               <label style={{ display: 'block', fontSize: '0.9rem', color: 'var(--text-muted)', marginBottom: '10px', fontWeight: 'bold' }}>Créneaux Disponibles :</label>
               <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
                 {availableSlots.map(slot => (
                   <div 
                     key={slot} 
                     onClick={() => setSelectedSlot(slot)}
                     style={{ 
                       padding: '10px 15px', 
                       border: selectedSlot === slot ? '2px solid var(--brand-accent)' : '1px solid #CCC', 
                       backgroundColor: selectedSlot === slot ? '#EBF5FF' : '#FFF', 
                       borderRadius: '6px', 
                       cursor: 'pointer',
                       display: 'flex',
                       alignItems: 'center',
                       gap: '8px'
                     }}
                   >
                     <CalendarIcon size={16} /> <span>{slot}</span>
                   </div>
                 ))}
               </div>
             </div>

             <button 
               className="btn-primary" 
               disabled={!selectedSlot || !visitorName} 
               onClick={confirmVisit}
               style={{ marginTop: '20px', width: '100%', padding: '12px' }}
             >
               Enregistrer la visite
             </button>
           </div>
         </div>
       </div>
    </div>
  );
};
export default VisitsScheduler;
