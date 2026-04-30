import { useState } from 'react';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { Calendar as CalendarIcon, ShieldAlert, CheckCircle, Clock, User } from 'lucide-react';
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
       <header className="page-header" style={{ marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
           <div>
              <h1 className="page-title font-roboto">Gestion des Visites</h1>
              <p className="page-subtitle">Dossier de visite de {selectedInmate.firstName} {selectedInmate.lastName}</p>
           </div>
       </header>

       {/* Statut d'autorisation */}
       <div className="card" style={{ marginBottom: '20px', padding: '15px', borderLeft: isAuthorized ? '4px solid #27AE60' : '4px solid #C0392B', display: 'flex', alignItems: 'center', gap: '15px' }}>
          {isAuthorized ? <CheckCircle color="#27AE60" size={32} /> : <ShieldAlert color="#C0392B" size={32} />}
          <div style={{ flex: 1 }}>
             <h3 style={{ margin: 0, color: isAuthorized ? '#2ecc71' : '#ff4d4d' }}>
                {isAuthorized ? 'Visites Autorisées' : 'Visites Suspendues'}
             </h3>
             <p style={{ margin: 0, color: '#94a3b8', fontSize: '0.9rem' }}>
                {isAuthorized ? 'Ce détenu dispose de tous ses droits de visite réguliers.' : 'Détenu actuellement sous le coup d\'une sanction disciplinaire. Seules les visites d\'avocat sont autorisées.'}
             </p>
          </div>
       </div>

       <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
         {/* Historique */}
         <div className="card" style={{ padding: '20px' }}>
           <h3 style={{ color: 'var(--brand-accent)', borderBottom: '1px solid var(--border-color)', paddingBottom: '10px' }}><Clock size={16} style={{ verticalAlign: 'middle', marginRight: '5px' }} /> Historique</h3>
           <ul style={{ listStyle: 'none', padding: 0 }}>
              <li style={{ padding: '12px 0', borderBottom: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between' }}>
                 <div>
                    <strong style={{ color: '#e2e8f0' }}>14 Avr 2026</strong>
                    <div style={{ fontSize: '0.8rem', color: '#94a3b8' }}>Marie Dupont (Mère)</div>
                 </div>
                 <span style={{ color: '#2ecc71', fontSize: '0.8rem', fontWeight: 'bold' }}>TERMINEE</span>
              </li>
              <li style={{ padding: '12px 0', borderBottom: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between' }}>
                 <div>
                    <strong style={{ color: '#e2e8f0' }}>02 Fév 2026</strong>
                    <div style={{ fontSize: '0.8rem', color: '#94a3b8' }}>Me. Martin (Avocat)</div>
                 </div>
                 <span style={{ color: '#2ecc71', fontSize: '0.8rem', fontWeight: 'bold' }}>TERMINEE</span>
              </li>
           </ul>
         </div>

         {/* Planification d'une visite */}
         <div className="card" style={{ padding: '20px', opacity: !isAuthorized ? 0.6 : 1, pointerEvents: !isAuthorized ? 'none' : 'auto' }}>
           <h3 style={{ color: 'var(--brand-accent)', borderBottom: '1px solid var(--border-color)', paddingBottom: '10px' }}>Planifier une Visite</h3>
           
           <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '15px' }}>
             <div className="input-group">
               <label>Nom du visiteur</label>
               <input type="text" value={visitorName} onChange={e => setVisitorName(e.target.value)} placeholder="Ex: Marie Dupont" style={{ background: 'var(--bg-main)', border: '1px solid var(--border-color)', color: 'var(--text-main)', padding: '10px', borderRadius: '4px' }} />
             </div>
             
             <div className="input-group">
               <label>Motif / Type</label>
               <select value={visitType} onChange={e => setVisitType(e.target.value)} style={{ background: 'var(--bg-main)', border: '1px solid var(--border-color)', color: 'var(--text-main)', padding: '10px', borderRadius: '4px' }}>
                 <option>Famille</option>
                 <option>Conjoint(e)</option>
                 <option>Ami / Autre</option>
                 <option>Officiel (Avocat)</option>
               </select>
             </div>

             <div style={{ marginTop: '15px' }}>
               <label style={{ display: 'block', fontSize: '0.9rem', color: '#94a3b8', marginBottom: '10px', fontWeight: 'bold' }}>Créneaux Disponibles :</label>
               <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
                 {availableSlots.map(slot => (
                   <div 
                     key={slot} 
                     onClick={() => setSelectedSlot(slot)}
                     style={{ 
                       padding: '10px 15px', 
                       border: selectedSlot === slot ? '2px solid #3b82f6' : '1px solid var(--border-color)', 
                       backgroundColor: selectedSlot === slot ? 'rgba(59, 130, 246, 0.1)' : 'var(--bg-main)', 
                       borderRadius: '6px', 
                       cursor: 'pointer',
                       display: 'flex',
                       alignItems: 'center',
                       gap: '8px',
                       color: selectedSlot === slot ? '#60a5fa' : 'var(--text-main)'
                     }}
                   >
                     <CalendarIcon size={16} /> <span style={{ fontSize: '0.85rem' }}>{slot}</span>
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
