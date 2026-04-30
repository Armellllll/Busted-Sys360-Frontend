import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import { useInmatesData } from '../context/InmatesDataContext';
import { User, Upload, ArrowLeft, FileText, Plus, ShieldCheck } from 'lucide-react';
import { useState } from 'react';
import './InmateProfile.css';

const InmateProfile = () => {
  const navigate = useNavigate();
  const { role } = useAuth();
  const { selectedInmate } = useSelectedInmate();
  const { updateInmatePhoto } = useInmatesData();
  const [documents, setDocuments] = useState<string[]>(['Mandat d\'arrêt']);

  const canEdit = role === 'greffier';

  if (!selectedInmate) {
    return (
      <div className="empty-state-container">
        <h2>Aucun Détenu Sélectionné</h2>
        <p>Sélectionnez un détenu dans la liste pour consulter ou modifier son dossier complet.</p>
      </div>
    );
  }

  const charges = ['NON-COMPARUTION', 'ASSISTANCE À D\'AUTRES AGENCES'];
  const sanctions = selectedInmate.sanctions || [];

  const handlePhotoUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const pUrl = URL.createObjectURL(e.target.files[0]);
      updateInmatePhoto(selectedInmate.id, pUrl);
    }
  };

  const handleDocumentUpload = () => {
    const docName = window.prompt("Entrez le nom du document (ou importez depuis vos fichiers localement)");
    if (docName) {
      setDocuments([...documents, docName]);
    }
  };

  return (
    <div className="legacy-profile-container">
      {/* Header */}
      <div className="legacy-profile-header">
        <div className="header-left" onClick={() => navigate('/inmates')} style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '5px' }}>
          <ArrowLeft size={16} /> Retour
        </div>
        <div className="header-right">
          Profil {canEdit ? 'Auteur de modification' : 'Aperçu (Lecture seule)'}
        </div>
      </div>

      <div className="legacy-profile-body">
        {/* Photo Section */}
        <div className="legacy-profile-photo-section" style={{ position: 'relative' }}>
          <div className="large-photo" style={{ position: 'relative', display: 'flex', alignItems: 'center', justifyContent: 'center', backgroundColor: '#6C8CA5', width: '150px', height: '180px', borderRadius: '5px', overflow: 'hidden' }}>
            {selectedInmate.photoUrl ? (
              <img src={selectedInmate.photoUrl} alt="Photo du détenu" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
            ) : (
              <User size={80} color="#CBD5E1" />
            )}
            
            {/* Upload Button */}
            {canEdit && (
              <label className="photo-upload-btn" style={{ position: 'absolute', bottom: '5px', right: '5px', background: 'rgba(0,0,0,0.6)', padding: '5px', borderRadius: '50%', cursor: 'pointer' }}>
                <Upload size={16} color="#fff" />
                <input type="file" accept="image/*" style={{ display: 'none' }} onChange={handlePhotoUpload} />
              </label>
            )}
            {!canEdit && (
              <div style={{ position: 'absolute', bottom: '5px', right: '5px', background: 'rgba(0,0,0,0.4)', padding: '5px', borderRadius: '50%', pointerEvents: 'none' }}>
                <ShieldCheck size={14} color="#aaa" />
              </div>
            )}
          </div>
        </div>

        {/* Info Header */}
        <h2 className="legacy-profile-name">{selectedInmate.lastName}, {selectedInmate.firstName}</h2>

        {/* Basic Info */}
        <div className="legacy-basic-info">
          <div><span className="info-label">Sexe/Race/Âge:</span> {selectedInmate.raceSexAge}</div>
          <div><span className="info-label">Cellule:</span> {selectedInmate.cellule || 'Non assignée'}</div>
          <div><span className="info-label">Lieu:</span> {selectedInmate.location}</div>
          <div><span className="info-label">Arrêté le:</span> {selectedInmate.arrested}</div>
          <div><span className="info-label">Libération:</span> {selectedInmate.released === 'Not Released' ? 'Non libéré' : selectedInmate.released}</div>
        </div>

        {/* Block: Documents Légaux */}
        <div className="legacy-block">
          <div className="legacy-block-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span>Documents Légaux ({documents.length})</span>
            {canEdit && (
              <button onClick={handleDocumentUpload} style={{ background: '#3b82f6', color: '#fff', border: 'none', borderRadius: '4px', padding: '2px 6px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '12px' }}>
                <Plus size={12} /> Ajouter
              </button>
            )}
          </div>
          <div className="legacy-block-content" style={{ maxHeight: '150px', overflowY: 'auto' }}>
            {documents.map((doc, idx) => (
              <div key={idx} className="charge-item" style={{display: 'flex', alignItems: 'center', gap: '5px'}}>
                <FileText size={14} color="#666" /> {doc}
              </div>
            ))}
          </div>
        </div>

        {/* Block: Charges */}
        <div className="legacy-block">
          <div className="legacy-block-header">
            Charges ({charges.length})
          </div>
          <div className="legacy-block-content">
            {charges.map((charge, idx) => (
              <div key={idx} className="charge-item">» {charge}</div>
            ))}
          </div>
        </div>

        {/* Block: Sanctions */}
        <div className="legacy-block">
          <div className="legacy-block-header">
            Sanctions ({sanctions.length})
          </div>
          <div className="legacy-block-content" style={{ maxHeight: '150px', overflowY: 'auto' }}>
            {sanctions.length > 0 ? sanctions.map((sanction, idx) => (
              <div key={idx} className="bond-item" style={{ borderBottom: '1px solid #eee', paddingBottom: '5px', marginBottom: '5px' }}>
                <strong>{sanction.date}</strong> - {sanction.description}
              </div>
            )) : (
              <div className="empty-content">Aucune sanction</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default InmateProfile;
