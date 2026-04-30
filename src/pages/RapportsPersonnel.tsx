import { Shield, Activity, Calendar, Users, ClipboardCheck, Info } from 'lucide-react';
import './Dashboard.css';

const RapportsPersonnel = () => {
  const services = [
    {
      id: 'security',
      title: 'Service Sécurité',
      icon: <Shield size={24} color="#3b82f6" />,
      stats: { primary: '2 Incidents', secondary: '100% Postes pourvus' },
      reports: [
        'Relève de garde effectuée à 06:00.',
        'Inspection du Bloc C - Aucune anomalie.',
        'Alerte mineure (Bagarre réfectoire) gérée à 12:30.'
      ],
      status: 'Opérationnel',
      statusColor: '#22c55e'
    },
    {
      id: 'medical',
      title: 'Service Médical',
      icon: <Activity size={24} color="#ef4444" />,
      stats: { primary: '8 Consultations', secondary: '0 Urgence active' },
      reports: [
        'Distribution médication matin finalisée.',
        '2 Nouveaux dossiers ouverts.',
        'Dr. Martin présent jusqu\'à 18:00.'
      ],
      status: 'Occupé',
      statusColor: '#f59e0b'
    },
    {
      id: 'visits',
      title: 'Service Visites',
      icon: <Calendar size={24} color="#a855f7" />,
      stats: { primary: '15 Visites', secondary: '4 En attente' },
      reports: [
        'Parloir famille (Zone A) saturé.',
        '3 Visites d\'avocats enregistrées.',
        'Vérification des accréditations en cours.'
      ],
      status: 'Opérationnel',
      statusColor: '#22c55e'
    },
    {
      id: 'registry',
      title: 'Service Greffe',
      icon: <Users size={24} color="#06b6d4" />,
      stats: { primary: '4 Entrées', secondary: '1 Sortie' },
      reports: [
        'Enregistrement de 2 nouveaux détenus (Police).',
        'Dossier de libération #452 validé.',
        'Archivage dossiers mensuels en cours.'
      ],
      status: 'Normal',
      statusColor: '#22c55e'
    }
  ];

  return (
    <div className="dashboard animate-fade-in">
      <header className="dashboard-header">
        <div>
          <h1 className="page-title">Rapports du Jour par Service</h1>
          <p className="page-subtitle">Aperçu consolidé de l'activité journalière des services.</p>
        </div>
        <div style={{ display: 'flex', gap: '10px' }}>
             <button className="btn-primary" style={{ padding: '8px 16px', background: 'var(--brand-accent)', border: 'none', borderRadius: '6px', color: '#fff', fontWeight: 'bold', cursor: 'pointer' }}>
                Exporter le Rapport Général
             </button>
        </div>
      </header>

      <div className="dashboard-widgets" style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
        {services.map((service) => (
          <div key={service.id} className="widget card" style={{ padding: '20px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '15px' }}>
              <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                <div style={{ background: 'rgba(255,255,255,0.05)', padding: '10px', borderRadius: '10px' }}>{service.icon}</div>
                <div>
                  <h3 style={{ margin: 0, fontSize: '1.1rem', color: 'var(--brand-accent)' }}>{service.title}</h3>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '5px', marginTop: '4px' }}>
                    <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: service.statusColor }}></div>
                    <span style={{ fontSize: '0.8rem', opacity: 0.8 }}>{service.status}</span>
                  </div>
                </div>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>{service.stats.primary}</div>
                <div style={{ fontSize: '0.75rem', opacity: 0.6 }}>{service.stats.secondary}</div>
              </div>
            </div>

            <div style={{ background: 'rgba(0,0,0,0.1)', padding: '15px', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
              <h4 style={{ margin: '0 0 10px 0', fontSize: '0.85rem', textTransform: 'uppercase', opacity: 0.7, display: 'flex', alignItems: 'center', gap: '6px' }}>
                <ClipboardCheck size={14} /> Activités & Rapports
              </h4>
              <ul style={{ margin: 0, padding: 0, listStyle: 'none' }}>
                {service.reports.map((report, idx) => (
                  <li key={idx} style={{ padding: '6px 0', borderBottom: idx < service.reports.length - 1 ? '1px solid rgba(255,255,255,0.05)' : 'none', fontSize: '0.9rem', display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
                    <span style={{ marginTop: '3px' }}><div style={{ width: '4px', height: '4px', borderRadius: '50%', background: 'var(--brand-accent)' }}></div></span>
                    {report}
                  </li>
                ))}
              </ul>
            </div>

            <div style={{ marginTop: '15px', display: 'flex', justifyContent: 'flex-end' }}>
               <button style={{ background: 'transparent', border: '1px solid var(--border-color)', color: 'var(--text-main)', fontSize: '0.8rem', padding: '4px 10px', borderRadius: '4px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '5px' }}>
                  <Info size={14} /> Détails complets
               </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RapportsPersonnel;
