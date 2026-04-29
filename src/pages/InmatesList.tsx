import { useNavigate } from 'react-router-dom';
import './InmatesList.css';

const InmatesList = () => {
  const navigate = useNavigate();

  const mockInmates = [
    { 
      id: '1', 
      lastName: 'CARTER', 
      firstName: 'Jason Aaron',
      raceSexAge: 'W/M/30 years',
      location: 'Judsonia, AR',
      arrested: '7/18/2026 12:45AM',
      released: 'Not Released'
    },
    { 
      id: '2', 
      lastName: 'Armstrong', 
      firstName: 'Chon Elex',
      raceSexAge: 'W/M/31 years',
      location: 'Pocahontas, AR',
      arrested: '7/18/2026 10:20AM',
      released: 'Not Released'
    },
    { 
      id: '3', 
      lastName: 'Duren', 
      firstName: 'Christopher',
      raceSexAge: 'W/M/32 years',
      location: 'Searcy, AR',
      arrested: '7/18/2026 12:00AM',
      released: 'Not Released'
    },
    { 
      id: '4', 
      lastName: 'Valencia', 
      firstName: 'Jose M',
      raceSexAge: 'U/M/40 years',
      location: 'Bradford, AR',
      arrested: '7/18/2026 12:34AM',
      released: 'Not Released'
    }
  ];

  return (
    <div className="legacy-mobile-container">
      {/* Mobile Header */}
      <div className="legacy-header">
        <div className="menu-icon">
          <div className="hamburger-line"></div>
          <div className="hamburger-line"></div>
          <div className="hamburger-line"></div>
        </div>
        <div className="header-title">Inmates In-Jail({mockInmates.length})</div>
        <div className="search-icon">🔍</div>
      </div>

      {/* Sub Header */}
      <div className="legacy-subheader">
        White County Sheriffs Office
      </div>

      {/* List */}
      <div className="legacy-list">
        {mockInmates.map((inmate) => (
          <div 
            key={inmate.id} 
            className="legacy-list-item"
            onClick={() => navigate(`/inmates/${inmate.id}`)}
          >
            <div className="legacy-photo-container">
              {/* Silhouette Placeholder */}
              <div className="legacy-silhouette"></div>
            </div>
            <div className="legacy-details">
              <div className="legacy-name">{inmate.lastName}, {inmate.firstName}</div>
              <div className="legacy-row">{inmate.raceSexAge}</div>
              <div className="legacy-row">{inmate.location}</div>
              <div className="legacy-row">Arrested: {inmate.arrested}</div>
              <div className="legacy-row">Released: {inmate.released}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default InmatesList;
