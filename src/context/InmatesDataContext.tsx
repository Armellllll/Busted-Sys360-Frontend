import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';

export interface Inmate {
  id: string;
  lastName: string;
  firstName: string;
  dob: string;
  age: number;
  sex: string;
  raceSexAge: string;
  location: string;
  cellule?: string;
  arrested: string;
  sentenceDuration: string;
  released: string;
  releaseStatus: 'safe' | 'warning' | 'danger';
  photoUrl?: string;
  sanctions?: { date: string; description: string }[];
}

export interface MedicalRequest {
  id: string;
  inmateId: string;
  inmateName: string;
  type: 'rdv' | 'urgence';
  reason: string;
  date: string;
  status: 'pending' | 'treated' | 'cancelled';
  severity?: 'basse' | 'moyenne' | 'haute' | 'critique';
}

export interface RealTimeEvent {
  id: string;
  type: 'medical_emergency' | 'medical_rdv' | 'medical_update' | 'new_inmate' | 'infraction_manual_sanction';
  title: string;
  message: string;
  severity?: 'basse' | 'moyenne' | 'haute' | 'critique';
  date: string;
  data?: any;
}

interface InmatesDataContextType {
  inmates: Inmate[];
  medicalRequests: MedicalRequest[];
  latestEvent?: RealTimeEvent;
  isLoading: boolean;
  setInmates: (inmates: Inmate[]) => void;
  addInmate: (inmate: Omit<Inmate, 'id'>) => Promise<void>;
  refreshInmates: () => Promise<void>;
  updateInmatePhoto: (id: string, photoUrl: string) => void;
  addMedicalRequest: (request: Omit<MedicalRequest, 'id' | 'status' | 'date' | 'severity'> & { severity: MedicalRequest['severity'] }) => void;
  updateMedicalRequestStatus: (id: string, status: MedicalRequest['status']) => void;
  triggerRealTimeEvent: (event: Omit<RealTimeEvent, 'id' | 'date'>) => void;
  clearLatestEvent: () => void;
}

const defaultInmates: Inmate[] = [
  { 
    id: '1', 
    lastName: 'CARTER', 
    firstName: 'Jason Aaron',
    dob: '02/14/1996',
    age: 30,
    sex: 'Male',
    raceSexAge: 'W/M/30 years',
    location: 'Judsonia, AR',
    cellule: 'A-101',
    arrested: '18/07/2026 00:45',
    sentenceDuration: '5 ans',
    released: '10/12/2031',
    releaseStatus: 'safe',
    sanctions: [{ date: '01/05/2026', description: 'Altération mineure' }]
  },
  { 
    id: '2', 
    lastName: 'Armstrong', 
    firstName: 'Chon Elex',
    dob: '05/20/1995',
    age: 31,
    sex: 'Male',
    raceSexAge: 'W/M/31 years',
    location: 'Pocahontas, AR',
    cellule: 'B-205',
    arrested: '18/07/2026 10:20',
    sentenceDuration: '1 an',
    released: '15/08/2026',
    releaseStatus: 'warning',
    sanctions: []
  },
  { 
    id: '3', 
    lastName: 'Duren', 
    firstName: 'Christopher',
    dob: '11/04/1994',
    age: 32,
    sex: 'Male',
    raceSexAge: 'W/M/32 years',
    location: 'Searcy, AR',
    cellule: 'A-102',
    arrested: '18/07/2026 00:00',
    sentenceDuration: '6 mois',
    released: '01/05/2026',
    releaseStatus: 'danger',
    sanctions: [{ date: '03/12/2026', description: 'Bagarre dans la cour' }]
  },
  { 
    id: '4', 
    lastName: 'Valencia', 
    firstName: 'Jose M',
    dob: '01/30/1986',
    age: 40,
    sex: 'Male',
    raceSexAge: 'U/M/40 years',
    location: 'Bradford, AR',
    cellule: 'C-080',
    arrested: '18/07/2026 00:34',
    sentenceDuration: 'Perpétuité',
    released: 'Non libéré',
    releaseStatus: 'safe',
    sanctions: []
  }
];

const InmatesDataContext = createContext<InmatesDataContextType | undefined>(undefined);

export const InmatesDataProvider = ({ children }: { children: ReactNode }) => {
  const [inmates, setInmates] = useState<Inmate[]>(defaultInmates);
  const [medicalRequests, setMedicalRequests] = useState<MedicalRequest[]>([]);
  const [latestEvent, setLatestEvent] = useState<RealTimeEvent | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(false);

  const triggerRealTimeEvent = (event: Omit<RealTimeEvent, 'id' | 'date'>) => {
    setLatestEvent({
      ...event,
      id: String(Date.now()),
      date: new Date().toLocaleString('fr-FR')
    });
  };

  const clearLatestEvent = () => setLatestEvent(undefined);

  const addMedicalRequest = (request: Omit<MedicalRequest, 'id' | 'status' | 'date' | 'severity'> & { severity: MedicalRequest['severity'] }) => {
    const newRequest: MedicalRequest = {
      ...request,
      id: String(Date.now()),
      date: new Date().toLocaleString('fr-FR'),
      status: 'pending'
    };
    setMedicalRequests(prev => [newRequest, ...prev]);
    
    // Déclenche une alerte temps réel (WebSocket simulation)
    triggerRealTimeEvent({
      type: newRequest.type === 'urgence' ? 'medical_emergency' : 'medical_rdv',
      title: newRequest.type === 'urgence' ? 'ALERTE MÉDICALE URGENTE' : 'Nouvelle Demande de RDV',
      message: `${newRequest.inmateName} : ${newRequest.reason}`,
      severity: newRequest.severity
    });
  };

  const updateMedicalRequestStatus = (id: string, status: MedicalRequest['status']) => {
    setMedicalRequests(prev => {
      const updated = prev.map(req => 
        req.id === id ? { ...req, status } : req
      );
      
      const request = updated.find(r => r.id === id);
      if (request) {
        triggerRealTimeEvent({
          type: 'medical_update',
          title: 'MISE À JOUR MÉDICALE',
          message: `La demande pour ${request.inmateName} a été ${status === 'treated' ? 'Traitée' : 'Annulée'}.`,
          data: { inmateId: request.inmateId }
        });
      }
      return updated;
    });
  };

  /**
   * addInmate — simule un appel POST vers le backend.
   */
  const addInmate = async (inmate: Omit<Inmate, 'id'>): Promise<void> => {
    setIsLoading(true);
    await new Promise(resolve => setTimeout(resolve, 600));
    const newId = String(Date.now());
    const saved: Inmate = { ...inmate, id: newId };
    setInmates(prev => [...prev, saved]);
    
    // Notification pour le Directeur
    triggerRealTimeEvent({
      type: 'new_inmate',
      title: 'NOUVELLE ADMISSION',
      message: `Le détenu ${saved.firstName} ${saved.lastName} a été enregistré.`
    });
    
    setIsLoading(false);
  };

  /**
   * refreshInmates — simule un appel GET /inmates vers le backend.
   * Quand le vrai backend sera prêt, remplacer par:
   * const data = await api.get('/inmates')
   * setInmates(data)
   */
  const refreshInmates = async (): Promise<void> => {
    setIsLoading(true);
    // --- Simulation backend (à remplacer par fetch réel) ---
    await new Promise(resolve => setTimeout(resolve, 400));
    // La liste est déjà à jour en mémoire, rien à remplacer en mode mock
    // -------------------------------------------------------
    setIsLoading(false);
  };

  const updateInmatePhoto = (id: string, photoUrl: string) => {
    setInmates(prev => prev.map(inmate => 
      inmate.id === id ? { ...inmate, photoUrl } : inmate
    ));
  };

  return (
    <InmatesDataContext.Provider value={{ 
      inmates, 
      medicalRequests, 
      latestEvent,
      isLoading, 
      setInmates, 
      addInmate, 
      refreshInmates, 
      updateInmatePhoto,
      addMedicalRequest,
      updateMedicalRequestStatus,
      triggerRealTimeEvent,
      clearLatestEvent
    }}>
      {children}
    </InmatesDataContext.Provider>
  );
};

export const useInmatesData = () => {
  const context = useContext(InmatesDataContext);
  if (context === undefined) {
    throw new Error("useInmatesData doit être utilisé au sein d'un InmatesDataProvider");
  }
  return context;
};
