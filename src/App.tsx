import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import InmatesList from './pages/InmatesList';
import InmateRegistration from './pages/InmateRegistration';
import InmateProfile from './pages/InmateProfile';
import CellsManagement from './pages/CellsManagement';
import MedicalDashboard from './pages/MedicalDashboard';
import VisitsScheduler from './pages/VisitsScheduler';
import InfractionDeclaration from './pages/InfractionDeclaration';
import Login from './pages/Login';
import RapportsStatistiques from './pages/RapportsStatistiques';
import RapportsPersonnel from './pages/RapportsPersonnel';
import Notifications from './pages/Notifications';
import ListePresence from './pages/ListePresence';
import MedicalManagement from './pages/MedicalManagement';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import { InmateSelectionProvider } from './context/InmateSelectionContext';
import { InmatesDataProvider } from './context/InmatesDataContext';
import './index.css';

function App() {
  return (
    // InmatesDataProvider est au niveau racine, jamais re-monté lors des navigations
    <InmatesDataProvider>
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/login" element={<Login />} />
            
            <Route element={<ProtectedRoute />}>
              <Route element={
                <InmateSelectionProvider>
                  <Layout />
                </InmateSelectionProvider>
              }>
                <Route index element={<Navigate to="/dashboard" replace />} />
                <Route path="dashboard" element={<Dashboard />} />
                
                {/* Accessible by all authenticated users */}
                <Route path="inmates" element={<InmatesList />} />
                <Route path="inmates/:id" element={<InmateProfile />} />
                
                {/* Greffier */}
                <Route element={<ProtectedRoute allowedRoles={['greffier']} />}>
                  <Route path="inmates/new" element={<InmateRegistration />} />
                </Route>

                {/* Directeur, Greffier, Agent */}
                <Route element={<ProtectedRoute allowedRoles={['directeur', 'greffier', 'agent']} />}>
                  <Route path="cells" element={<CellsManagement />} />
                </Route>

                {/* Directeur */}
                <Route element={<ProtectedRoute allowedRoles={['directeur']} />}>
                  <Route path="rapports-statistiques" element={<RapportsStatistiques />} />
                  <Route path="rapports-personnel" element={<RapportsPersonnel />} />
                  <Route path="notifications" element={<Notifications />} />
                </Route>

                {/* Agent */}
                <Route element={<ProtectedRoute allowedRoles={['agent']} />}>
                  <Route path="medical" element={<MedicalDashboard />} />
                  <Route path="infractions" element={<InfractionDeclaration />} />
                  <Route path="liste-presence" element={<ListePresence />} />
                </Route>

                {/* Responsable Visite */}
                <Route element={<ProtectedRoute allowedRoles={['responsable_visite']} />}>
                  <Route path="visites" element={<VisitsScheduler />} />
                </Route>

                {/* Service Médical */}
                <Route element={<ProtectedRoute allowedRoles={['service_medical']} />}>
                  <Route path="medical-management" element={<MedicalManagement />} />
                </Route>

                <Route path="*" element={<Navigate to="/dashboard" replace />} />
              </Route>
            </Route>
          </Routes>
        </Router>
      </AuthProvider>
    </InmatesDataProvider>
  );
}

export default App;

