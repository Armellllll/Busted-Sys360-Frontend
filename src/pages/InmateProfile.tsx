import { useNavigate } from 'react-router-dom';
import { useSelectedInmate } from '../context/InmateSelectionContext';
import './InmateProfile.css';

const InmateProfile = () => {
  const navigate = useNavigate();
  const { selectedInmate } = useSelectedInmate();

  if (!selectedInmate) {
    return (
      <div className="empty-state-container">
        <h2>Aucun Détenu Sélectionné</h2>
        <p>Sélectionnez un détenu dans la liste de gauche pour consulter ou modifier son dossier complet.</p>
      </div>
    );
  }

  // Mocked details not inside the context for visual representation
  const charges = ['FAILURE TO APPEAR', 'ASSISTING OTHER AGENCIES'];
  const bonds = ['$540', '$295'];

  return (
    <div className="legacy-profile-container">
      {/* Header */}
      <div className="legacy-profile-header">
        <div className="header-left" onClick={() => navigate('/inmates')}>
          <span className="back-arrow">{'<'}</span> Focus
        </div>
        <div className="header-right">
          Details
        </div>
      </div>

      <div className="legacy-profile-body">
        {/* Photo Section */}
        <div className="legacy-profile-photo-section">
          <div className="nav-arrow left-arrow">{'<'}</div>
          <div className="large-photo">
            <div className="large-silhouette"></div>
          </div>
          <div className="nav-arrow right-arrow">{'>'}</div>
        </div>

        {/* Info Header */}
        <h2 className="legacy-profile-name">{selectedInmate.lastName}, {selectedInmate.firstName}</h2>

        {/* Basic Info */}
        <div className="legacy-basic-info">
          <div><span className="info-label">Race/Sex/Age:</span> {selectedInmate.raceSexAge}</div>
          <div><span className="info-label">Location:</span> {selectedInmate.location}</div>
          <div><span className="info-label">Arrested Date:</span> {selectedInmate.arrested}</div>
          <div><span className="info-label">Released Date:</span> {selectedInmate.released}</div>
        </div>

        {/* Greffier specific action block */}
        <div className="legacy-block action-block">
          <button className="btn-action">Modifier Informations</button>
          <button className="btn-action">Documents Légaux</button>
        </div>

        {/* Block: Charges */}
        <div className="legacy-block">
          <div className="legacy-block-header">
            Charges({charges.length})
          </div>
          <div className="legacy-block-content">
            {charges.map((charge, idx) => (
              <div key={idx} className="charge-item">» {charge}</div>
            ))}
          </div>
        </div>

        {/* Block: Bonds */}
        <div className="legacy-block">
          <div className="legacy-block-header">
            Bonds({bonds.length})
          </div>
          <div className="legacy-block-content">
            {bonds.map((bond, idx) => (
              <div key={idx} className="bond-item">{bond}</div>
            ))}
          </div>
        </div>
        
        {/* Block: Court Date */}
        <div className="legacy-block">
          <div className="legacy-block-header">
            Court Date(1)
          </div>
          <div className="legacy-block-content empty-content">
            No active dates
          </div>
        </div>
      </div>
    </div>
  );
};

export default InmateProfile;
