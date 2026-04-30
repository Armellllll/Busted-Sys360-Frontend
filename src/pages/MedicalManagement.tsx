import { useInmatesData } from '../context/InmatesDataContext';
import { Activity, Calendar, CheckCircle, Clock, XCircle, AlertCircle } from 'lucide-react';
import './Dashboard.css';

const MedicalManagement = () => {
  const { medicalRequests, updateMedicalRequestStatus } = useInmatesData();

  const pendingRequests = medicalRequests.filter(req => req.status === 'pending');
  const emergencies = pendingRequests.filter(req => req.type === 'urgence');
  const regularRDV = pendingRequests.filter(req => req.type === 'rdv');
  const treatedRequests = medicalRequests.filter(req => req.status === 'treated');

  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <div>
          <h1 className="page-title">Gestion du Service Médical</h1>
          <p className="page-subtitle">Traitement des demandes et urgences en temps réel.</p>
        </div>
        <div style={{ display: 'flex', gap: '15px' }}>
          <div className="stat-badge" style={{ background: 'rgba(239, 68, 68, 0.2)', color: '#ef4444', padding: '8px 15px', borderRadius: '6px', fontWeight: 'bold', border: '1px solid #ef4444' }}>
            {emergencies.length} URGENCES
          </div>
        </div>
      </header>

      <div className="dashboard-widgets" style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '25px' }}>
        
        {/* Colonne Principale: File d'attente */}
        <div className="column-left" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          <section className="widget card" style={{ padding: '20px' }}>
            <h3 style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px', display: 'flex', alignItems: 'center', gap: '10px', color: '#FFADAD' }}>
               <AlertCircle size={20} /> Urgences Vitales ({emergencies.length})
            </h3>
            <div style={{ marginTop: '15px' }}>
              {emergencies.length === 0 ? (
                <p style={{ opacity: 0.6, fontStyle: 'italic' }}>Aucune urgence signalée.</p>
              ) : (
                emergencies.map(req => (
                  <div key={req.id} style={{ background: 'rgba(192, 57, 43, 0.1)', border: '1px solid #C0392B', borderRadius: '8px', padding: '15px', marginBottom: '12px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                      <div style={{ fontWeight: 'bold', fontSize: '1.1rem', color: '#fff' }}>{req.inmateName} <span style={{ fontSize: '0.8rem', opacity: 0.7 }}>#{req.inmateId}</span></div>
                      <div style={{ marginTop: '5px', color: '#ffadad', fontWeight: 'bold' }}>MOTIF: {req.reason}</div>
                      <div style={{ fontSize: '0.8rem', opacity: 0.6, marginTop: '5px' }}><Clock size={12} inline /> Signalé à {req.date}</div>
                    </div>
                    <button 
                      onClick={() => updateMedicalRequestStatus(req.id, 'treated')}
                      style={{ background: '#C0392B', color: '#fff', border: 'none', padding: '10px 20px', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer', boxShadow: '0 4px 10px rgba(0,0,0,0.3)' }}
                    >
                      TRAITER URGENCE
                    </button>
                  </div>
                ))
              )}
            </div>
          </section>

          <section className="widget card" style={{ padding: '20px' }}>
            <h3 style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '12px', display: 'flex', alignItems: 'center', gap: '10px', color: '#A0C4FF' }}>
               <Calendar size={20} /> Demandes de Rendez-vous ({regularRDV.length})
            </h3>
            <div style={{ marginTop: '15px' }}>
              {regularRDV.length === 0 ? (
                <p style={{ opacity: 0.6, fontStyle: 'italic' }}>Aucun rendez-vous en attente.</p>
              ) : (
                regularRDV.map(req => (
                  <div key={req.id} style={{ borderBottom: '1px solid var(--border-color)', padding: '12px 0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                      <div style={{ fontWeight: 'bold' }}>{req.inmateName}</div>
                      <div style={{ fontSize: '0.9rem', opacity: 0.8 }}>{req.reason}</div>
                      <div style={{ fontSize: '0.75rem', opacity: 0.5 }}>Reçu le {req.date}</div>
                    </div>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button 
                        onClick={() => updateMedicalRequestStatus(req.id, 'treated')}
                        style={{ background: 'rgba(34, 197, 94, 0.1)', color: '#22c55e', border: '1px solid #22c55e', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer' }}
                      >
                         Approuver
                      </button>
                      <button 
                        onClick={() => updateMedicalRequestStatus(req.id, 'cancelled')}
                        style={{ background: 'rgba(239, 68, 68, 0.1)', color: '#ef4444', border: '1px solid #ef4444', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer' }}
                      >
                         Refuser
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </section>
        </div>

        {/* Colonne Droite: Historique & Stats */}
        <div className="column-right" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <div className="widget card" style={{ padding: '20px' }}>
            <h3 style={{ fontSize: '1rem', marginBottom: '15px', color: 'var(--brand-accent)' }}>Derniers Traitements</h3>
            <div className="history-list">
              {treatedRequests.slice(0, 5).map(req => (
                <div key={req.id} style={{ fontSize: '0.85rem', padding: '8px 0', borderBottom: '1px solid rgba(255,255,255,0.05)', display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <CheckCircle size={14} color="#22c55e" />
                  <div>
                    <span style={{ fontWeight: 'bold' }}>{req.inmateName}</span>
                    <div style={{ opacity: 0.6, fontSize: '0.75rem' }}>{req.type.toUpperCase()} - {req.date}</div>
                  </div>
                </div>
              ))}
              {treatedRequests.length === 0 && <p style={{ opacity: 0.5, fontSize: '0.85rem' }}>Aucun historique aujourd'hui.</p>}
            </div>
          </div>

          <div className="widget card" style={{ padding: '20px', background: 'rgba(46, 204, 113, 0.05)', border: '1px solid rgba(46, 204, 113, 0.2)' }}>
            <h3 style={{ fontSize: '1rem', color: '#2ecc71', marginBottom: '10px' }}>Capacité de Soins</h3>
            <div style={{ fontSize: '0.85rem' }}>
              <p>Équipe : <strong>Standard (3 pers)</strong></p>
              <p>Temps d'attente moy : <strong>12 min</strong></p>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};

export default MedicalManagement;
